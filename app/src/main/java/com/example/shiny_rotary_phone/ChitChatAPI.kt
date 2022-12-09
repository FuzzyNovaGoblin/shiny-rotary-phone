package com.example.shiny_rotary_phone

import android.util.Log
import org.json.JSONObject
import java.io.DataOutputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection

fun sendRequest(dest: String): String {
    var contents = ""
    val thread = Thread {
        val url = URL(dest)
        val conn = url.openConnection() as HttpsURLConnection
        contents = String(conn.inputStream.readBytes())
        conn.disconnect()
    }
    thread.start()
    thread.join()
    return contents
}
fun postRequest(dest: String, contents : String){
    Log.i("here", "in post")
    val thread = Thread {
        val url = URL(dest)
        val conn = url.openConnection() as HttpsURLConnection
        conn.requestMethod = "POST";
        val output = DataOutputStream(conn.outputStream)
        output.writeBytes(contents)
        output.close()
        conn.disconnect()
    }
    thread.start()
    thread.join()
}

class ChitChatAPI(val key: String, val email: String) {

    fun retrieveMessages(skip: Int = 0, limit: Int = 20): List<Message> {
        // Instantiate the RequestQueue.
        val url = "https://www.stepoutnyc.com/chitchat?key=${key}&client=${email}&skip=${skip}&limit=${limit}"
        val contents = sendRequest(url)
        Log.i("blah", contents)
        val array = JSONObject(contents).getJSONArray("messages")
        val messages = mutableListOf<Message>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            val id = obj.getString("_id")
            val author = obj.getString("client")
            val likes = obj.getInt("likes")
            val dislikes = obj.getInt("dislikes")
            val message = obj.getString("message")
            val date = obj.getString("date")
            messages.add(Message(id, message, author, date, likes, dislikes))
        }
        return messages
    }

    fun likeMessage(message:Message) {
        val url ="https://www.stepoutnyc.com/chitchat/like/${message.id}?key=${key}&client=${email}"
        sendRequest(url)
    }
    fun dislikeMessage(message:Message) {
        val url ="https://www.stepoutnyc.com/chitchat/dislike/${message.id}?key=${key}&client=${email}"
        sendRequest(url)
    }

    fun sendMessage(message:String) {
        Log.i("here", "in send msg")
        val url ="https://www.stepoutnyc.com/chitchat?key=${key}&client=${email}"
        postRequest(url, message)
    }

}