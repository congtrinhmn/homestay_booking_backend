package com.ctr.homestaybooking.config

import com.ctr.homestaybooking.shared.log
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import java.io.IOException
import javax.annotation.PostConstruct


/**
 * Created by at-trinhnguyen2 on 2020/12/09
 */
@Service
class FCMInitializer {
    @Value("\${app.firebase-config}")
    private val firebaseConfigPath: String? = null

    @PostConstruct
    fun initialize() {
        try {
            val options: FirebaseOptions = FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(ClassPathResource(firebaseConfigPath!!).inputStream)).build()
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options)
                log.info { "Firebase application has been initialized" }
            }
        } catch (e: IOException) {
            log.info { e.message }
        }
    }
}
