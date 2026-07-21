package com.example.staffapp.entity;

/**
 * スタッフの役職。
 * STORE_MANAGER: 店長（ワンオペ可・残業は月45時間まで許可）
 * VETERAN      : ベテランスタッフ（リーダー業務は可能だが、労務ルール上はアルバイト扱い）
 * STAFF        : 通常のアルバイト（残業不可）
 */
public enum Role {
    STORE_MANAGER,
    VETERAN,
    STAFF
}
