window.onload = function(){
    if (complete) {
        alert("아이디가 변경되었습니다.");
        history.replaceState(null, null, '/main/sc/v1/change-id');
    } else if (idExists) {
        alert("동일 아이디로 가입된 회원이 존재합니다.");
        history.replaceState(null, null, '/main/sc/v1/change-id');
    }
}

// 입력값 검증 함수
function validateForm() {
    var accountIdField = document.getElementById('accountId');
    var accountId = accountIdField.value;

    if (accountId === "") {
        alert("아이디를 입력하세요.");
        accountIdField.focus();
        return false;
    }

    return true;
}
