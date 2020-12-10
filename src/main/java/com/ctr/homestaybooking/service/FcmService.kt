package com.ctr.homestaybooking.service

import com.ctr.homestaybooking.FirebaseMessage
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message
import org.springframework.stereotype.Service


/**
 * Created by at-trinhnguyen2 on
 */
@Service
class FcmService {
    fun pushNotification(firebaseMessage: FirebaseMessage): String? {
        val message: Message = Message.builder()
                .putData("title", firebaseMessage.title)
                .putData("body", firebaseMessage.body)
                .putData("extra", firebaseMessage.extra)
                .setToken(firebaseMessage.token)
                .build()
        var response: String? = null
        try {
            response = FirebaseMessaging.getInstance().send(message)
        } catch (e: FirebaseMessagingException) {
            e.printStackTrace()
        }
        return response
    }
}
