package com.raillylinker.springboot_mvc_template.custom_classes

import com.raillylinker.springboot_mvc_template.custom_objects.SseEmitterUtil
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.Semaphore
import kotlin.collections.ArrayList

// [SseEmitter 래핑 클래스]
data class SseEmitterWrapper(
    // (SSE Emitter 의 만료시간 Milli Sec)
    val sseEmitterTimeMs: Long
) {
    // (SSE Emitter 를 고유값과 함께 모아둔 맵)
    /*
        map key = EmitterId =
        "${emitterPublishSequence}_${LocalDateTime.now().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd-'T'-HH-mm-ss-SSSSSS-z"))}_${memberUid}"
        쉽게 말해 (발행시퀀스_현재시간_수신자멤버고유번호(비회원은 -1)) 으로,
        발행시퀀스, 현재시간 둘이 합쳐 emitter 고유성을 보장하고, 뒤에 붙는 정보들은 필터링을 위한 정보들
     */
    val emitterMap: HashMap<String, SseEmitter> = hashMapOf()

    // (발행 시퀀스)
    // emitter 고유값으로 사용되며, 유한한 값이지만, 현재 날짜와 같이 사용되므로 고유성을 보장함
    var emitterPublishSequence: Long = 0L

    // (Emitter 관련 세마포어)
    // emitterMap, emitterPublishSequence 를 조회, 수정시에는 꼭 세마포어로 뮤텍스 할 것.
    val emitterMapSemaphore: Semaphore = Semaphore(1)

    // (발행 이벤트 맵)
    /*
         map key = EmitterId
         map value = map(dateString, EventBuilder)
         발행한 모든 이벤트를 기록하는 맵이며, 키는 emitterMap 과 동일한 고유값을 사용.
         값의 map 은 이벤트 발행시간이과 SSE Event Builder 객체의 쌍으로 이루어짐
     */
    val emitterEventMap: HashMap<String, ArrayList<Pair<String, SseEmitter.SseEventBuilder>>> = hashMapOf()

    // (이벤트 관련 세마포어)
    // emitterEventMap 를 조회, 수정시에는 꼭 세마포어로 뮤텍스 할 것.
    val emitterEventMapSemaphore: Semaphore = Semaphore(1)


    // (SSE Emitter 객체 발행)
    // !! 주의 : 함수 사용시 꼭 이 클래스 멤버변수인 emitterMapSemaphore, emitterEventMapSemaphore 로 감쌀것. !!
    fun getSseEmitter(
        sseEmitterId: String,
        lastSseEventId: String? // 마지막으로 클라이언트가 수신했던 이벤트 아이디 (없으면 null)){}
    ): SseEmitter {
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
        SseEmitterUtil.sendSseEvent(
            sseEmitter,
            SseEmitterUtil.makeSseEventBuilder("system", null, "SSE Connected!")
        )

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

                        SseEmitterUtil.sendSseEvent(sseEmitter, event)
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
}