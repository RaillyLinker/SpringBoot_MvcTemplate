window.onload = function(){
    // 페이지가 완전히 로드 되었을 때 실행되는 코드 작성

    if (complete) {
        alert("회원 탈퇴 처리가 완료되었습니다.");
        history.replaceState(null, null, '/main/sc/v1/member-withdrawal');
    } else if (memberNotFound) {
        alert("존재하지 않는 회원 고유번호입니다.");
        history.replaceState(null, null, '/main/sc/v1/member-withdrawal');
    }
}

// 입력값 검증 함수
function validateForm() {
    var memberUidField = document.getElementById('memberUid');
    var memberUid = memberUidField.value;

    if (memberUid === "") {
        alert("회원 탈퇴 처리 할 회원의 고유번호를 입력하세요.");
        memberUidField.focus();
        return false;
    }

    return true;
}
