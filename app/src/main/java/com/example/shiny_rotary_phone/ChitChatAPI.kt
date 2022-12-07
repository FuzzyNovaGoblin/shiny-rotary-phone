package com.example.shiny_rotary_phone

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

fun sendRequest(dest: String, callback: (String) -> Unit) {
    Thread {
        val url = URL(dest)
        val conn = url.openConnection() as HttpsURLConnection
        val contents = String(conn.inputStream.readBytes())
        conn.disconnect()
        callback(contents)
    }.start()
}

class ChitChatAPI(val key: String, val email: String, val context: Context) {

    fun retrieveMessages(skip: Int = 0, limit: Int = 20): List<Message> {
        // Instantiate the RequestQueue.
        val url = "https://www.google.com"

        
        return emptyList()
    }

}