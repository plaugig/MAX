package com.example.max.ui.chatsList.item

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.max.R
import com.example.max.databinding.ChatsMainMenuBinding
import com.example.max.ui.chatsList.ChatListViewModel
import com.example.max.ui.common.ItemUserData

class ChatsListViewHolder(
    itemView: View,
    private val listener: ChatListViewModel
): RecyclerView.ViewHolder(itemView) {

    private val binding = ChatsMainMenuBinding.bind(itemView)

    fun bind(chat: ItemUserData){
        binding.name.text = chat.name
        binding.description.text = chat.lastMessage

        Glide.with(itemView)
            .load(chat.avatarUrl)
            .placeholder(R.drawable.lox)
            .error(R.drawable.lox)
            .into(binding.avatar)

        binding.root.setOnClickListener {
            listener.openChat(
                threadId = chat.threadId ?: chat.id,
                peerUserId = chat.id
            )
        }
    }
}
