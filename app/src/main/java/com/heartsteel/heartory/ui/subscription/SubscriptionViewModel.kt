package com.heartsteel.heartory.ui.subscription

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.healthcarecomp.base.BaseViewModel
import com.heartsteel.heartory.common.util.Resource
import com.heartsteel.heartory.service.model.domain.Subscription
import com.heartsteel.heartory.service.model.request.CreateOrderReq
import com.heartsteel.heartory.service.model.response.CreateOrderRes
import com.heartsteel.heartory.service.model.response.ResponseObject
import com.heartsteel.heartory.service.repository.PaymentRepository
import com.heartsteel.heartory.service.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Objects
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    override val userRepository: UserRepository,
    private val paymentRepository: PaymentRepository,
    private val hasInternetConnection: () -> Boolean
) : BaseViewModel(userRepository) {

    val orderState = MutableLiveData<Resource<ResponseObject<CreateOrderRes>>>()
    val subscriptionsState = MutableLiveData<Resource<ResponseObject<List<Subscription>>>>()

    fun createOrder(subscriptionId: Int) {
        orderState.postValue(Resource.Loading())

        if (!hasInternetConnection()) {
            orderState.postValue(Resource.Error("No internet connection"))
            return
        }

        viewModelScope.launch {
            val createOrderReq = CreateOrderReq(subscriptionId)
            try {
                val response = paymentRepository.createOrder(createOrderReq)
                if (response.isSuccessful) {
                    if (Objects.isNull(response.body())) {
                        orderState.postValue(Resource.Error("Order not found"))
                        return@launch
                    }
                    orderState.postValue(Resource.Success(response.body()!!))
                } else {
                    orderState.postValue(Resource.Error(response.message()))
                }

            } catch (e: Exception) {
                e.printStackTrace()
                orderState.postValue(Resource.Error(e.message ?: "An error occurred"))
            }
        }
    }

    fun getSubscription() {
        if (!hasInternetConnection()) {
            subscriptionsState.postValue(Resource.Error("No internet connection"))
            return
        }
        viewModelScope.launch {
            try {
                subscriptionsState.postValue(Resource.Loading())
                val response = paymentRepository.getSubscription()
                if (response.isSuccessful) {
                    if (Objects.isNull(response.body())) {
                        subscriptionsState.postValue(Resource.Error("Subscription not found"))
                        return@launch
                    }
                    subscriptionsState.postValue(Resource.Success(response.body()!!))
                } else {
                    subscriptionsState.postValue(Resource.Error(response.message()))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                subscriptionsState.postValue(Resource.Error(e.message ?: "An error occurred"))
            }
        }
    }


}