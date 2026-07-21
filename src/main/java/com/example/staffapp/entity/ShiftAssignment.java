package com.example.staffapp.entity;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.common.PlanningId;
import ai.timefold.solver.core.api.domain.variable.PlanningVariable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

/**
 * 「この勤務枠(slot)の1人分の枠」に、誰(staff)を割り当てるかを表す。
 * 1つのShiftSlotに requiredStaff 人が必要な場合、その人数分だけ ShiftAssignment が作られる。
 * staffフィールドがTimefold Solverによって書き換えられる「planning variable」。
 */
@Entity
@PlanningEntity
public class ShiftAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @PlanningId
    private Long id;

    @ManyToOne
    private ShiftSlot slot;

    @ManyToOne
    @PlanningVariable(valueRangeProviderRefs = "staffRange")
    private Staff staff;

    public ShiftAssignment() {
    }

    public ShiftAssignment(ShiftSlot slot) {
        this.slot = slot;
    }

    // ----------------------------
    // 休憩ルール（安全版）
    // ----------------------------

    public long getBreakMinutes() {
        long minutes = slot.durationMinutes();

        if (minutes >= 8 * 60) return 60;
        if (minutes >= 6 * 60) return 45;
        return 0;
    }

    public boolean needsBreak() {
        return slot.durationMinutes() >= 6 * 60;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShiftSlot getSlot() {
        return slot;
    }

    public void setSlot(ShiftSlot slot) {
        this.slot = slot;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }
}
