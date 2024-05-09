package com.heartsteel.heartory.ui.heart_rate_onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewbinding.ViewBindings
import com.heartsteel.heartory.R
import com.heartsteel.heartory.databinding.FragmentOnBoardingBinding


class OnBoardingFragment(val page: Page) : Fragment() {
    private lateinit var binding: FragmentOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        binding = FragmentOnBoardingBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_on_boarding, container, false)
        val title = view.findViewById<TextView>(R.id.tv_title)
        val description = view.findViewById<TextView>(R.id.tv_description)
        val image = view.findViewById<ImageView>(R.id.img_blood_pressure)

        title.text = page.title
        description.text = page.description
        image.setImageResource(page.image)
        return view
    }


}