package com.example.shiny_rotary_phone

import android.util.Log

class ChitChatAPI(val key: String, val email: String) {

    fun retrieveMessages(skip: Int = 0, limit: Int = 20): List<Message> {
        Log.i("blah", "calling retrieveMessages")
        return emptyList()
    }

}