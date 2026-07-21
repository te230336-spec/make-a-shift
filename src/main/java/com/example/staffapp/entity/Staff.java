package com.example.staffapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import jakarta.persistence.Column;

/**
 * スタッフ情報。
 * スタッフ管理CRUD用のJPAエンティティであると同時に、
 * シフト自動作成（Timefold Solver）の割当候補（value range）としても使われる。
 */
@Entity
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(unique = true)
    private String email;

    //@JsonIgnore
    private String password; // ハッシュ化された文字列が保存される

    private String pinCode;

    @Enumerated(EnumType.STRING)
    private Role role = Role.STAFF;



    // 勤務時間関連のプロパティ（計算用）
    private double weeklyWorkingHours; // 今週の合計労働時間
    private double monthlyOvertimeHours; // 今月の残業時間

    // スタッフが現在有効か（退職者などを無効化）
    private boolean active = true;

    // 登録日時
    private LocalDateTime createdAt;

    // 最終ログイン日時
    private LocalDateTime lastLoginAt;

    public Staff() {
        this.createdAt = LocalDateTime.now();
    }

    public Staff(Long id, String name, Role role) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.createdAt = LocalDateTime.now();
    }

    // ----------------------------
    // 役職に基づく判定ロジック
    // ----------------------------

    /** シフトでリーダー業務（ワンオペ・応援）を任せられるか */
    public boolean isLeader() {
        return role == Role.STORE_MANAGER || role == Role.VETERAN;
    }

    /** 店長かどうか（労務ルール判定・画面表示互換用） */
    public boolean isManager() {
        return role == Role.STORE_MANAGER;
    }

    /** ベテランかどうか（画面表示互換用） */
    public boolean isVeteran() {
        return role == Role.VETERAN;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public double getWeeklyWorkingHours() {
        return weeklyWorkingHours;
    }

    public void setWeeklyWorkingHours(double weeklyWorkingHours) {
        this.weeklyWorkingHours = weeklyWorkingHours;
    }

    public double getMonthlyOvertimeHours() {
        return monthlyOvertimeHours;
    }

    public void setMonthlyOvertimeHours(double monthlyOvertimeHours) {
        this.monthlyOvertimeHours = monthlyOvertimeHours;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }
}