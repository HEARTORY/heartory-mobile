package com.heartsteel.heartory.ui.article

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthcarecomp.base.BaseFragment
import com.heartsteel.heartory.R
import com.heartsteel.heartory.databinding.FragmentArticleBinding

class ArticleFragment : BaseFragment(R.layout.fragment_article) {

    private lateinit var _binding: FragmentArticleBinding
    private val _viewModel: ArticleViewModel by viewModels()
    private lateinit var linearLayout: LinearLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleBinding.inflate(layoutInflater, container, false)
        setupView()
        setupEvent()

        return _binding.root
    }

    private fun setupView() {
        val outerRecyclerView = _binding.recyclerView
        outerRecyclerView.layoutManager = LinearLayoutManager(context)

        // Sample data
        val innerItems1 = listOf(
            InnerItem("Title 1", "https://example.com/image1.jpg"),
            InnerItem("Title 2", "https://example.com/image2.jpg")
        )

        val innerItems2 = listOf(
            InnerItem("Title 3", "https://example.com/image3.jpg"),
            InnerItem("Title 4", "https://example.com/image4.jpg")
        )

        val outerItems = listOf(
            OuterItem("Category 1", innerItems1),
            OuterItem("Category 2", innerItems2)
        )
        val outerAdapter = OuterAdapter(outerItems)
        outerRecyclerView.adapter = outerAdapter

    }

    private fun setupEvent() {

    }


}