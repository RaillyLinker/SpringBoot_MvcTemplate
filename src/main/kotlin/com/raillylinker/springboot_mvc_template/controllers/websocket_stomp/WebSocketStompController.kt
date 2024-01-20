package com.raillylinker.springboot_mvc_template.controllers.websocket_stomp

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller

// [WebSocket STOMP 컨트롤러]
// api1 은 samples/html_file_sample/websocket-stomp.html 파일로 테스트 가능
@Controller
class WebSocketStompController(
    private val service: WebSocketStompService
) {
    // 메세지 함수 호출 경로 (WebSocketStompConfig 의 setApplicationDestinationPrefixes 설정과 합쳐서 호출 : /app/hello)
    @MessageMapping("/test")
    // 이 함수의 리턴값 반환 위치
    @SendTo("/topic")
    fun api1(inputVo: Api1InputVo): TopicVo {
        return service.api1(inputVo)
    }

    data class Api1InputVo(
        @JsonProperty("chat")
        val chat: String
    )


    // ---------------------------------------------------------------------------------------------
    // <채팅 데이터 VO 선언 공간>
    data class TopicVo(
        @JsonProperty("content")
        val content: String
    )
}