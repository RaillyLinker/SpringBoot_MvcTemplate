package com.raillylinker.springboot_mvc_template.custom_classes

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Semaphore

// (스레드 병합 객체)
/*
    사용 예시 :
    val threadMerger =
        ThreadMerger(
            3,
            onComplete = {
                screenDataSemaphoreMbr.release()
                onComplete()
            }
        )
    위와 같이 객체를 생성.
    threadMerger.threadComplete()
    위와 같이 각 스레드동작 완료시마다 호출하면 객체 생성시 설정한 갯수만큼 호출되면 onComplete 가 실행되고, 그 이상으로 호출하면 아무 반응 없음
 */
class ThreadMerger(
    private val numberOfThreadsBeingJoined: Int, // 합쳐지는 스레드 총개수
    private val onComplete: () -> Unit // 스레드 합류가 모두 끝나면 실행할 콜백 함수
) {
    // <멤버 변수 공간>
    // (스레드 풀)
    private val executorService: ExecutorService = Executors.newCachedThreadPool()
    private var threadAccessCount = 0
    private val threadAccessCountSemaphore = Semaphore(1)


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    // 한 스레드 작업이 종료 되었을 때 이를 실행
    fun threadComplete() {
        if (threadAccessCount < 0) { // 오버플로우 방지
            return
        }

        executorService.execute {
            threadAccessCountSemaphore.acquire()
            // 스레드 접근 카운트 +1
            ++threadAccessCount

            if (threadAccessCount != numberOfThreadsBeingJoined) {
                // 접근 카운트가 합류 총 개수를 넘었을 때 or 접근 카운트가 합류 총 개수에 미치지 못했을 때
                threadAccessCountSemaphore.release()
            } else { // 접근 카운트가 합류 총 개수에 다다랐을 때
                threadAccessCountSemaphore.release()
                onComplete()
            }
        }
    }
}