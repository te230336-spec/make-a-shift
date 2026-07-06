// ==========================================
// 1. データ管理（ダミーデータ）
// ==========================================
// シフト用のスタッフデータ（名前とカラーコード）
let currentStaffShifts = [
    { name: "田中", start: 9, end: 13, left: 0, bg: "#F6C9DA", text: "#9A3C60" },
    { name: "佐藤", start: 11, end: 17, left: 80, bg: "#CFE4F5", text: "#2C6690" },
    { name: "鈴木", start: 14, end: 20, left: 160, bg: "#DCE9C8", text: "#5B753C" }
];

// 初期タスクリスト
let todayTasks = [
    { id: 1, text: "レジの点検をする" },
    { id: 2, text: "お花のにおいずけ（確認）" }
];

// ==========================================
// 2. 画面読み込み時の初期化
// ==========================================
document.addEventListener("DOMContentLoaded", () => {
    // スライダーのイベント登録（文字サイズ・明るさ）
    initSliders();

    // シフトバーの描画
    renderShifts(currentStaffShifts);

    // タスクの初期描画 ＆ 追加ボタンイベント
    renderTasks();
    document.getElementById("btn-add-task").addEventListener("click", handleAddTask);

    // 自動作成ボタンのダミー処理
    document.getElementById("btn-auto-shift").addEventListener("click", () => {
        alert("提出された希望を計算して、重ならないように自動で綺麗に並べました！");
        // 綺麗に左位置を整列させる
        let fixedShifts = currentStaffShifts.map((s, idx) => ({ ...s, left: idx * 80 }));
        renderShifts(fixedShifts);
    });
});

// ==========================================
// 3. おばあちゃん向けかんたん調整（元のロジックを統合）
// ==========================================
function initSliders() {
    const fontSlider = document.getElementById("font-slider");
    const fontLabel = document.getElementById("font-size-label");
    const brightSlider = document.getElementById("bright-slider");
    const appEl = document.getElementById("app");

    // 5段階のフォントスケールマップ
    const fontScaleMap = { 1: 0.85, 2: 0.92, 3: 1.0, 4: 1.18, 5: 1.4 };
    const fontLabelMap = { 1: "小", 2: "やや小", 3: "標準", 4: "やや大", 5: "特大👵" };

    // 文字サイズスライダー変更時
    fontSlider.addEventListener("input", (e) => {
        const val = parseInt(e.target.value);
        appEl.style.setProperty("--font-scale", fontScaleMap[val]);
        fontLabel.textContent = fontLabelMap[val];
    });

    // 明るさスライダー変更時
    brightSlider.addEventListener("input", (e) => {
        const val = parseInt(e.target.value);
        appEl.style.setProperty("--bright", val / 100);
    });
}

// ==========================================
// 4. シフトバーのレンダリング
// ==========================================
function renderShifts(staffList) {
    const container = document.getElementById("shift-bars-container");
    if (!container) return;
    container.innerHTML = "";

    const HOUR_HEIGHT = 40; 
    const BASE_TOP = 9; 

    staffList.forEach(staff => {
        const bar = document.createElement("div");
        bar.className = "staff-bar";
        
        const duration = staff.end - staff.start;
        const topOffset = (staff.start - BASE_TOP) * HOUR_HEIGHT;
        const barHeight = duration * HOUR_HEIGHT;

        // 元ファイルのようにスタッフ固有の可愛い色を適用
        bar.style.top = `${topOffset}px`;
        bar.style.height = `${barHeight}px`;
        bar.style.left = `${staff.left}px`;
        bar.style.backgroundColor = staff.bg;
        bar.style.color = staff.text;

        bar.innerHTML = `
            <div class="staff-icon" style="color:${staff.text}">${staff.name[0]}</div>
            <div class="staff-name">${staff.name}</div>
        `;
        container.appendChild(bar);
    });
}

// ==========================================
// 5. タスク管理のロジック（追加・削除）
// ==========================================
function renderTasks() {
    const taskListUl = document.getElementById("task-list");
    taskListUl.innerHTML = "";

    todayTasks.forEach(task => {
        const li = document.createElement("li");
        li.className = "task-item";
        li.innerHTML = `
            <span>• ${task.text}</span>
            <button class="btn-delete" onclick="deleteTask(${task.id})">できた！</button>
        `;
        taskListUl.appendChild(li);
    });
}

function handleAddTask() {
    const input = document.getElementById("new-task-input");
    const text = input.value.trim();
    if (!text) return;

    const newTask = {
        id: Date.now(), // 簡易的なユニークID
        text: text
    };

    todayTasks.push(newTask);
    input.value = "";
    renderTasks();
}

// グローバルスコープから呼び出せるようにwindowに紐付け（削除ボタン用）
window.deleteTask = function(id) {
    todayTasks = todayTasks.filter(t => t.id !== id);
    renderTasks();
};