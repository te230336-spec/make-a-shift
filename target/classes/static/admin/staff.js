// 役職表示用
const ROLE_LABEL = {
    STORE_MANAGER: '店長',
    VETERAN: 'ベテラン',
    STAFF: 'アルバイト'
};

// 画面表示時にスタッフ一覧を取得
document.addEventListener('DOMContentLoaded', () => {
    fetchStaffList();
});

// ----------------------------
// スタッフ一覧取得
// ----------------------------
function fetchStaffList() {
    fetch('/api/staff')
        .then(response => response.json())
        .then(data => {
            const tbody = document.getElementById('staffTableBody');
            tbody.innerHTML = '';

            data.forEach(staff => {
                const tr = document.createElement('tr');

                tr.innerHTML = `
                    <td>${staff.name}</td>
                    <td>${ROLE_LABEL[staff.role] || staff.role}</td>
                    <td>${staff.role === 'VETERAN' ? '〇' : '-'}</td>
                    <td>${staff.weeklyWorkingHours}h</td>
                    <td>${staff.monthlyOvertimeHours}h</td>
                `;

                tbody.appendChild(tr);
            });
        })
        .catch(error => {
            console.error('スタッフ一覧取得エラー:', error);
        });
}

// ----------------------------
// スタッフ登録
// ----------------------------
document.getElementById('staffForm').addEventListener('submit', function (event) {
    event.preventDefault();

    const newStaff = {
        name: document.getElementById('name').value,
        email: document.getElementById('email').value,
        password: document.getElementById('password').value,
        pinCode: document.getElementById('pinCode').value,
        role: document.getElementById('role').value,
        weeklyWorkingHours: 0.0,
        monthlyOvertimeHours: 0.0
    };

    fetch('/api/staff', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(newStaff)
    })
        .then(response => {
            if (response.ok) {
                alert('スタッフを登録しました。');
                this.reset();
                fetchStaffList();
            } else {
                alert('スタッフの登録に失敗しました。');
            }
        })
        .catch(error => {
            console.error('登録エラー:', error);
        });
});