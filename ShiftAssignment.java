package com.example.shift.domain;

import org.timefold.solver.core.api.domain.entity.PlanningEntity;
import org.timefold.solver.core.api.domain.variable.PlanningVariable;

@PlanningEntity
public class ShiftAssignment {

    private Long id;
    private ShiftSlot slot;

    @PlanningVariable(valueRangeProviderRefs = "staffRange")
    private Staff staff;

    public ShiftAssignment() {}

    public ShiftAssignment(Long id, ShiftSlot slot) {
        this.id = id;
        this.slot = slot;
    }

    public Long getId() { return id; }
    public ShiftSlot getSlot() { return slot; }
    public Staff getStaff() { return staff; }

    public void setId(Long id) { this.id = id; }
    public void setSlot(ShiftSlot slot) { this.slot = slot; }
    public void setStaff(Staff staff) { this.staff = staff; }

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
}