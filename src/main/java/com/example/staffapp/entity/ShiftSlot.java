package com.example.staffapp.entity;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * 勤務枠（この日のこの時間帯に、何人・どんな条件で人員が必要か）。
 * シフト自動作成の入力データであり、Timefold Solverから見ると「変化しない事実（problem fact）」。
 */
@Entity
public class ShiftSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    private int requiredStaff;
    private boolean requiresLeader;

    public ShiftSlot() {
    }

    public ShiftSlot(LocalDate date, LocalTime startTime, LocalTime endTime,
                      int requiredStaff, boolean requiresLeader) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public int getRequiredStaff() {
        return requiredStaff;
    }

    public void setRequiredStaff(int requiredStaff) {
        this.requiredStaff = requiredStaff;
    }

    public boolean isRequiresLeader() {
        return requiresLeader;
    }

    public void setRequiresLeader(boolean requiresLeader) {
        this.requiresLeader = requiresLeader;
    }
}
