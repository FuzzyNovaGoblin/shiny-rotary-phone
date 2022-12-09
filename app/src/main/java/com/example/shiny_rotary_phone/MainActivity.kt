package com.example.shiny_rotary_phone

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shiny_rotary_phone.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val key = "2cd8f552-3fa3-455d-b48f-1eb986db380c"
        val name = "julien.marcuse@mymail.champlain.edu"
        val api = ChitChatAPI(key, name, this)
        val messages = MessageRepository(api)
        val adapter = ChatAdapter(messages) {runOnUiThread(it)}
        binding.chat.adapter = adapter
        binding.chat.layoutManager = LinearLayoutManager(this)
    }
}

class ChatMessage(val view: LinearLayout): RecyclerView.ViewHolder(view) {

    fun reset() {
        view.removeAllViews()
        view.minimumHeight = 10
    }

    fun bind(message: Message) {
        view.removeAllViews()
        view.orientation = LinearLayout.VERTICAL
        val username = TextView(view.context).apply {text = message.sender; setTextColor(Color.LTGRAY)}
        val msg = TextView(view.context).apply {text = message.content}
        val likes = LinearLayout(view.context).apply {orientation = LinearLayout.HORIZONTAL}
        val likeButton = Button(view.context).apply {text = "${message.likes} Likes"}
        val dislikeButton = Button(view.context).apply {text = "${message.dislikes} Dislikes"}
        likes.addView(likeButton)
        likes.addView(dislikeButton)
        view.addView(username)
        view.addView(msg)
        view.addView(likes)
    }

}

class ChatAdapter(val cache: MessageRepository, val runner: (() -> Unit) -> Unit): RecyclerView.Adapter<ChatMessage>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessage {
        Log.i("blah", viewType.toString())
        return ChatMessage(LinearLayout(parent.context))
    }

    override fun onBindViewHolder(holder: ChatMessage, position: Int) {
        holder.reset()
        cache.getMessage(position) {
            runner {holder.bind(it)}
        }
    }

    override fun getItemCount() = cache.cacheSize() + 10

}