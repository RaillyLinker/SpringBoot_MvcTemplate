window.onload = function(){
    // 페이지가 완전히 로드 되었을 때 실행되는 코드 작성

    if (complete) {
        alert("회원의 비밀번호가 변경되었습니다.");
        history.replaceState(null, null, '/main/sc/v1/member-password-change');
    } else if (memberNotFound) {
        alert("존재하지 않는 회원 고유번호입니다.");
        history.replaceState(null, null, '/main/sc/v1/member-password-change');
    }
}

// 입력값 검증 함수
function validateForm() {
    var memberUidField = document.getElementById('memberUid');
    var memberUid = memberUidField.value;
    var newPasswordField = document.getElementById('newPassword');
    var newPassword = newPasswordField.value;
    var confirmNewPasswordField = document.getElementById('confirmNewPassword');
    var confirmNewPassword = confirmNewPasswordField.value;

    if (memberUid === "") {
        alert("비밀번호를 변경할 회원의 고유번호를 입력하세요.");
        memberUidField.focus();
        return false;
    }

    if (newPassword === "") {
        alert("새 비밀번호를 입력하세요.");
        newPasswordField.focus();
        return false;
    }

    if (confirmNewPassword === "") {
        alert("새 비밀번호 확인을 입력하세요.");
        confirmNewPasswordField.focus();
        return false;
    }

    if (newPassword !== confirmNewPassword) {
        alert("새 비밀번호가 일치하지 않습니다.");
        confirmNewPasswordField.focus();
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