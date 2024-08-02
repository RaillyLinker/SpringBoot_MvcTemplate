window.onload = function(){
    if (complete) {
        alert("런타임 설정이 수정되었습니다.");
        history.replaceState(null, null, '/main/sc/v1/runtime-config-editor');
        history.back();
    } else if (fail) {
        alert("런타임 설정 수정에 실패하였습니다.");
        history.replaceState(null, null, '/main/sc/v1/runtime-config-editor');
        history.back();
    }

    // Initialize CodeMirror editor
    const editor = CodeMirror.fromTextArea(document.getElementById('configEditor'), {
        lineNumbers: true,
        mode: 'application/json',
        theme: 'default'
    });

    // Set initial content from configJsonString
    editor.setValue(configJsonString);
}
