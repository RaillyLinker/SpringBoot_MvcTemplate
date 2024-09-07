package com.raillylinker.springboot_mvc_template.custom_objects

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

// [SseEmitter 관련 유틸]
object SseEmitterUtil {
    // (SseEvent Builder 생성)
    fun makeSseEventBuilder(
        eventName: String?, // 발행 이벤트 그룹명
        eventId: String?, // 발행 이벤트 고유 아이디
        data: Any // 이벤트 전달 데이터 객체
    ): SseEmitter.SseEventBuilder {
        val emitterEvent = SseEmitter
            .event()
            .data(data)

        if (eventId != null) {
            emitterEvent
                .id(eventId)
        }

        if (eventName != null) {
            emitterEvent
                .name(eventName)
        }

        return emitterEvent
    }

    // (SseEvent Builder 를 Emitter 에 발송)
    fun sendSseEvent(
        emitter: SseEmitter, // SSE Emitter
        sseEventBuilder: SseEmitter.SseEventBuilder
    ) {
        try {
            emitter.send(
                sseEventBuilder
            )

        } catch (_: Exception) {

        }
    }
}