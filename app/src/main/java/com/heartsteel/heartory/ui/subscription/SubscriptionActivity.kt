package com.heartsteel.heartory.ui.subscription

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.healthcarecomp.base.BaseActivity
import com.heartsteel.heartory.R
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
        binding.ivBack.setOnClickListener {
            finish()
        }

        val rvSubscriptionList = binding.rvSubscriptionList
        rvSubscriptionList.clipToPadding = false
        rvSubscriptionList.clipChildren = false
        rvSubscriptionList.offscreenPageLimit = 3
        rvSubscriptionList.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(40))
        compositePageTransformer.addTransformer(ViewPager2.PageTransformer() { page, position ->
            val r = 1 - Math.abs(position)
            page.scaleY = 0.85f + r * 0.15f
        })

        rvSubscriptionList.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.d("Subscription", "position $position")
                when (position) {
                    0 -> {
                        binding.llIndicator1.background = getDrawable(R.drawable.background_border_radius_subscription)
                        binding.llIndicator2.background = getDrawable(R.drawable.bg_button_disabled)
                    }
                    1 -> {
                        binding.llIndicator2.background = getDrawable(R.drawable.background_border_radius_subscription)
                        binding.llIndicator1.background = getDrawable(R.drawable.bg_button_disabled)
                    }
                }
            }
        })
        rvSubscriptionList.setPageTransformer(compositePageTransformer)


        subscriptionAdapter = SubscriptionRecyclerViewAdapter {
            Log.d("Subscription", "Buying id $it")
            subscriptionViewModel.createOrder(it.id)
        }
        rvSubscriptionList.apply {
            adapter = subscriptionAdapter
//            layoutManager = LinearLayoutManager(this@SubscriptionActivity, LinearLayoutManager.HORIZONTAL, false)
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
                        Toasty.success(this, "Please complete payment", Toasty.LENGTH_SHORT).show()
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