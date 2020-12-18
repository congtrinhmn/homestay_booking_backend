package com.ctr.homestaybooking.shared

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import java.io.FileInputStream
import java.io.IOException


/**
 * Created by at-trinhnguyen2 on 2020/12/16
 */

object Uploader {
    @Throws(IOException::class)
    fun upload(fileName: String, fileInputStream: FileInputStream): String {
        val serviceAccount = Thread.currentThread().contextClassLoader.getResourceAsStream("ctr-homestaybooking-firebase-adminsdk-dcfg0-d9fd17cea8.json")
        val storage = StorageOptions.newBuilder()
                .setProjectId("ctr-homestaybooking")
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build().service
        val bucket = storage.get("ctr-homestaybooking.appspot.com").toBuilder().setVersioningEnabled(false).build().update()
        val blob = storage.create(BlobInfo.newBuilder(
                BlobId.of("ctr-homestaybooking.appspot.com", "calendars/$fileName")).build(),
                fileInputStream.readBytes(),
                Storage.BlobTargetOption.predefinedAcl(Storage.PredefinedAcl.PUBLIC_READ))
        return "https://www.googleapis.com/download/storage/v1/b/ctr-homestaybooking.appspot.com/o/calendars%2F$fileName?alt=media"
    }
}
