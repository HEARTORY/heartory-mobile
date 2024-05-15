package com.heartsteel.heartory.ui.chat.inside

import androidx.lifecycle.MutableLiveData
import com.example.healthcarecomp.base.BaseViewModel
import com.heartsteel.heartory.common.util.Resource
import com.heartsteel.heartory.data.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatInsideViewModel @Inject constructor(

) : BaseViewModel() {
    val mockData = MutableList(10) { index ->
        Message(
            id = "id$index",
            content = "content$index",
            userId = "userId$index",
            isByUser = index % 2 == 0,
            timeStamp = System.currentTimeMillis(),
            sent = true,
            seen = true
        )
    }

    val messages = MutableLiveData<Resource<MutableList<Message>>>()

    fun getMessages() {
        messages.postValue(Resource.Success(mockData))
    }

}