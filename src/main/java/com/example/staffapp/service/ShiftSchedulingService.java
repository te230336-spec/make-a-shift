package com.example.staffapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import ai.timefold.solver.core.api.solver.SolverManager;

import com.example.staffapp.domain.ShiftSchedule;
import com.example.staffapp.entity.ShiftAssignment;
import com.example.staffapp.entity.ShiftSlot;
import com.example.staffapp.entity.ShiftRequest; // 【追加】シフト希望用Entity
import com.example.staffapp.entity.Staff;
import com.example.staffapp.repository.ShiftAssignmentRepository;
import com.example.staffapp.repository.ShiftSlotRepository;
import com.example.staffapp.repository.ShiftRequestRepository; // 【追加】シフト希望用Repository
import com.example.staffapp.repository.StaffRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShiftSchedulingService {

    private static final Long PROBLEM_ID = 1L;

    private final StaffRepository staffRepository;
    private final ShiftSlotRepository shiftSlotRepository;
    private final ShiftAssignmentRepository shiftAssignmentRepository;
    private final ShiftRequestRepository shiftRequestRepository; // 【追加】
    private final SolverManager<ShiftSchedule> solverManager;

    /**
     * 登録済みのスタッフ・勤務枠から、シフトを自動作成する。
     * 各勤務枠(ShiftSlot)の requiredStaff の数だけ ShiftAssignment を用意し、
     * Timefold Solverに解かせたあと、結果をDBに保存して返す。
     */
    public ShiftSchedule solve() {
        List<Staff> staffList = staffRepository.findAll();
        List<ShiftSlot> slotList = shiftSlotRepository.findAll();

        if (staffList.isEmpty()) {
            throw new IllegalStateException("スタッフが1人も登録されていません。");
        }
        if (slotList.isEmpty()) {
            throw new IllegalStateException("勤務枠が1件も登録されていません。");
        }

        List<ShiftAssignment> assignmentList = new ArrayList<>();

        long assignmentId = 1;

        for (ShiftSlot slot : slotList) {
            for (int i = 0; i < slot.getRequiredStaff(); i++) {

                ShiftAssignment assignment = new ShiftAssignment(slot);
                assignment.setId(assignmentId++);

                assignmentList.add(assignment);
            }
        }

        ShiftSchedule problem = new ShiftSchedule(staffList, slotList, assignmentList);

        try {
            ShiftSchedule solution = solverManager.solve(PROBLEM_ID, problem).getFinalBestSolution();

            // 直近の割当結果を置き換える
            shiftAssignmentRepository.deleteAll();
            shiftAssignmentRepository.saveAll(solution.getAssignmentList());

            return solution;
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException("シフトの自動作成に失敗しました。", e);
        }
    }

    public List<ShiftAssignment> getCurrentAssignments() {
        return shiftAssignmentRepository.findAll();
    }

    // === 【ここから下を追加】 ===

    /**
     * スタッフからのシフト希望をデータベースに保存する。
     * @param request 画面から送信されたシフト希望データ
     * @return 保存されたデータ
     */
    public ShiftRequest saveShiftRequest(ShiftRequest request) {
        return shiftRequestRepository.save(request);
    }
}