package com.example.staffapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.staffapp.entity.ShiftRequest;

@Repository
public interface ShiftRequestRepository extends JpaRepository<ShiftRequest, Long> {
    // JpaRepositoryを継承するだけで基本的なCRUD（保存・検索・更新・削除）操作が可能になります。
    // 特殊な検索（例：特定のスタッフIDの希望だけを探す等）が必要になった場合のみ、ここにメソッドを追加します。
}