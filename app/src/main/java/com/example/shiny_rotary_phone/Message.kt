package com.example.shiny_rotary_phone

data class Message(val id: String, val content: String, val sender: String, val date: String, var likes: Int, var dislikes: Int) {
}