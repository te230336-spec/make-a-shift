package com.example.staffapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.staffapp.entity.Role;
import com.example.staffapp.entity.Staff;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    // 指定した役職のスタッフだけを検索する（例：店長だけを探す）
    List<Staff> findByRole(Role role);

    // メールアドレスでスタッフ検索（ログイン用）
    Optional<Staff> findByEmail(String email);
}
