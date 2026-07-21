package com.example.staffapp.domain;

import java.util.List;

import ai.timefold.solver.core.api.domain.solution.PlanningEntityCollectionProperty;
import ai.timefold.solver.core.api.domain.solution.PlanningScore;
import ai.timefold.solver.core.api.domain.solution.PlanningSolution;
import ai.timefold.solver.core.api.domain.solution.ProblemFactCollectionProperty;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.score.HardSoftScore;

import com.example.staffapp.entity.ShiftAssignment;
import com.example.staffapp.entity.ShiftSlot;
import com.example.staffapp.entity.Staff;

/**
 * シフト自動作成1回分の問題(入力)と解(出力)をまとめて持つプランニング解。
 * DBに永続化されるエンティティではなく、Solverに渡す/受け取るための入れ物。
 */
@PlanningSolution
public class ShiftSchedule {

    @ValueRangeProvider(id = "staffRange")
    @ProblemFactCollectionProperty
    private List<Staff> staffList;

    @ProblemFactCollectionProperty
    private List<ShiftSlot> slotList;

    @PlanningEntityCollectionProperty
    private List<ShiftAssignment> assignmentList;

    @PlanningScore
    private HardSoftScore score;

    public ShiftSchedule() {
    }

    public ShiftSchedule(List<Staff> staffList,
                          List<ShiftSlot> slotList,
                          List<ShiftAssignment> assignmentList) {
        this.staffList = staffList;
        this.slotList = slotList;
        this.assignmentList = assignmentList;
    }

    public List<Staff> getStaffList() {
        return staffList;
    }

    public void setStaffList(List<Staff> staffList) {
        this.staffList = staffList;
    }

    public List<ShiftSlot> getSlotList() {
        return slotList;
    }

    public void setSlotList(List<ShiftSlot> slotList) {
        this.slotList = slotList;
    }

    public List<ShiftAssignment> getAssignmentList() {
        return assignmentList;
    }

    public void setAssignmentList(List<ShiftAssignment> assignmentList) {
        this.assignmentList = assignmentList;
    }

    public HardSoftScore getScore() {
        return score;
    }

    public void setScore(HardSoftScore score) {
        this.score = score;
    }
}
