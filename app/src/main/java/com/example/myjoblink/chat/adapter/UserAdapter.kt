package com.example.myjoblink.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myjoblink.databinding.UserChatItemBinding
import com.example.myjoblink.model.UserData

class UserAdapter(
    private val list: MutableList<UserData>,
    val clickListener: OnUserClickListener
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(val userChatItemBinding: UserChatItemBinding) :
        RecyclerView.ViewHolder(userChatItemBinding.root) {
        fun setData(user: UserData, action: OnUserClickListener) {
            userChatItemBinding.apply {
                userName.text = user.name
            }
            userChatItemBinding.root.setOnClickListener {
                action.onUserClick(user, adapterPosition)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            UserChatItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = list[position]
        holder.setData(user, clickListener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnUserClickListener {
        fun onUserClick(user: UserData, position: Int)
    }

}