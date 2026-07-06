package com.example.shift.domain;

import org.timefold.solver.core.api.domain.solution.*;
import org.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.List;

@PlanningSolution
public class ShiftSchedule {

    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "staffRange")
    private List<Staff> staffList;

    @ProblemFactCollectionProperty
    private List<ShiftSlot> slotList;

    @PlanningEntityCollectionProperty
    private List<ShiftAssignment> assignmentList;

    @PlanningScore
    private HardSoftScore score;

    public ShiftSchedule() {}

    public ShiftSchedule(List<Staff> staffList,
                         List<ShiftSlot> slotList,
                         List<ShiftAssignment> assignmentList) {
        this.staffList = staffList;
        this.slotList = slotList;
        this.assignmentList = assignmentList;
    }

    public List<Staff> getStaffList() { return staffList; }
    public List<ShiftSlot> getSlotList() { return slotList; }
    public List<ShiftAssignment> getAssignmentList() { return assignmentList; }

    public void setStaffList(List<Staff> staffList) { this.staffList = staffList; }
    public void setSlotList(List<ShiftSlot> slotList) { this.slotList = slotList; }
    public void setAssignmentList(List<ShiftAssignment> assignmentList) { this.assignmentList = assignmentList; }

    public HardSoftScore getScore() { return score; }
    public void setScore(HardSoftScore score) { this.score = score; }
}