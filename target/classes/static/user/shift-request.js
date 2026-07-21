// ログイン機能などで現在のアカウント名が特定されている想定の変数
let currentLoginUser = "田中"; //[cite: 2]

// 各曜日行の「休み状態」を管理するオブジェクト
let dayOffStatus = { //[cite: 2]
    mon: false, tue: false, wed: false, thu: false, fri: false, sat: false, sun: false //[cite: 2]
};

// ★修正：ボタン自身の要素を受け取り、見た目とinputの有効/無効を切り替える
function toggleDayOff(dayKey, btnElement) {
    // 状態を反転
    dayOffStatus[dayKey] = !dayOffStatus[dayKey]; //[cite: 2]
    
    const rowEl = document.getElementById(`row-${dayKey}`); //[cite: 2]
    if (!rowEl) return; //[cite: 2]

    const inputs = rowEl.querySelectorAll("input");

    if (dayOffStatus[dayKey]) { //[cite: 2]
        // 休みにする場合
        rowEl.classList.add("is-off"); //[cite: 2]
        btnElement.textContent = "〇 シフトに入れる";
        btnElement.classList.add("revert");
        
        // 入力できないようにロックする
        inputs.forEach(input => input.disabled = true);
    } else {
        // 休みを解除してシフトに入れる場合
        rowEl.classList.remove("is-off"); //[cite: 2]
        btnElement.textContent = "✕ 休みにする";
        btnElement.classList.remove("revert");

        // 入力ロックを解除する
        inputs.forEach(input => input.disabled = false);
    }
}

function submitWeeklyShift(event) {
    event.preventDefault(); //[cite: 2]

    const weekVal = document.getElementById("target-week").value; //[cite: 2]
    const days = ['mon', 'tue', 'wed', 'thu', 'fri', 'sat', 'sun']; //[cite: 2]
    const dayLabels = { mon: "月", tue: "火", wed: "水", thu: "木", fri: "金", sat: "土", sun: "日" }; //[cite: 2]

    let newRequests = []; //[cite: 2]
    
    // 固定カラー（店長画面のバー描画用）
    const userColors = { color: "#F6C9DA", dark: "#9A3C60" }; //[cite: 2]

    days.forEach(day => {
        // 休み（✕）が押されていない曜日だけデータを収集
        if (!dayOffStatus[day]) { //[cite: 2]
            const rowEl = document.getElementById(`row-${day}`); //[cite: 2]
            if (!rowEl) return; // 曜日のHTMLが存在しない場合はスキップ

            const startTimeStr = rowEl.querySelector(".start-t").value; //[cite: 2]
            const endTimeStr = rowEl.querySelector(".end-t").value; //[cite: 2]
            const memo = rowEl.querySelector(".row-memo").value.trim(); //[cite: 2]

            const startHour = parseTimeToDouble(startTimeStr); //[cite: 2]
            const endHour = parseTimeToDouble(endTimeStr); //[cite: 2]

            if (startHour < endHour) { //[cite: 2]
                newRequests.push({ //[cite: 2]
                    name: currentLoginUser, //[cite: 2]
                    start: startHour, //[cite: 2]
                    end: endHour, //[cite: 2]
                    left: 0, //[cite: 2]
                    color: userColors.color, //[cite: 2]
                    dark: userColors.dark, //[cite: 2]
                    date: `${weekVal} (${dayLabels[day]})`, //[cite: 2]
                    memo: memo || "なし" //[cite: 2]
                });
            }
        }
    });

    if (newRequests.length === 0) { //[cite: 2]
        alert("出勤希望日の入力がありません（すべて休み、または時間不正）。"); //[cite: 2]
        return; //[cite: 2]
    }

    // ローカルストレージへの保存処理（店長画面へ連動）
    let savedShifts = JSON.parse(localStorage.getItem("submittedShifts") || "[]"); //[cite: 2]
    
    // サンプルデータとのバッティング回避
    if (savedShifts.length === 0) { //[cite: 2]
        savedShifts = [ //[cite: 2]
            { name: "田中", start: 9, end: 13, left: 0, color: "#F6C9DA", dark: "#9A3C60" }, //[cite: 2]
            { name: "佐藤", start: 11, end: 17, left: 75, color: "#CFE4F5", dark: "#2C6690" }, //[cite: 2]
            { name: "鈴木", start: 14, end: 20, left: 150, color: "#DCE9C8", dark: "#5B753C" } //[cite: 2]
        ];
    }

    // 収集したデータをマージ
    savedShifts = [...savedShifts, ...newRequests]; //[cite: 2]
    localStorage.setItem("submittedShifts", JSON.stringify(savedShifts)); //[cite: 2]

    alert(`👍 ${currentLoginUser}さんの1週間分（${newRequests.length}日間分）の希望シフトを保存しました！店長画面で確認できます。`); //[cite: 2]
}

// "09:00" -> 9 へのパース用
function parseTimeToDouble(timeStr) { //[cite: 2]
    const parts = timeStr.split(":"); //[cite: 2]
    return parseInt(parts[0], 10) + (parseInt(parts[1], 10) / 60); //[cite: 2]
} //[cite: 2]