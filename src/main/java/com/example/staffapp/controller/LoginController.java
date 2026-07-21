package com.example.staffapp.controller;


import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.staffapp.entity.LoginRequest;
import com.example.staffapp.entity.Staff;
import com.example.staffapp.repository.StaffRepository;


@RestController
@RequestMapping("/api/login")
@CrossOrigin
public class LoginController {


    @Autowired
    private StaffRepository staffRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        //System.out.println("★★★★★ LoginControllerに入りました ★★★★★");

        Optional<Staff> staffOpt =
                staffRepository.findByEmail(
                        request.getEmail()
                );


        if(staffOpt.isEmpty()) {

            return ResponseEntity
                    .status(401)
                    .body("メールアドレスまたはパスワードが違います");
        }


        Staff staff = staffOpt.get();


        // パスワード比較
        if(!passwordEncoder.matches(
                request.getPassword(),
                staff.getPassword())) {


            return ResponseEntity
                    .status(401)
                    .body("メールアドレスまたはパスワードが違います");

        }


        // 最終ログイン更新
        staff.setLastLoginAt(
                LocalDateTime.now()
        );

        staffRepository.save(staff);


        return ResponseEntity.ok(staff);

    }

}