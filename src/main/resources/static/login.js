// フロントエンドテスト用のモックアカウントデータ
const MOCK_USER = {
    email: "test@example.com",
    password: "password123",
    name: "田中"
};

// 1. パスワードの表示・非表示を切り替える
function togglePasswordVisibility() {
    const passwordInput = document.getElementById("login-password");
    const toggleBtn = document.querySelector(".btn-toggle-password");
    
    if (passwordInput.type === "password") {
        passwordInput.type = "text";
        toggleBtn.textContent = "🙈"; // 非表示のアイコン
    } else {
        passwordInput.type = "password";
        toggleBtn.textContent = "👁️"; // 表示のアイコン
    }
}

// 2. ログインボタンが押された時の処理
function handleLogin(event){

    event.preventDefault();


    const email = document.getElementById("login-email").value;

    const password = document.getElementById("login-password").value;
    fetch("http://localhost:8080/api/login",{

        method:"POST",

        headers:{
            "Content-Type":"application/json"
        },

        body:JSON.stringify({

            email:email,
            password:password

        })

    })

    .then(response=>{

        if(!response.ok){
            throw new Error();
        }

        return response.json();

    })

    .then(user=>{


        localStorage.setItem(
            "currentLoginUser",
            user.name
        );


        alert(
          `${user.name}さん、ログインしました`
        );


        if(user.role==="STORE_MANAGER"){
            location.href="admin/index-admin.html";
        }else{
            location.href="user/index-user.html";
        }

    })

    .catch(()=>{

        const errorBox =
        document.getElementById(
            "login-error-msg"
        );

        errorBox.style.display="block";

    });

}