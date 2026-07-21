package com.example.staffapp.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.staffapp.entity.Staff;
import com.example.staffapp.service.StaffService;

import lombok.RequiredArgsConstructor;

@RestController // データ（JSONなど）を返すAPI用のコントローラー
@RequestMapping("/api/staff")
@RequiredArgsConstructor
public class StaffController {
    private final StaffService staffService;

    // 全スタッフ情報を取得するAPI (GET /api/staff)
    @GetMapping
    public List<Staff> getStaffList() {
        return staffService.getAllStaff();
    }

    // 新規スタッフを登録するAPI (POST /api/staff)
    @PostMapping
    public Staff addStaff(@RequestBody Staff staff) {

    /*System.out.println("===========");
    System.out.println(staff.getName());
    System.out.println(staff.getEmail());
    System.out.println(staff.getPassword());
    System.out.println("===========");
    */
        return staffService.registerStaff(staff);
    }
}
