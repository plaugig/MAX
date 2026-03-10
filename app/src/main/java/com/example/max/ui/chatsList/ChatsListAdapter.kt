package com.example.max.ui.chatsList


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.max.databinding.ChatsMainMenuBinding
import com.example.max.ui.chatsList.item.ChatsListViewHolder
import com.example.max.ui.item.ItemUserData

class ChatsListAdapter(
    private val onChatClick: (ItemUserData) -> Unit
) : ListAdapter<ItemUserData, ChatsListViewHolder>(DiffCallback) {

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

    companion object DiffCallback : DiffUtil.ItemCallback<ItemUserData>() {
        override fun areItemsTheSame(
            oldItem: ItemUserData,
            newItem: ItemUserData
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: ItemUserData,
            newItem: ItemUserData
        ): Boolean = oldItem == newItem

    }
}