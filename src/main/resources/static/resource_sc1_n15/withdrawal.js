window.onload = function(){
    // 페이지가 완전히 로드 되었을 때 실행되는 코드 작성
}

// 입력값 검증 함수
function validateForm() {
    var checkbox = document.getElementById('agreeCheckbox');
    if (!checkbox.checked) {
        alert('동의합니다를 체크해주세요.');
        return false;
    }
    return true;
}