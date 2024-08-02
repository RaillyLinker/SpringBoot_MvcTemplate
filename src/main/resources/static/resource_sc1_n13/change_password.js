window.onload = function(){
    if (passwordNotMatch) {
        alert("기존 비밀번호가 일치하지 않습니다.");
        history.replaceState(null, null, '/main/sc/v1/change-password');
    }
}

// 입력값 검증 함수
function validateForm() {
    var oldPasswordField = document.getElementById('oldPassword');
    var oldPassword = oldPasswordField.value;
    var newPasswordField = document.getElementById('newPassword');
    var newPassword = newPasswordField.value;
    var confirmNewPasswordField = document.getElementById('confirmNewPassword');
    var confirmNewPassword = confirmNewPasswordField.value;

    if (oldPassword === "") {
        alert("기존 비밀번호를 입력하세요.");
        oldPasswordField.focus();
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