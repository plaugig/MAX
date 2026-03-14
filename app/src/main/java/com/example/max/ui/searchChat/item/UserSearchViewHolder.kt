package com.example.max.ui.searchChat.item

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.max.R
import com.example.max.databinding.UserSearchBinding
import com.example.max.ui.item.ItemUserData
import com.example.max.ui.searchChat.SearchUiActionListener

class UserSearchViewHolder(
    itemView: View,
    private val listener: SearchUiActionListener
): RecyclerView.ViewHolder(itemView) {

    private val binding = UserSearchBinding.bind(itemView)

    fun bind(user: ItemUserData){
        binding.tvUserName.text = user.name

        Glide.with(itemView.context)
            .load(user.avatarUrl)
            .placeholder(R.drawable.lox)
            .error(R.drawable.lox)
            .circleCrop()
            .into(binding.ivUserAvatar)

        binding.root.setOnClickListener {
            listener.openChat(user)
        }
    }
}