package com.heartsteel.heartory.ui.heart_rate

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.heartsteel.heartory.R

class HearRateFragment : Fragment() {

    companion object {
        fun newInstance() = HearRateFragment()
    }

    private val viewModel: HearRateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_hear_rate, container, false)
    }
}