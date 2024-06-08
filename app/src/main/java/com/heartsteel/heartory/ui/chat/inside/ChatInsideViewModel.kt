package com.heartsteel.heartory.ui.chat.inside

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.healthcarecomp.base.BaseViewModel
import com.google.gson.Gson
import com.heartsteel.heartory.common.util.Resource
import com.heartsteel.heartory.service.model.domain.Message
import com.heartsteel.heartory.service.model.response.ResponseObject
import com.heartsteel.heartory.service.repository.MessageRepository
import com.heartsteel.heartory.service.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatInsideViewModel @Inject constructor(
    override val userRepository: UserRepository,
    val messageRepository: MessageRepository
) : BaseViewModel(userRepository) {

    val messages = MutableLiveData<Resource<MutableList<Message>>>()
    val sendMessageState = MutableLiveData<Resource<Message>>()

    fun getMessages() {
        messages.value = Resource.Loading()
        viewModelScope.launch {
            val response = messageRepository.getMessages(0)
            if (response.isSuccessful) {
                messages.value = Resource.Success(response.body()?.data!!)
            } else {
                val errorBody = response.errorBody()
                val errorMsg = Gson().fromJson(errorBody?.string(), ResponseObject::class.java).message
                messages.value = Resource.Error(errorMsg)
            }
        }
    }

    fun sendMessage(content: String) {
        val message = Message(content = content, role = "user")
        viewModelScope.launch {
            val response = messageRepository.sendMessage(message)
            if (response.isSuccessful) {
                getMessages()
            } else {
                val errorBody = response.errorBody()
                val errorMsg = Gson().fromJson(errorBody?.string(), ResponseObject::class.java).message
                messages.value = Resource.Error(errorMsg)
            }
        }
    }
}