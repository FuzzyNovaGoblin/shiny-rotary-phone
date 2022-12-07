package com.example.shiny_rotary_phone

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.net.HttpURLConnection
import java.net.URL


class ChitChatAPI(val key: String, val email: String, val context: Context) {

    val queue: RequestQueue

    init {
        queue = Volley.newRequestQueue(context)

    }
    fun retrieveMessages(skip: Int = 0, limit: Int = 20): List<Message> {
        // Instantiate the RequestQueue.
        val url = "https://www.google.com"

// Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                Log.i("here",  "Response is: ${response.substring(0, 500)}")
            },
            { Log.i("here","That didn't work!") }

        )

        queue.add(stringRequest)
        return emptyList()
    }

}