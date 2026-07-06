package com.example.shift.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;

public class ShiftSlot {

    private Long id;
    private LocalDate date;

    private LocalTime startTime;
    private LocalTime endTime;

    private int requiredStaff;
    private boolean requiresLeader;

    public ShiftSlot() {}

    public ShiftSlot(Long id, LocalDate date,
                     LocalTime startTime, LocalTime endTime,
                     int requiredStaff, boolean requiresLeader) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.requiredStaff = requiredStaff;
        this.requiresLeader = requiresLeader;
    }

    public long durationMinutes() {
        return Duration.between(startTime, endTime).toMinutes();
    }

    public boolean isTuesday() {
        return date.getDayOfWeek().getValue() == 2;
    }

    public Long getId() { return id; }
    public LocalDate getDate() { return date; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public int getRequiredStaff() { return requiredStaff; }
    public boolean isRequiresLeader() { return requiresLeader; }

    public void setId(Long id) { this.id = id; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public void setRequiredStaff(int requiredStaff) { this.requiredStaff = requiredStaff; }
    public void setRequiresLeader(boolean requiresLeader) { this.requiresLeader = requiresLeader; }
}