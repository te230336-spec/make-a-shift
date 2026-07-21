package com.example.staffapp.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.staffapp.domain.ShiftSchedule;
import com.example.staffapp.entity.ShiftAssignment;
import com.example.staffapp.entity.ShiftSlot;
import com.example.staffapp.repository.ShiftSlotRepository;
import com.example.staffapp.service.ShiftSchedulingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/shifts")
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftSlotRepository shiftSlotRepository;
    private final ShiftSchedulingService shiftSchedulingService;

    // 勤務枠の一覧取得 (GET /api/shifts/slots)
    @GetMapping("/slots")
    public List<ShiftSlot> getSlots() {
        return shiftSlotRepository.findAll();
    }

    // 勤務枠の新規登録 (POST /api/shifts/slots)
    @PostMapping("/slots")
    public ShiftSlot addSlot(@RequestBody ShiftSlot slot) {
        return shiftSlotRepository.save(slot);
    }

    // 勤務枠の削除 (DELETE /api/shifts/slots/{id})
    @DeleteMapping("/slots/{id}")
    public void deleteSlot(@PathVariable Long id) {
        shiftSlotRepository.deleteById(id);
    }

    // シフト自動作成の実行 (POST /api/shifts/solve)
    @PostMapping("/solve")
    public ShiftSchedule solve() {
        return shiftSchedulingService.solve();
    }

    // 直近の自動作成結果の取得 (GET /api/shifts/assignments)
    @GetMapping("/assignments")
    public List<ShiftAssignment> getAssignments() {
        return shiftSchedulingService.getCurrentAssignments();
    }
    // シフト希望の提出 (POST /api/shifts/requests)
    @PostMapping("/requests")
    public ShiftAssignment submitShiftRequest(@RequestBody ShiftAssignment requestData) {
        // フロントエンドからJSON形式で送られたデータ（requestData）を受け取ります。
        
        // 実際にはService層でデータベースへ保存する処理を呼び出します。
        // （※以下のメソッドはShiftSchedulingServiceに新規作成する必要があります）
        // return shiftSchedulingService.saveShiftRequest(requestData);
        
        return requestData; // エラーにならないよう、一旦受け取ったデータをそのまま返す仮実装です
    }
}
