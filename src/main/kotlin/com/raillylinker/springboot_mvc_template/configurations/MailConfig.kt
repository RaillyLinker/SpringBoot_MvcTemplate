package com.raillylinker.springboot_mvc_template.configurations

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.*

@Configuration
class MailConfig(
    @Value("\${customConfig.smtp.host}")
    var host: String,
    @Value("\${customConfig.smtp.port}")
    var port: Int,
    @Value("\${customConfig.smtp.senderName}")
    var senderName: String,
    @Value("\${customConfig.smtp.senderPassword}")
    var senderPassword: String,
    @Value("\${customConfig.smtp.timeOutMillis}")
    var timeOutMillis: String
) {
    @Bean
    fun javaMailSender(): JavaMailSenderImpl {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = host
        mailSender.port = port
        mailSender.username = senderName
        mailSender.password = senderPassword

        val props: Properties = mailSender.javaMailProperties
        props["mail.smtp.connectiontimeout"] = timeOutMillis
        props["mail.smtp.timeout"] = timeOutMillis
        props["mail.smtp.writetimeout"] = timeOutMillis

        // SMTP 종류별 설정
        if (port == 587) {
            // port 587 일 경우
            props["mail.transport.protocol"] = "smtp"
            props["mail.smtp.auth"] = "true"
            props["mail.smtp.starttls.enable"] = "true"
            props["mail.debug"] = "true"
        } else if (port == 465) {
            // port 465 일 경우
            props["mail.smtp.ssl.enable"] = "true"  // SSL 활성화
            props["mail.smtp.auth"] = "true"  // SMTP 인증 활성화
            props["mail.smtp.connectiontimeout"] = "10000"
            props["mail.smtp.timeout"] = "10000"
            props["mail.smtp.writetimeout"] = "10000"
            props["mail.smtp.ssl.checkserveridentity"] = "false"
            props["mail.smtp.ssl.trust"] = "*"
        }

        return mailSender
    }
}

