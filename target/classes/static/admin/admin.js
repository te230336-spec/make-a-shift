const STAFF_THEMES = {
    "1234": { name: "田中", color: "#F6C9DA", dark: "#9A3C60" }
};

let TASKS = [
    { id: 1, title: "レジの精算と現金確認", detail: "点検レシートを出して、金庫の額面と一致しているか数える。誤差があればノートに記録すること。", status: "todo", completedAt: null },
    { id: 2, title: "看板のライト交換", detail: "裏の倉庫にある『LED40形』の電球を使用してください。脚立を使うときは2人以上で作業すること。", status: "done", completedAt: new Date().getTime() }
];

window.onload = function() {
    focusPinInput();
    checkAndCleanExpiredTasks();
    renderTasks();
    document.body.onclick = focusPinInput;
};

function focusPinInput() {
    const el = document.getElementById("hidden-pin-input");
    if (el && document.getElementById("pin-screen").style.display !== "none") { el.focus(); }
}

function handlePinInput(event) {
    const val = event.target.value.replace(/\D/g, "");
    event.target.value = val;
    for (let i = 0; i < 4; i++) {
        const slot = document.getElementById(`slot-${i}`);
        if (slot) {
            slot.classList.toggle("filled", i < val.length);
            slot.classList.toggle("active-focus", i === val.length);
        }
    }
    if (val.length === 4) {
        setTimeout(() => { checkPin(val); }, 150);
    }
}

function checkPin(pin) {
    const staff = STAFF_THEMES[pin];
    if (staff) {
        document.getElementById("pin-screen").style.display = "none";
        document.getElementById("main-screen").style.display = "block";
        document.getElementById("current-user-name").textContent = staff.name + "さん";
        const av = document.getElementById("current-user-avatar");
        av.textContent = staff.name[0];
        av.style.backgroundColor = staff.color;
        av.style.color = staff.dark;
        document.getElementById("hidden-pin-input").value = "";
    } else {
        alert("PINコードが違います。もういちど入力してください。");
        document.getElementById("hidden-pin-input").value = "";
        for (let i = 0; i < 4; i++) {
            document.getElementById(`slot-${i}`).classList.remove("filled");
            document.getElementById(`slot-${i}`).classList.toggle("active-focus", i === 0);
        }
    }
}

function logOut() {
    document.getElementById("main-screen").style.display = "none";
    document.getElementById("pin-screen").style.display = "flex";
    setTimeout(focusPinInput, 100);
}

function checkAndCleanExpiredTasks() {
    const now = new Date().getTime();
    const ONE_MONTH_MS = 1000 * 60 * 60 * 24 * 30;
    TASKS = TASKS.filter(task => {
        if (task.status === "done" && task.completedAt) {
            if (now - task.completedAt > ONE_MONTH_MS) return false;
        }
        return true;
    });
}

function createNewTask(e) {
    e.preventDefault();
    const titleInput = document.getElementById("task-title");
    const detailInput = document.getElementById("task-detail");
    const task = {
        id: Date.now(),
        title: titleInput.value.trim(),
        detail: detailInput.value.trim(),
        status: "todo",
        completedAt: null
    };
    TASKS.unshift(task);
    titleInput.value = "";
    detailInput.value = "";
    renderTasks();
}

function completeTask(id) {
    TASKS = TASKS.map(t => t.id === id ? {...t, status: "done", completedAt: new Date().getTime()} : t);
    renderTasks();
}

function revertTask(id) {
    TASKS = TASKS.map(t => t.id === id ? {...t, status: "todo", completedAt: null} : t);
    renderTasks();
}

function renderTasks() {
    const todoList = document.getElementById("todo-list");
    const doneList = document.getElementById("done-list");
    todoList.innerHTML = ""; doneList.innerHTML = "";

    const todoTasks = TASKS.filter(t => t.status === "todo");
    const doneTasks = TASKS.filter(t => t.status === "done");

    if (todoTasks.length === 0) {
        todoList.innerHTML = `<p style="text-align:center; color:#999; font-size:14px; font-weight:800;">現在のタスクはありません 🎉</p>`;
    }
    if (doneTasks.length === 0) {
        doneList.innerHTML = `<p style="text-align:center; color:#999; font-size:14px; font-weight:800;">1ヶ月以内に終了したタスクはありません</p>`;
    }

    TASKS.forEach(task => {
        const html = `
            <div class="task-item-box">
                <div class="task-item-header">
                    <h3 class="task-item-title">${task.title}</h3>
                    ${task.status === 'todo' 
                        ? `<button class="btn-task-action complete" onclick="completeTask(${task.id})">✅ 完了</button>`
                        : `<button class="btn-task-action revert" onclick="revertTask(${task.id})">↩️ 未達に戻す</button>`
                    }
                </div>
                <div class="task-item-detail">${task.detail}</div>
                ${task.completedAt ? `<span class="task-date-info" style="font-size:11px; color:#888; display:block; margin-top:5px;">完了日: ${new Date(task.completedAt).toLocaleDateString()} (これより1ヶ月間表示)</span>` : ''}
            </div>
        `;
        if (task.status === "todo") todoList.insertAdjacentHTML("beforeend", html);
        else doneList.insertAdjacentHTML("beforeend", html);
    });
}