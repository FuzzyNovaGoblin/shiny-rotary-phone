package com.example.shiny_rotary_phone

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shiny_rotary_phone.databinding.ActivityMainBinding
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val key = "2cd8f552-3fa3-455d-b48f-1eb986db380c"
        val name = "julien.marcuse@mymail.champlain.edu"
        val api = ChitChatAPI(key, name, this)
        Log.i("blah", api.retrieveMessages().toString())
        val adapter = ChatAdapter(ChitChatAPI(key, name, this))
        binding.chat.adapter = adapter
        binding.chat.layoutManager = LinearLayoutManager(this)
    }
}

class ChatMessage(val view: LinearLayout): RecyclerView.ViewHolder(view) {

    fun reset() {
        view.removeAllViews()
    }

    fun bind(message: Message) {
        view.orientation = LinearLayout.VERTICAL
        val username = TextView(view.context).apply {text = message.sender; setTextColor(Color.LTGRAY)}
        val msg = TextView(view.context).apply {text = message.content}
        view.addView(username)
        view.addView(msg)
    }

}

class ChatAdapter(val api: ChitChatAPI): RecyclerView.Adapter<ChatMessage>() {

    val messages = HashMap<Int, Message>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessage {
        return ChatMessage(LinearLayout(parent.context))
    }

    fun getMessage(pos: Int, callback: (Message) -> Unit) {
        if (messages.containsKey(pos)) {
            callback(messages[pos]!!)
            return
        }
        api.retrieveMessages(pos).also {
            if (it.isEmpty()) {
                return@also
            }
            callback(it[0])
            it.forEachIndexed { index, entry -> messages[index + pos] = entry }
        }
    }

    fun clearCache() = messages.clear()

    override fun onBindViewHolder(holder: ChatMessage, position: Int) {
        holder.reset()
        getMessage(position, holder::bind)
    }

    override fun getItemCount() = 10000

}