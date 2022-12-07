package com.example.shiny_rotary_phone

import java.util.TreeMap

class MessageRepository(val api: ChitChatAPI) {

    var thread: Thread? = null
    val queue = TreeMap<Int, () -> Unit>()
    val cache = HashMap<Int, Message>()

    init {
        thread = Thread {
            while (true) {
                if (queue.isEmpty()) {
                    continue
                }
                val key = queue.firstKey()
                queue.remove(key)!!()
            }
        }
        thread?.start()
    }

    fun clearCache() = cache.clear()

    private fun doAsync(pos: Int, callback: () -> Unit) {
        queue[pos] = callback
    }

    fun getMessage(pos: Int, callback: (Message) -> Unit) {
        cache[pos]?.let {
            callback(it)
            return
        }
        doAsync(pos) {
            cache[pos]?.let {
                callback(it)
                return@doAsync
            }
            api.retrieveMessages(pos, 100).forEachIndexed {index, elem -> cache[pos + index] = elem}
            cache[pos]?.let(callback)
        }
    }

    fun cacheSize() = cache.size

}