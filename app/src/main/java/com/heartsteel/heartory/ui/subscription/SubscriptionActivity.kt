package com.heartsteel.heartory.ui.subscription

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthcarecomp.base.BaseActivity
import com.heartsteel.heartory.common.util.Resource
import com.heartsteel.heartory.databinding.ActivitySubscriptionBinding
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty

@AndroidEntryPoint
class SubscriptionActivity : BaseActivity() {

    private val binding: ActivitySubscriptionBinding by lazy {
        ActivitySubscriptionBinding.inflate(layoutInflater)
    }

    private val subscriptionViewModel: SubscriptionViewModel by viewModels()

    private lateinit var subscriptionAdapter: SubscriptionRecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
        setupEvent()
        setObserver()

    }

    private fun setupView() {
        val rvSubscriptionList = binding.rvSubscriptionList
        subscriptionAdapter = SubscriptionRecyclerViewAdapter {
            Log.d("Subscription", "Buying id $it")
            subscriptionViewModel.createOrder(it.id)
        }
        rvSubscriptionList.apply {
            adapter = subscriptionAdapter
            layoutManager = LinearLayoutManager(this@SubscriptionActivity, LinearLayoutManager.HORIZONTAL, false)
        }
        subscriptionViewModel.getSubscription()
    }

    private fun setupEvent() {
    }

    private fun setObserver() {
        subscriptionViewModel.orderState.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    showLoading2()
                }

                is Resource.Success -> {
                    hideLoading2()
                    it.data?.data?.checkoutUrl.let {
                        Toasty.success(this, "Success", Toasty.LENGTH_SHORT).show()
                        //open browser
                        Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse(it)
                            startActivity(this)
                        }
                        finish()
                    }
                }

                is Resource.Error -> {
                    hideLoading2()
                    Toasty.error(this, it.message.toString(), Toasty.LENGTH_SHORT).show()
                }
            }
        }

        subscriptionViewModel.subscriptionsState.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    showLoading2()
                }

                is Resource.Success -> {
                    hideLoading2()
                    it.data?.data?.let {
                        subscriptionAdapter.setSubscriptionList(it)
                    }
                }

                is Resource.Error -> {
                    hideLoading2()
                    Toasty.error(this, it.message.toString(), Toasty.LENGTH_SHORT).show()
                }
            }
        }
    }
}