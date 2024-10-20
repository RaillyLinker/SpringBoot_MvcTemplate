package com.raillylinker.module_api_sample.services

import com.raillylinker.module_api_sample.controllers.C5Service1TkV1MediaResourceProcessController
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.io.Resource
import org.springframework.http.ResponseEntity

interface C5Service1TkV1MediaResourceProcessService {
    // <공개 메소드 공간>
    fun api1ResizeImage(
        inputVo: C5Service1TkV1MediaResourceProcessController.Api1ResizeImageInputVo,
        httpServletResponse: HttpServletResponse
    ): ResponseEntity<Resource>?


    ////
    fun api2SplitAnimatedGif(
        httpServletResponse: HttpServletResponse
    )


    ////
    fun api3MergeImagesToAnimatedGif(httpServletResponse: HttpServletResponse)


    ////
    fun api4ResizeGifImage(
        inputVo: C5Service1TkV1MediaResourceProcessController.Api4ResizeGifImageInputVo,
        httpServletResponse: HttpServletResponse
    ): ResponseEntity<Resource>?


    ////
    fun api5CreateSignature(
        httpServletResponse: HttpServletResponse,
        inputVo: C5Service1TkV1MediaResourceProcessController.Api5CreateSignatureInputVo
    )
}