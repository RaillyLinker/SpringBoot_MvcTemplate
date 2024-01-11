package com.raillylinker.springboot_mvc_template.custom_objects

import com.lowagie.text.Image
import com.lowagie.text.pdf.BaseFont
import org.springframework.core.io.ClassPathResource
import org.w3c.dom.Element
import org.xhtmlrenderer.extend.ReplacedElement
import org.xhtmlrenderer.extend.ReplacedElementFactory
import org.xhtmlrenderer.extend.UserAgentCallback
import org.xhtmlrenderer.layout.LayoutContext
import org.xhtmlrenderer.pdf.ITextFSImage
import org.xhtmlrenderer.pdf.ITextImageElement
import org.xhtmlrenderer.pdf.ITextRenderer
import org.xhtmlrenderer.render.BlockBox
import org.xhtmlrenderer.simple.extend.FormSubmissionListener
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Path

// [HTML String 을 기반으로 PDF 파일을 생성하는 유틸]
// https://flyingsaucerproject.github.io/flyingsaucer/r8/guide/users-guide-R8.html
// XHTML 1.0(strict), CSS 2.1 (@page 의 size 는 가능) 를 사용하는 것을 엄격히 지켜야함.
object PdfGenerator {
    // (HTML String 을 PDF 로 변환)
    fun createPdfFileFromHtmlString(
        createPdfFileDirectoryPathString: String, // 생성된 PDF 파일을 저장할 위치(ex : "D:\ZZZ_DEV\SpringBoot\springboot_kotlinprojecttemplate\files\temp" 혹은 상대경로 사용 가능)
        createPdfFileNameString: String, // 확장자를 제외한 파일명 (ex : "sample")
        htmlString: String, // PDF 로 변환할 HTML String (ex : <!DOCTYPE html> <html> ....)
        // 주의사항 : HTML 내에서 폰트를 사용하고 싶다면 아래 리스트 변수에 resource 내의 폰트 파일 URI 를 추가하고 HTML 내에서 CSS 로 적용할것.
        // HTML 내부 태그의 CSS 명시를 안하거나 여기에 폰트 파일명을 명시하지 않으면 폰트 적용이 되지 않음.
        resourceFontFilePathList: List<String>? // HTML 내에 적용할 resource 내 폰트 파일 경로 리스트 (ex : "/static/fonts/NanumGothic.ttf")
    ): File {
        val fileDirectoryPathString =
            if (createPdfFileDirectoryPathString.last() == '/' || createPdfFileDirectoryPathString.last() == '\\') {
                createPdfFileDirectoryPathString.dropLast(1)
            } else {
                createPdfFileDirectoryPathString
            }

        val fileNameString = if (createPdfFileNameString.first() == '/' || createPdfFileNameString.first() == '\\') {
            createPdfFileNameString
        } else {
            "/$createPdfFileNameString"
        }

        // PDF 변환 객체
        val renderer = ITextRenderer()

        // PDF 변환기에 폰트 파일 적용
        if (resourceFontFilePathList != null) {
            for (fontFilePath in resourceFontFilePathList) {
                renderer.fontResolver.addFont(
                    // resources 아래에 있는 폰트 경로를 입력해준다
                    ClassPathResource(fontFilePath).url.toString(),
                    BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED
                )
            }
        }

        // HTML 내 img src 경로의 이미지 적용
        val replacedElementFactory = renderer.sharedContext.replacedElementFactory
        renderer.sharedContext.replacedElementFactory =
            object : ReplacedElementFactory {
                override fun createReplacedElement(
                    layoutContext: LayoutContext?,
                    blockBox: BlockBox,
                    userAgentCallback: UserAgentCallback?,
                    cssWidth: Int,
                    cssHeight: Int
                ): ReplacedElement? {
                    // HTML element 가 비어있으면 null 을 반환
                    val element = blockBox.element ?: return null

                    val nodeName = element.nodeName // HTML Tag 이름(ex : "img")
                    val srcPath =
                        element.getAttribute("src") // HTML 태그의 src 속성에 적힌 값(ex : "/images/c0_n5/html_to_pdf_sample.jpg")

                    if (nodeName == "img" && srcPath.startsWith("/images")) { // img 태그에, /images 경로를 참고하는 경우
                        // static/{srcPath} 의 이미지 파일 읽기
                        val fsImage = ITextFSImage(
                            Image.getInstance(
                                Files.readAllBytes(
                                    Path.of(
                                        ClassPathResource("static${srcPath}").uri
                                    )
                                )
                            )
                        )

                        // css의 높이, 너비가 설정되어있으면 적용
                        if ((cssWidth != -1) || (cssHeight != -1)) {
                            fsImage.scale(cssWidth, cssHeight)
                        }

                        return ITextImageElement(fsImage)
                    } else {
                        // 해당사항 없는 태그는 그대로 반환
                        return replacedElementFactory.createReplacedElement(
                            layoutContext,
                            blockBox,
                            userAgentCallback,
                            cssWidth,
                            cssHeight
                        )
                    }
                }

                override fun remove(e: Element?) {
                    replacedElementFactory.remove(e)
                }

                override fun setFormSubmissionListener(listener: FormSubmissionListener?) {
                    replacedElementFactory.setFormSubmissionListener(listener)
                }

                override fun reset() {
                    replacedElementFactory.reset()
                }
            }

        renderer.setDocumentFromString(htmlString) // HTML String 세팅
        renderer.layout() // PDF 데이터 생성

        // 경로가 없으면 만들기
        val directory = File(fileDirectoryPathString)
        if (!directory.exists()) {
            directory.mkdir()
        }

        renderer.createPDF(FileOutputStream("$fileDirectoryPathString$fileNameString")) // PDF 파일 생성

        return File("$fileDirectoryPathString$fileNameString")
    }
}