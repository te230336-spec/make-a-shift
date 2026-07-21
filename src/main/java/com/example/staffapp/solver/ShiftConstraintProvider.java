package com.example.staffapp.solver;

import ai.timefold.solver.core.api.score.HardSoftScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintCollectors;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.ConstraintProvider;
import ai.timefold.solver.core.api.score.stream.Joiners;

import com.example.staffapp.entity.ShiftAssignment;
import com.example.staffapp.entity.ShiftSlot;

/**
 * シフト自動作成の制約（ハード制約は絶対に破ってはいけないルール、
 * ソフト制約はできるだけ守りたい・より良くしたいルール）。
 */
public class ShiftConstraintProvider implements ConstraintProvider {

    private static final int WEEKLY_LIMIT_MINUTES = 40 * 60; // 週40時間
    private static final int DAILY_OVERTIME_MINUTES = 8 * 60; // 1勤務8時間

    @Override
    public Constraint[] defineConstraints(ConstraintFactory factory) {
        return new Constraint[] {
                noDuplicateStaffInSameSlot(factory),
                noOverlappingShiftsForStaff(factory),
                leaderRequiredCoverage(factory),
                weeklyHourLimit(factory),
                noOvertimeForNonManagers(factory),
                fairWorkloadDistribution(factory),
        };
    }

    /** 同じ勤務枠に同じスタッフを二重に割り当ててはいけない */
    Constraint noDuplicateStaffInSameSlot(ConstraintFactory factory) {
        return factory.forEachUniquePair(ShiftAssignment.class,
                        Joiners.equal(ShiftAssignment::getSlot),
                        Joiners.equal(ShiftAssignment::getStaff))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("NoDuplicateStaffInSameSlot");//"同一勤務枠へのスタッフ重複割当"
    }

    /** 同じスタッフが、同じ日の時間帯が重なる勤務枠に同時に入ってはいけない */
    Constraint noOverlappingShiftsForStaff(ConstraintFactory factory) {
        return factory.forEachUniquePair(ShiftAssignment.class,
                        Joiners.equal(ShiftAssignment::getStaff))
                .filter((a1, a2) -> !a1.getSlot().equals(a2.getSlot()) && overlaps(a1.getSlot(), a2.getSlot()))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("NoOverlappingShiftsForStaff");//"スタッフの勤務時間帯の重複"
    }

    /** リーダーが必要な勤務枠には、店長またはベテランを最低1人割り当てる */
    Constraint leaderRequiredCoverage(ConstraintFactory factory) {
        return factory.forEach(ShiftSlot.class)
                .filter(ShiftSlot::isRequiresLeader)
                .ifNotExists(ShiftAssignment.class,
                        Joiners.equal(slot -> slot, ShiftAssignment::getSlot),
                        Joiners.filtering((slot, assignment) ->
                                assignment.getStaff() != null && assignment.getStaff().isLeader()))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("LeaderRequiredCoverage");//"リーダー必須勤務枠のリーダー未配置"
    }

    /** 週40時間を超える勤務は禁止（超過分に応じて重く罰する） */
    Constraint weeklyHourLimit(ConstraintFactory factory) {
        return factory.forEach(ShiftAssignment.class)
                .filter(a -> a.getStaff() != null)
                .groupBy(ShiftAssignment::getStaff,
                        ConstraintCollectors.sum(a -> (int) a.getSlot().durationMinutes()))
                .filter((staff, totalMinutes) -> totalMinutes > WEEKLY_LIMIT_MINUTES)
                .penalize(HardSoftScore.ONE_HARD, (staff, totalMinutes) -> totalMinutes - WEEKLY_LIMIT_MINUTES)
                .asConstraint("weeklyHourLimit");//"週40時間超過"
    }

    /** 店長以外（アルバイト・ベテラン）は、1回8時間を超える残業的な勤務を割り当てない */
    Constraint noOvertimeForNonManagers(ConstraintFactory factory) {
        return factory.forEach(ShiftAssignment.class)
                .filter(a -> a.getStaff() != null
                        && !a.getStaff().isManager()
                        && a.getSlot().durationMinutes() > DAILY_OVERTIME_MINUTES)
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("NoOvertimeForNonManagers");//"非店長スタッフの残業禁止"
    }

    /**
     * 特定のスタッフに勤務時間が偏らないようにする（ソフト制約）。
     * 割当総時間の合計は勤務枠の構成上ほぼ一定なので、各スタッフの合計時間の「2乗」を罰することで、
     * 均等に分散させたときに罰則が最小になるようにしている（負荷分散の定番テクニック）。
     */
    Constraint fairWorkloadDistribution(ConstraintFactory factory) {
        return factory.forEach(ShiftAssignment.class)
                .filter(a -> a.getStaff() != null)
                .groupBy(ShiftAssignment::getStaff,
                        ConstraintCollectors.sum(a -> (int) a.getSlot().durationMinutes()))
                .penalize(HardSoftScore.ONE_SOFT,
                        (staff, totalMinutes) -> (totalMinutes * totalMinutes) / 3600)
                .asConstraint("FairWorkloadDistribution");//"勤務時間の偏りの抑制"
    }

    private boolean overlaps(ShiftSlot s1, ShiftSlot s2) {
        if (!s1.getDate().equals(s2.getDate())) {
            return false;
        }
        return s1.getStartTime().isBefore(s2.getEndTime()) && s2.getStartTime().isBefore(s1.getEndTime());
    }
}
