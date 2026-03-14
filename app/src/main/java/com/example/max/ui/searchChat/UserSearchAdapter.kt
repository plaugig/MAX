package com.example.max.ui.searchChat


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.max.databinding.UserSearchBinding
import com.example.max.ui.item.ItemUserData
import com.example.max.ui.searchChat.item.UserSearchViewHolder

class UserSearchAdapter(
    private val onUserClick: (ItemUserData) -> Unit
): ListAdapter<ItemUserData, UserSearchViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserSearchViewHolder {
       val binding = UserSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
           return UserSearchViewHolder(binding.root, onUserClick)
    }

    override fun onBindViewHolder(
        holder: UserSearchViewHolder,
        position: Int
    ) {
       holder.bind(getItem(position))
    }
    companion object DiffCallback : DiffUtil.ItemCallback<ItemUserData>() {
        override fun areItemsTheSame(oldItem: ItemUserData, newItem: ItemUserData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ItemUserData, newItem: ItemUserData): Boolean {
           return oldItem == newItem
        }
    }


}