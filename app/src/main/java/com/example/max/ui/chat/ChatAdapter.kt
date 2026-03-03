package com.example.max.ui.chat

import androidx.recyclerview.widget.ListAdapter
import com.example.max.ui.item.ItemData

class ChatAdapter: ListAdapter<ItemData, ChatAdapter.ChatViewHolder>(DiffCalback) {
}