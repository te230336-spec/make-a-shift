// 画面が読み込まれたら勤務枠一覧を取得
document.addEventListener('DOMContentLoaded', () => {
    fetchSlotList();
});

// ----------------------------
// 勤務枠一覧取得
// ----------------------------
function fetchSlotList() {
    fetch('/api/shifts/slots')
        .then(response => response.json())
        .then(data => {
            const tbody = document.getElementById('slotTableBody');
            tbody.innerHTML = '';

            data.forEach(slot => {
                const tr = document.createElement('tr');

                tr.innerHTML = `
                    <td>${slot.date}</td>
                    <td>${slot.startTime} ～ ${slot.endTime}</td>
                    <td>${slot.requiredStaff}人</td>
                    <td>${slot.requiresLeader ? '必須' : '-'}</td>
                    <td>
                        <button type="button" onclick="deleteSlot(${slot.id})">
                            削除
                        </button>
                    </td>
                `;

                tbody.appendChild(tr);
            });
        })
        .catch(error => {
            console.error('勤務枠取得エラー:', error);
        });
}

// ----------------------------
// 勤務枠登録
// ----------------------------
document.getElementById('slotForm').addEventListener('submit', function(event) {

    event.preventDefault();

    const newSlot = {
        date: document.getElementById('slotDate').value,
        startTime: document.getElementById('slotStart').value,
        endTime: document.getElementById('slotEnd').value,
        requiredStaff: parseInt(document.getElementById('slotRequiredStaff').value),
        requiresLeader: document.getElementById('slotRequiresLeader').checked
    };

    fetch('/api/shifts/slots', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(newSlot)
    })
    .then(response => {
        if (response.ok) {
            alert('勤務枠を登録しました。');
            this.reset();
            fetchSlotList();
        } else {
            alert('勤務枠の登録に失敗しました。');
        }
    })
    .catch(error => {
        console.error('登録エラー:', error);
    });

});

// ----------------------------
// 勤務枠削除
// ----------------------------
function deleteSlot(id) {

    if (!confirm('この勤務枠を削除しますか？')) {
        return;
    }

    fetch(`/api/shifts/slots/${id}`, {
        method: 'DELETE'
    })
    .then(response => {

        if (!response.ok) {
            throw new Error('削除失敗');
        }

        fetchSlotList();
    })
    .catch(error => {
        console.error('削除エラー:', error);
    });

}

// ----------------------------
// シフト自動作成
// ----------------------------
document.getElementById('solveButton').addEventListener('click', () => {

    const resultArea = document.getElementById('shiftResultArea');

    resultArea.innerHTML =
        '<p>シフトを自動作成しています...（数秒かかります）</p>';

    fetch('/api/shifts/solve', {
        method: 'POST'
    })
    .then(response => {

        if (!response.ok) {
            throw new Error('シフト作成失敗');
        }

        return response.json();
    })
    .then(schedule => {
        renderShiftResult(schedule);
    })
    .catch(error => {

        console.error(error);

        resultArea.innerHTML =
            '<p>シフトの自動作成に失敗しました。</p>';
    });

});

// ----------------------------
// 作成結果表示
// ----------------------------
function renderShiftResult(schedule) {

    const resultArea = document.getElementById('shiftResultArea');

    resultArea.innerHTML = '';

    const score = document.createElement('p');
    score.textContent =
        `スコア：${schedule.score}`;

    resultArea.appendChild(score);

    const table = document.createElement('table');

    table.innerHTML = `
        <thead>
            <tr>
                <th>日付</th>
                <th>時間</th>
                <th>担当スタッフ</th>
            </tr>
        </thead>
        <tbody></tbody>
    `;

    const tbody = table.querySelector('tbody');

    const assignments = [...schedule.assignmentList];

    assignments.sort((a, b) => {

        if (a.slot.date !== b.slot.date) {
            return a.slot.date.localeCompare(b.slot.date);
        }

        return a.slot.startTime.localeCompare(b.slot.startTime);
    });

    assignments.forEach(assignment => {

        const tr = document.createElement('tr');

        tr.innerHTML = `
            <td>${assignment.slot.date}</td>
            <td>${assignment.slot.startTime} ～ ${assignment.slot.endTime}</td>
            <td>${assignment.staff ? assignment.staff.name : '未割当'}</td>
        `;

        tbody.appendChild(tr);

    });

    resultArea.appendChild(table);

}