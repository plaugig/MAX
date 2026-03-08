package com.example.max.ui.chatsList


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.max.databinding.ChatsMainMenuBinding
import com.example.max.ui.chatsList.item.ChatsListViewHolder
import com.example.max.ui.chatsList.item.ItemChatListData

class ChatsListAdapter(
    private val onChatClick: (ItemChatListData) -> Unit
) : ListAdapter<ItemChatListData, ChatsListViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatsListViewHolder {
        val binding = ChatsMainMenuBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ChatsListViewHolder(binding.root, onChatClick)

    }

    override fun onBindViewHolder(
        holder: ChatsListViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ItemChatListData>() {
        override fun areItemsTheSame(
            oldItem: ItemChatListData,
            newItem: ItemChatListData
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: ItemChatListData,
            newItem: ItemChatListData
        ): Boolean = oldItem == newItem

    }
}