package com.example.myjoblink.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myjoblink.R
import com.example.myjoblink.chat.model.Message
import com.example.myjoblink.databinding.ItemMessageBinding

class ChatAdapter(
    private val messages: List<Message>,
    private val currentUserId: String
) : RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    inner class MessageViewHolder(private val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            binding.messageText.text = message.text

            val layoutParams = binding.root.layoutParams as ViewGroup.MarginLayoutParams

            if (message.senderId == currentUserId) {
                // Sender message
                binding.messageText.setBackgroundResource(R.drawable.sender_bg)
                layoutParams.marginStart = 100
                layoutParams.marginEnd = 0
            } else {
                // Receiver message
                binding.messageText.setBackgroundResource(R.drawable.receiver_bg)
                layoutParams.marginStart = 0
                layoutParams.marginEnd = 100
            }

            binding.root.layoutParams = layoutParams
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.bind(message)
    }

    override fun getItemCount(): Int {
        return messages.size
    }
}