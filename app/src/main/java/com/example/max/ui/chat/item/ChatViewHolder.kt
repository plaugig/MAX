package com.example.max.ui.chat.item

import android.content.res.ColorStateList
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.max.R
import com.example.max.databinding.MessageBinding

class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    private val binding = MessageBinding.bind(itemView)

    fun bind(item: ItemMessageData){
       if (!item.imageUrl.isNullOrEmpty()){
           binding.messageImage.visibility = View.VISIBLE
           Glide.with(itemView.context)
               .load(item.imageUrl)
               .into(binding.messageImage)
       } else {
           binding.messageImage.visibility = View.GONE
       }

        if (!item.text.isNullOrEmpty()){
            binding.messageText.visibility = View.VISIBLE
            binding.messageText.text = item.text
        } else {
            binding.messageText.visibility = View.GONE
        }

        binding.messageTime.text = item.time

        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.root)

        if(item.isMine){

            binding.messageStatus.visibility = View.VISIBLE

            val statusIcon = if (item.isSent){
                R.drawable.ic_check_double
            } else {
                R.drawable.ic_status_pending
            }
            binding.messageStatus.setImageResource(statusIcon)

            constraintSet.setHorizontalBias(binding.messageText.id, 1.0f)

            binding.root.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(itemView.context, R.color.purple)
            )
        } else {
            constraintSet.setHorizontalBias(binding.messageText.id, 0.0f)

            binding.root.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(itemView.context, R.color.blue)
            )

            binding.messageStatus.visibility = View.GONE
        }
        constraintSet.applyTo(binding.root)
    }
}