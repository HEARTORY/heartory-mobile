package com.heartsteel.heartory.ui.subscription

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.heartsteel.heartory.R
import com.heartsteel.heartory.service.model.domain.Subscription

class SubscriptionRecyclerViewAdapter(private val itemClickListener: (Subscription) -> Unit) :
    RecyclerView.Adapter<SubscriptionRecyclerViewAdapter.ViewHolder>() {

    private var subscriptionList: List<Subscription> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.component_subscription, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(subscriptionList[position])
    }

    override fun getItemCount(): Int {
        return subscriptionList.size
    }

    fun setSubscriptionList(subscriptionList: List<Subscription>) {
        this.subscriptionList = subscriptionList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val subscriptionName: TextView = itemView.findViewById(R.id.tvSubscriptionName)
        private val subscriptionPrice: TextView = itemView.findViewById(R.id.tvSubscriptionPrice)
        private val subscriptionDuration: TextView = itemView.findViewById(R.id.tvSubscriptionDays)
        private val btnChoose: Button = itemView.findViewById(R.id.btnChoose)

        fun bind(subscription: Subscription) {
            subscriptionName.text = subscription.name
            subscriptionPrice.text = subscription.price.toString()
            subscriptionDuration.text = subscription.durationDays.toString()

            btnChoose.setOnClickListener {
                itemClickListener(subscription)
            }
        }
    }
}