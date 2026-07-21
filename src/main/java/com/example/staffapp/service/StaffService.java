package com.example.staffapp.service;

import java.time.LocalTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.staffapp.entity.Staff;
import com.example.staffapp.repository.StaffRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StaffService {
    private final StaffRepository staffRepository;
    // Spring Securityが提供するハッシュ化エンコーダー
    private final PasswordEncoder passwordEncoder;

    public java.util.List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    // 1. パスワードをハッシュ化してスタッフを登録
    public Staff registerStaff(Staff staff) {
        System.out.println("name = " + staff.getName());
        System.out.println("email = " + staff.getEmail());
        System.out.println("password = " + staff.getPassword());
        System.out.println("pin = " + staff.getPinCode());
        System.out.println("role = " + staff.getRole());

        // 平文のパスワードをハッシュ化して上書き保存
        String hashedPassword = passwordEncoder.encode(staff.getPassword());
        staff.setPassword(hashedPassword);

        // タブレットのピンコードも同様にハッシュ化する場合はここで処理
        return staffRepository.save(staff);
    }

    // 2. 営業時間内の勤務かチェック (9:00 〜 20:00)
    public boolean isWithinBusinessHours(LocalTime startTime, LocalTime endTime) {
        LocalTime businessStart = LocalTime.of(9, 0);
        LocalTime businessEnd = LocalTime.of(20, 0);

        return !startTime.isBefore(businessStart) && !endTime.isAfter(businessEnd);
    }

    // 3. 労働時間に応じた休憩時間の自動計算
    // 6時間以上 => 45分 (0.75時間), 8時間以上 => 1時間 (1.0時間)
    public double calculateRequiredBreak(double workingHours) {
        if (workingHours >= 8.0) {
            return 1.0;
        } else if (workingHours >= 6.0) {
            return 0.75;
        }
        return 0.0;
    }

    // 4. 労務ルールのバリデーション（週40時間、残業上限のチェック）
    public void validateStaffHours(Staff staff, double additionalHours, double additionalOvertime) {
        // 週に一人40時間まで
        if (staff.getWeeklyWorkingHours() + additionalHours > 40.0) {
            throw new IllegalArgumentException("週の労働時間が40時間を超えています。");
        }

        // 残業時間のチェック（店長は45時間まで、アルバイト・ベテランはなし）
        if (staff.isManager()) {
            if (staff.getMonthlyOvertimeHours() + additionalOvertime > 45.0) {
                throw new IllegalArgumentException("店長の残業時間が月45時間を超えています。");
            }
        } else {
            if (additionalOvertime > 0 || staff.getMonthlyOvertimeHours() > 0) {
                throw new IllegalArgumentException("アルバイト・ベテランスタッフは残業できません。");
            }
        }
    }
}
