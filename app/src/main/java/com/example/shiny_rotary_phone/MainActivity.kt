package com.example.shiny_rotary_phone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.shiny_rotary_phone.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding = ActivityMainBinding.inflate(layoutInflater)
    }
}

class ChatMessage(view: LinearLayout): RecyclerView.ViewHolder(view) {

    fun bind(message: Message) {
        
    }

}

class ChatAdapter(val api: ChitChatAPI, val messages: TreeMap<Int, Message>): RecyclerView.Adapter<ChatMessage>() {
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