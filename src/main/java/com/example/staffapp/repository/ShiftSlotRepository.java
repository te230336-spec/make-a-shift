package com.example.staffapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.staffapp.entity.ShiftSlot;

@Repository
public interface ShiftSlotRepository extends JpaRepository<ShiftSlot, Long> {
}
