package com.example.shiny_rotary_phone

class ChitChatAPI(val key: String, val email: String) {

    fun retrieveMessages(skip: Int = 0, limit: Int = 20): List<Message> {
        return emptyList()
    }

}