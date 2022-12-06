package com.example.shiny_rotary_phone

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shiny_rotary_phone.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val key = "2cd8f552-3fa3-455d-b48f-1eb986db380c"
        val name = "julien.marcuse@mymail.champlain.edu"
        binding.chat.adapter = ChatAdapter(ChitChatAPI(key, name))
    }
}

class ChatMessage(val view: LinearLayout): RecyclerView.ViewHolder(view) {

    fun bind(message: Message) {
        view.removeAllViews()
        view.orientation = LinearLayout.VERTICAL
        val username = TextView(view.context).apply {text = message.sender; setTextColor(Color.LTGRAY)}
        val msg = TextView(view.context).apply {text = message.content}
        view.addView(username)
        view.addView(msg)
    }

}

class ChatAdapter(val api: ChitChatAPI): RecyclerView.Adapter<ChatMessage>() {

    val messages = TreeMap<Int, Message>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessage {
        return ChatMessage(LinearLayout(parent.context))
    }

    fun getMessage(pos: Int): Message {
        if (messages.containsKey(pos)) {
            return messages[pos]!!
        }
        val offset = if (messages.isEmpty()) 0 else messages.firstKey()
        api.retrieveMessages(pos - offset).forEachIndexed { index, item ->
            messages.put(index + offset, item)
        }
        return messages[pos]!!
    }

    override fun onBindViewHolder(holder: ChatMessage, position: Int) {
        holder.bind(getMessage(position))
    }

    override fun getItemCount() = messages.count()

}