window.onload = function(){
    if (loggedIn) {
        // 이미 로그인된 상황 -> 뒤로가기
        history.back();
    }

    if (complete) {
        alert("회원가입이 완료되었습니다.");
        window.location.href = '/main/sc/v1/login';
    } else if (idExists) {
        alert("동일 아이디로 가입된 회원이 존재합니다.");
        history.replaceState(null, null, '/main/sc/v1/join');
        history.back();
    }
}

// 입력값 검증 함수
function validateForm() {
    var accountIdField = document.getElementById('accountId');
    var accountId = accountIdField.value;
    var passwordField = document.getElementById('password');
    var password = passwordField.value;
    var confirmPasswordField = document.getElementById('confirmPassword');
    var confirmPassword = confirmPasswordField.value;

    if (accountId === "") {
        alert("아이디를 입력하세요.");
        accountIdField.focus();
        return false;
    }

    if (password === "") {
        alert("비밀번호를 입력하세요.");
        passwordField.focus();
        return false;
    }

    if (confirmPassword === "") {
        alert("비밀번호 확인을 입력하세요.");
        confirmPasswordField.focus();
        return false;
    }

    if (password !== confirmPassword) {
        alert("비밀번호가 일치하지 않습니다.");
        confirmPasswordField.focus();
        return false;
    }

    return true;
}

// 비밀번호 감추기/보이기 기능
function togglePasswordVisibility(id) {
    var passwordInput = document.getElementById(id);
    var toggleButton = id === 'password' ? document.getElementById('togglePassword') : document.getElementById('toggleConfirmPassword');
    if (passwordInput.type === "password") {
        passwordInput.type = "text";
        toggleButton.textContent = "Hide";
    } else {
        passwordInput.type = "password";
        toggleButton.textContent = "Show";
    }
}