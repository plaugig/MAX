package com.example.max.ui.chat.item

import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.max.R
import com.example.max.databinding.MessageBinding
import com.example.max.ui.common.ItemMessageData

class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    private val binding = MessageBinding.bind(itemView)

    fun bind(item: ItemMessageData) {
        if (!item.imageUrl.isNullOrBlank()) {
            binding.messageImage.visibility = View.VISIBLE
            Glide.with(itemView)
                .load(item.imageUrl)
                .into(binding.messageImage)
        } else {
            binding.messageImage.visibility = View.GONE
        }

        if (item.text.isNotBlank()) {
            binding.messageText.visibility = View.VISIBLE
            binding.messageText.text = item.text
        } else {
            binding.messageText.visibility = View.GONE
        }

        binding.messageTime.text = item.time

        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.root)

        constraintSet.clear(binding.messageCard.id, ConstraintSet.START)
        constraintSet.clear(binding.messageCard.id, ConstraintSet.END)
        constraintSet.connect(
            binding.messageCard.id,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START
        )
        constraintSet.connect(
            binding.messageCard.id,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END
        )

        if (item.isMine) {

            constraintSet.setHorizontalBias(binding.messageCard.id, 1.0f)

            binding.messageStatus.visibility = View.VISIBLE

            val statusIcon = if (item.isSent) {
                R.drawable.ic_check_double
            } else {
                R.drawable.ic_status_pending
            }
            binding.messageStatus.setImageResource(statusIcon)

            binding.messageCard.setCardBackgroundColor(
                ContextCompat.getColor(itemView.context, R.color.purple)
            )
        } else {
            constraintSet.setHorizontalBias(binding.messageCard.id, 0.0f)

            binding.messageStatus.visibility = View.GONE

            binding.messageCard.setCardBackgroundColor(
                ContextCompat.getColor(itemView.context, R.color.blue)
            )
        }
        constraintSet.applyTo(binding.root)
    }
}
