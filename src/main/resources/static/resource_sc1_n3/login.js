window.onload = function(){
    // 페이지가 완전히 로드 되었을 때 실행되는 코드 작성

    if (loggedIn) {
        // 이미 로그인된 상황 -> 뒤로가기
        history.back();
    }

    if (loginError) {
        alert("인증 정보가 일치하지 않습니다.");
        // 현재 URL을 /main/sc/v1/login 으로 변경하고, 실패 URL을 제거
        history.replaceState(null, null, '/main/sc/v1/login');
        history.back();
    } else if (expired) {
        alert("세션이 만료되었습니다.");
        history.replaceState(null, null, '/main/sc/v1/login');
    } else if (duplicated) {
        alert("이미 로그인 중인 회원입니다.");
        history.replaceState(null, null, '/main/sc/v1/login');
    } else if (changePassword) {
        alert("비밀번호가 변경되었습니다.");
        history.replaceState(null, null, '/main/sc/v1/login');
    }
}

// 입력값 검증 함수
function validateForm() {
    var accountIdField = document.getElementById('accountId');
    var accountId = accountIdField.value;
    var passwordField = document.getElementById('password');
    var password = passwordField.value;

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

    document.getElementById('username').value = 'accountId_' + accountId;

    return true;
}

    // 회원가입 페이지로 이동하는 함수
function goToSignup() {
    location.href = '/main/sc/v1/join';
}

    // 비밀번호 감추기/보이기 기능
function togglePasswordVisibility() {
    var passwordInput = document.getElementById('password');
    var toggleButton = document.getElementById('togglePassword');
    if (passwordInput.type === "password") {
        passwordInput.type = "text";
        toggleButton.textContent = "Hide";
    } else {
        passwordInput.type = "password";
        toggleButton.textContent = "Show";
    }
}
