package com.raillylinker.springboot_mvc_template.data_sources.network_retrofit2.request_apis

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.*

// (한 주소에 대한 API 요청명세)
// 사용법은 아래 기본 사용 샘플을 참고하여 추상함수를 작성하여 사용
interface SensApigwNtrussComRequestApi {
    // (Naver SMS 발송)
    @POST("sms/v2/services/{naverSmsServiceId}/messages")
    @Headers("Content-Type: application/json")
    fun postSmsV2ServicesNaverSmsServiceIdMessages(
        @Path("naverSmsServiceId") naverSmsServiceId: String,
        @Header("x-ncp-apigw-timestamp") xNcpApigwTimestamp: String,
        @Header("x-ncp-iam-access-key") xNcpIamAccessKey: String,
        @Header("x-ncp-apigw-signature-v2") xNcpApigwSignatureV2: String,
        @Body inputVo: PostSmsV2ServicesNaverSmsServiceIdMessagesInputVO
    ): Call<Unit?>

    data class PostSmsV2ServicesNaverSmsServiceIdMessagesInputVO(
        @SerializedName("type")
        @Expose
        var type: String,
        @SerializedName("contentType")
        @Expose
        var contentType: String,
        @SerializedName("countryCode")
        @Expose
        var countryCode: String,
        @SerializedName("from")
        @Expose
        var from: String,
        @SerializedName("content")
        @Expose
        var content: String,
        @SerializedName("messages")
        @Expose
        var messages: List<MessageVo>
    ) {
        data class MessageVo(
            @SerializedName("to")
            @Expose
            var to: String,
            @SerializedName("content")
            @Expose
            var content: String
        )
    }
}