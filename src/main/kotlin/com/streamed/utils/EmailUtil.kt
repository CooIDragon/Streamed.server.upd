package com.streamed.utils

import java.util.*
import javax.mail.*
import javax.mail.internet.*

object EmailUtil {
    private val emailUsername = System.getenv("EMAIL_USERNAME")
    private val emailPassword = System.getenv("EMAIL_PASSWORD")

    private val session: Session by lazy {
        val properties = Properties().apply {
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")
            put("mail.smtp.host", "smtp.yandex.com")
            put("mail.smtp.port", "587")
        }

        Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication() = PasswordAuthentication(emailUsername, emailPassword)
        })
    }

    fun sendPasswordResetEmail(to: String, token: String) {
        try {
            val message = MimeMessage(session).apply {
                setFrom(InternetAddress(emailUsername))
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(to))
                subject = "Password Reset Request"
                setText("Your password reset token is: $token")
            }

            Transport.send(message)
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
    }
}
