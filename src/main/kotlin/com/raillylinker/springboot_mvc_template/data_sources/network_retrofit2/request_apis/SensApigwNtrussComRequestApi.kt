package com.raillylinker.springboot_mvc_template.data_sources.network_retrofit2.request_apis

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.*

// (한 주소에 대한 API 요청명세)
// 사용법은 아래 기본 사용 샘플을 참고하여 추상함수를 작성하여 사용
interface SensApigwNtrussComRequestApi {
    // (Naver SMS 발송)
    // https://api.ncloud-docs.com/docs/ai-application-service-sens-smsv2
    @POST("sms/v2/services/{naverSmsServiceId}/messages")
    @Headers("Content-Type: application/json")
    fun postSmsV2ServicesNaverSmsServiceIdMessages(
        // 프로젝트 등록 시 발급받은 서비스 아이디
        @Path("naverSmsServiceId") naverSmsServiceId: String,
        // 1970년 1월 1일 00:00:00 협정 세계시(UTC)부터의 경과 시간을 밀리초(Millisecond)로 나타냄
        // API Gateway 서버와 시간 차가 5분 이상 나는 경우 유효하지 않은 요청으로 간주
        @Header("x-ncp-apigw-timestamp") xNcpApigwTimestamp: String,
        // 포탈 또는 Sub Account에서 발급받은 Access Key ID
        @Header("x-ncp-iam-access-key") xNcpIamAccessKey: String,
        // 위 예제의 Body를 Access Key Id와 맵핑되는 SecretKey로 암호화한 서명, HMAC 암호화 알고리즘은 HmacSHA256 사용
        @Header("x-ncp-apigw-signature-v2") xNcpApigwSignatureV2: String,
        @Body inputVo: PostSmsV2ServicesNaverSmsServiceIdMessagesInputVO
    ): Call<PostSmsV2ServicesNaverSmsServiceIdMessagesOutputVO?>

    data class PostSmsV2ServicesNaverSmsServiceIdMessagesInputVO(
        // SMS Type, SMS, LMS, MMS (소문자 가능)
        @SerializedName("type")
        @Expose
        var type: String,
        // 메시지 Type, COMM: 일반메시지 default, AD: 광고메시지
        @SerializedName("contentType")
        @Expose
        var contentType: String?,
        // 국가 번호, SENS에서 제공하는 국가로의 발송만 가능, default: 82
        // https://guide.ncloud-docs.com/docs/sens-smspolicy
        @SerializedName("countryCode")
        @Expose
        var countryCode: String?,
        // 발신번호, 사전 등록된 발신번호만 사용 가능
        @SerializedName("from")
        @Expose
        var from: String,
        // 기본 메시지 제목, LMS, MMS에서만 사용 가능(최대 40byte)
        @SerializedName("subject")
        @Expose
        var subject: String?,
        // 기본 메시지 내용, SMS: 최대 90byte, LMS, MMS: 최대 2000byte
        @SerializedName("content")
        @Expose
        var content: String,
        // 메시지 정보, 최대 100개
        @SerializedName("messages")
        @Expose
        var messages: List<MessageVo>,
        // 파일 전송
        @SerializedName("files")
        @Expose
        var files: List<FileVo>?,
        // 메시지 발송 예약 일시 (yyyy-MM-dd HH:mm)
        @SerializedName("reserveTime")
        @Expose
        var reserveTime: String?,
        // 예약 일시 타임존 (기본: Asia/Seoul) https://en.wikipedia.org/wiki/List_of_tz_database_time_zones
        @SerializedName("reserveTimeZone")
        @Expose
        var reserveTimeZone: String?
    ) {
        data class MessageVo(
            // 수신번호, 붙임표 ( - )를 제외한 숫자만 입력 가능
            @SerializedName("to")
            @Expose
            var to: String,
            // 개별 메시지 제목, LMS, MMS에서만 사용 가능(최대 40byte)
            @SerializedName("subject")
            @Expose
            var subject: String?,
            // 개별 메시지 내용, SMS: 최대 90byte, LMS, MMS: 최대 2000byte
            @SerializedName("content")
            @Expose
            var content: String?
        )

        data class FileVo(
            // 파일 아이디, MMS 에서만 사용 가능, 파일 전송 api 사용 후 받아온 fileId 를 입력
            @SerializedName("fileId")
            @Expose
            var fileId: String,
        )
    }

    data class PostSmsV2ServicesNaverSmsServiceIdMessagesOutputVO(
        // 요청 아이디
        @SerializedName("requestId")
        @Expose
        var requestId: String,
        // 요청 시간 yyyy-MM-dd'T'HH:mm:ss.SSS
        @SerializedName("requestTime")
        @Expose
        var requestTime: String,
        // 요청 상태 코드, 202: 성공, 그 외: 실패, HTTP Status 규격을 따름
        @SerializedName("statusCode")
        @Expose
        var statusCode: String,
        // 요청 상태명, success: 성공, fail: 실패
        @SerializedName("statusName")
        @Expose
        var statusName: String
    )
}