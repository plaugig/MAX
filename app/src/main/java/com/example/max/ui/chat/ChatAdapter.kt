package com.example.max.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.max.databinding.MessageBinding
import com.example.max.ui.chat.item.ChatViewHolder
import com.example.max.ui.common.ItemMessageData

class ChatAdapter(): ListAdapter<ItemMessageData, ChatViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatViewHolder {
       val binding = MessageBinding.inflate(
           LayoutInflater.from(parent.context),
           parent,
           false
       )
        return ChatViewHolder(binding.root)
    }

    override fun onBindViewHolder(
        holder: ChatViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

   companion object DiffCallback : DiffUtil.ItemCallback<ItemMessageData>(){
       override fun areItemsTheSame(oldItem: ItemMessageData, newItem: ItemMessageData): Boolean {
           return oldItem.id == newItem.id
       }

       override fun areContentsTheSame(oldItem: ItemMessageData, newItem: ItemMessageData): Boolean {
           return oldItem == newItem
       }
   }

}