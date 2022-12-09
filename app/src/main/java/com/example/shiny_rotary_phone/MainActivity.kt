package com.example.shiny_rotary_phone

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shiny_rotary_phone.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.myAppbar)
        binding.myAppbar.showOverflowMenu()
        binding.myAppbar.showContextMenu()
        val key = "2cd8f552-3fa3-455d-b48f-1eb986db380c"
        val name = "julien.marcuse@mymail.champlain.edu"
        val api = ChitChatAPI(key, name)
        val messages = MessageRepository(api)
        adapter = ChatAdapter(messages) {runOnUiThread(it)}
        binding.chat.adapter = adapter
        binding.chat.layoutManager = LinearLayoutManager(this)
        Log.i("here", "children "+binding.myAppbar.menu.children.count())
        Log.i("here", "children item id "+binding.myAppbar.menu.children.first().itemId)
        Log.i("here", "children resource "+ R.id.sync_button)
        Log.i("here", "children "+binding.myAppbar.menu.children.toString())



        binding.newMsgButton.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            val input: EditText = EditText(this);
            input.hint = "Your message here"
            builder.setView(input)
            builder.setPositiveButton(
                "Send"
            ) { _, _ ->
                api.sendMessage(input.text.toString())
                adapter.cache.push()
                adapter.notifyDataSetChanged()
            }
            builder.setNegativeButton(
                "Cancel"
            ) { dialog, _ ->
                dialog.cancel() }
            builder.show()
        }
    }
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar_options, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        R.id.sync_button -> {
            adapter.cache.clearCache()
            adapter.notifyDataSetChanged()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}


class ChatMessage(val cache: MessageRepository, val view: LinearLayout): RecyclerView.ViewHolder(view) {

    fun reset() {
        view.removeAllViews()
        view.minimumHeight = 10
    }

    fun bind(message: Message) {
        view.removeAllViews()
        view.showDividers = LinearLayout.SHOW_DIVIDER_END
        view.orientation = LinearLayout.VERTICAL
        val header = LinearLayout(view.context).apply {orientation = LinearLayout.HORIZONTAL}
        val username = TextView(view.context).apply {text = message.sender.split("@")[0] + "  "}
        username.setTextColor(Color.LTGRAY)
        val date = TextView(view.context).apply {text = message.date}
        date.gravity = Gravity.END
        date.textAlignment = TextView.TEXT_ALIGNMENT_VIEW_END
        val msg = TextView(view.context).apply {text = message.content}
        val likes = LinearLayout(view.context).apply {orientation = LinearLayout.HORIZONTAL}
        val likeButton = Button(view.context).apply {text = "${message.likes} Likes"}
        val dislikeButton = Button(view.context).apply {text = "${message.dislikes} Dislikes"}
        likeButton.setOnClickListener {
            if (!message.liked) {
                message.liked = true
                message.likes += 1
                cache.api.likeMessage(message)
                likeButton.text = "${message.likes} Likes"
            }
        }
        dislikeButton.setOnClickListener {
            if (!message.disliked) {
                message.disliked = true
                message.dislikes += 1
                cache.api.dislikeMessage(message)
                dislikeButton.text = "${message.dislikes} Dislikes"
            }
        }
        header.addView(username)
        header.addView(date)
        likes.addView(likeButton)
        likes.addView(dislikeButton)
        view.addView(header)
        view.addView(msg)
        view.addView(likes)
    }

}

class ChatAdapter(val cache: MessageRepository, val runner: (() -> Unit) -> Unit): RecyclerView.Adapter<ChatMessage>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessage {
        return ChatMessage(cache, LinearLayout(parent.context))
    }

    override fun onBindViewHolder(holder: ChatMessage, position: Int) {
        holder.reset()
        cache.getMessage(position) {
            runner {holder.bind(it)}
        }
    }

    override fun getItemCount() = cache.cacheSize() + 10

}