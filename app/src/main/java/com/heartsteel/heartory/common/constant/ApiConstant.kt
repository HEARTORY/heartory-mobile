package com.heartsteel.heartory.common.constant

import android.os.Build

class ApiConstant {
    companion object {
        private const val BASE_URL_EMULATOR = "http://10.0.2.2:8080/api/"
        private const val BASE_URL_DEVICE = "http://localhost:8080/api/"

        val BASE_URL: String
            get() = if (isEmulator()) BASE_URL_EMULATOR else BASE_URL_DEVICE

        private fun isEmulator(): Boolean {
            return Build.FINGERPRINT.startsWith("generic")
                    || Build.FINGERPRINT.startsWith("unknown")
                    || Build.MODEL.contains("google_sdk")
                    || Build.MODEL.contains("Emulator")
                    || Build.MODEL.contains("Android SDK built for x86")
                    || Build.MANUFACTURER.contains("Genymotion")
                    || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                    || "google_sdk" == Build.PRODUCT
        }
    }
}