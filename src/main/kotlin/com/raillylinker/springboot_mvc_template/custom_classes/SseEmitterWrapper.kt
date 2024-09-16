package com.raillylinker.springboot_mvc_template.custom_classes

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.ArrayList

// [SseEmitter 래핑 클래스]
class SseEmitterWrapper {
    // (SSE Emitter 를 고유값과 함께 모아둔 맵)
    private val emitterMap: ConcurrentHashMap<String, SseEmitter> = ConcurrentHashMap()

    // (발행 이벤트 맵)
    /*
         map key = EmitterId
         map value = map(dateString, EventBuilder)
         발행한 모든 이벤트를 기록하는 맵이며, 키는 emitterMap 과 동일한 고유값을 사용.
         값의 map 은 이벤트 발행시간이과 SSE Event Builder 객체의 쌍으로 이루어짐
     */
    private val emitterEventMap: ConcurrentHashMap<String, ArrayList<Pair<String, SseEmitter.SseEventBuilder>>> =
        ConcurrentHashMap()

    // (발행 시퀀스)
    // emitter 고유성 보장을 위한 값으로 사용되며, 유한한 값이지만, 현재 날짜와 같이 사용됩니다.
    private var emitterPublishSequence: Long = 0L


    // (SSE Emitter 객체 발행)
    // !! 주의 : 함수 사용시 꼭 이 클래스 멤버변수인 emitterMapSemaphore, emitterEventMapSemaphore 로 감쌀것. !!
    fun getSseEmitter(
        // 멤버고유번호(비회원은 -1)
        memberUid: Long,
        // 마지막으로 클라이언트가 수신했던 이벤트 아이디 (없으면 null)){}
        lastSseEventId: String?,
        // (SSE Emitter 의 만료시간 Milli Sec)
        sseEmitterTimeMs: Long
    ): SseEmitter {
        // 수신 객체 아이디 (발행총개수_발행일_멤버고유번호(비회원은 -1))
        val sseEmitterId =
            "${emitterPublishSequence++}_${
                LocalDateTime.now().atZone(ZoneId.systemDefault())
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd-'T'-HH-mm-ss-SSSSSS-z"))
            }_$memberUid"

        // 수신 객체
        val sseEmitter = SseEmitter(sseEmitterTimeMs)
        sseEmitter.onTimeout { // 타임아웃 시 실행
            // 이후 바로 onCompletion 이 실행되고,다시 lastSseEventId 를 넣은 클라이언트 요청으로 현 API가 재실행됨
        }

        sseEmitter.onError { // 에러 발생시 실행.
            // 대표적으로 클라이언트가 연결을 끊었을 때 실행됨.
            // 이후 바로 onCompletion 이 실행되고, 함수는 재실행되지 않음.
        }

        sseEmitter.onCompletion { // sseEmitter 가 종료되었을 때 공통적, 최종적으로 실행

        }

        // 생성된 수신 객체를 저장
        emitterMap[sseEmitterId] = sseEmitter

        // 503 에러를 방지하기 위해, 처음 이미터 생성시엔 빈 메세지라도 발송해야함
        try {
            sseEmitter.send(
                SseEmitter
                    .event()
                    .name("system")
                    .data("SSE Connected!")
            )
        } catch (_: Exception) {
        }

        if (lastSseEventId != null) { // 첫번째 요청이 아님 (에러로 인해 lastSseEventId 다음의 이벤트를 못 받은 상황)
            val lastSseEventIdSplit = lastSseEventId.split("/")
            val lastEmitterId = lastSseEventIdSplit[0] // 지난번 발행된 emitter id

            emitterMap.remove(lastEmitterId)

            // 마지막으로 이벤트를 수신한 시간
            val lastEventDate = ZonedDateTime.parse(
                lastSseEventIdSplit[1],
                DateTimeFormatter.ofPattern("yyyy-MM-dd-'T'-HH-mm-ss-SSSSSS-z")
            )

            // lastSseEventId 로 식별하여 다음에 발송해야할 이벤트들을 전송하기
            // 쌓인 event 처리
            if (emitterEventMap.containsKey(lastEmitterId)) {
                // 지난 이벤트 리스트 가져오기
                val lastEventList = emitterEventMap[lastEmitterId]!!
                // 지난 이미터 정보를 이벤트 맵에서 제거
                emitterEventMap.remove(lastEmitterId)

                val newEventList = ArrayList<Pair<String, SseEmitter.SseEventBuilder>>()
                for (lastEvent in lastEventList) {
                    val eventDateString = lastEvent.first
                    val eventDate = ZonedDateTime.parse(
                        eventDateString,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd-'T'-HH-mm-ss-SSSSSS-z")
                    )
                    val event = lastEvent.second

                    val dateCompareResult: Int = eventDate.compareTo(lastEventDate)

                    if (dateCompareResult > 0) {
                        // eventDate 은 lastEventDate 이후 날짜
                        newEventList.add(lastEvent)

                        try {
                            sseEmitter.send(event)
                        } catch (_: Exception) {
                        }
                    }
                }

                emitterEventMap[sseEmitterId] = newEventList
            }

        }

        // 시간 지난 이미터, 이벤트 판별 및 제거
        val removeEmitterIdList = ArrayList<String>()
        for (emitter in emitterMap) {
            // 이미터 생성시간
            val emitterDate =
                ZonedDateTime.parse(
                    emitter.key.split("_")[1],
                    DateTimeFormatter.ofPattern("yyyy-MM-dd-'T'-HH-mm-ss-SSSSSS-z")
                )
            // 현재시간
            val nowDate = ZonedDateTime.now()

            // 이미터 생성시간으로부터 몇 ms 지났는지
            val diffMs = nowDate.toInstant().toEpochMilli() - emitterDate.toInstant().toEpochMilli()

            // 이미티 생성 시간이 타임아웃 시간(+1초) 을 초과했을 때
            if (diffMs > sseEmitterTimeMs + 1000) {
                // 삭제 목록에 포함
                removeEmitterIdList.add(emitter.key)
            }
        }

        // 삭제 목록에 있는 이미터와 이벤트 삭제
        for (removeEmitterId in removeEmitterIdList) {
            emitterMap.remove(removeEmitterId)
            emitterEventMap.remove(removeEmitterId)
        }

        return sseEmitter
    }

    fun broadcastEvent(
        eventName: String,
        eventMessage: String
    ) {
        for (emitter in emitterMap) { // 저장된 모든 emitter 에 발송 (필터링 하려면 emitter.key 에 저장된 정보로 필터링 가능)
            // 발송 시간
            val dateString = LocalDateTime.now().atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd-'T'-HH-mm-ss-SSSSSS-z"))

            // 이벤트 고유값 생성 (이미터고유값/발송시간)
            val eventId = "${emitter.key}/${dateString}"

            // 이벤트 빌더 생성
            val sseEventBuilder = SseEmitter
                .event()
                .id(eventId)
                .name(eventName)
                .data(eventMessage)

            // 이벤트 누락 방지 처리를 위하여 이벤트 빌더 기록
            if (emitterEventMap.containsKey(emitter.key)) {
                emitterEventMap[emitter.key]!!.add(Pair(dateString, sseEventBuilder))
            } else {
                emitterEventMap[emitter.key] = arrayListOf(Pair(dateString, sseEventBuilder))
            }

            // 이벤트 발송
            try {
                emitter.value.send(
                    sseEventBuilder
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}