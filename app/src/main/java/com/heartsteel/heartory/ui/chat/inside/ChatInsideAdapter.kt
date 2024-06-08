package com.heartsteel.heartory.ui.chat.inside

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.example.healthcarecomp.base.BaseAdapter
import com.example.healthcarecomp.base.BaseItemViewHolder
import com.heartsteel.heartory.service.model.domain.Message
import com.heartsteel.heartory.databinding.ComponentMiaResponseBinding
import com.heartsteel.heartory.databinding.ComponentUserPromptBinding

class ChatInsideAdapter : BaseAdapter<Message, ChatInsideAdapter.ChatInsideViewHolder>() {

    private val LEFT = 0
    private val RIGHT = 1

    inner class ChatInsideViewHolder(binding: ViewBinding) :
        BaseItemViewHolder<Message, ViewBinding>(binding) {

        override fun bind(item: Message) {
            if (binding is ComponentMiaResponseBinding) {
                binding.tvMiaText.text = item.content
            } else if (binding is ComponentUserPromptBinding) {
                binding.tvUserPrompt.text = item.content
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatInsideViewHolder {
        if (viewType == LEFT) {
            return ChatInsideViewHolder(
                ComponentMiaResponseBinding.inflate(
                    LayoutInflater
                        .from(parent.context), parent, false
                )
            )
        } else {
            return ChatInsideViewHolder(
                ComponentUserPromptBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        }
    }

    override fun differCallBack(): DiffUtil.ItemCallback<Message> {
        return object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (differ.currentList.isNotEmpty() && differ.currentList[position].role == "user") {
            RIGHT
        } else {
            LEFT
        }
    }


}
