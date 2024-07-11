package com.heartsteel.heartory.ui.chat.inside

import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.example.healthcarecomp.base.BaseAdapter
import com.example.healthcarecomp.base.BaseItemViewHolder
import com.heartsteel.heartory.databinding.ComponentMiaLoadingBinding
import com.heartsteel.heartory.databinding.ComponentMiaResponseBinding
import com.heartsteel.heartory.databinding.ComponentUserPromptBinding
import com.heartsteel.heartory.service.model.domain.Message
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ChatInsideAdapter : BaseAdapter<Message, ChatInsideAdapter.ChatInsideViewHolder>() {

    private val LEFT = 0
    private val RIGHT = 1
    private val LEFT_LOADING = 2

    public val LEFT_ROLE = "assistant"
    public val RIGHT_ROLE = "user"
    public val LEFT_LOADING_ROLE = "left_loading"

    inner class ChatInsideViewHolder(binding: ViewBinding) :
        BaseItemViewHolder<Message, ViewBinding>(binding) {

        override fun bind(item: Message) {
            if (binding is ComponentMiaResponseBinding) {
                binding.tvMiaText.text = item.content
                binding.tvMiaText.autoLinkMask = Linkify.WEB_URLS
                binding.tvMiaText.movementMethod = LinkMovementMethod.getInstance()
            } else if (binding is ComponentUserPromptBinding) {
                binding.tvUserPrompt.text = item.content
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatInsideViewHolder {
        return when (viewType) {
            LEFT -> {
                ChatInsideViewHolder(
                    ComponentMiaResponseBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            RIGHT -> {
                ChatInsideViewHolder(
                    ComponentUserPromptBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            LEFT_LOADING -> {
                ChatInsideViewHolder(
                    ComponentMiaLoadingBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> {
                ChatInsideViewHolder(
                    ComponentUserPromptBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
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
        return if (differ.currentList.isNotEmpty() && differ.currentList[position].role == RIGHT_ROLE) {
            RIGHT
        } else if (differ.currentList.isNotEmpty() && differ.currentList[position].role == LEFT_ROLE) {
            LEFT
        } else if (differ.currentList.isNotEmpty() && differ.currentList[position].role == LEFT_LOADING_ROLE) {
            LEFT_LOADING
        } else {
            RIGHT
        }

    }


}
