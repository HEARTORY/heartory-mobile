package com.heartsteel.heartory.ui.article

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.navigation.fragment.findNavController
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
        _binding.topAppBar.setOnClickListener(View.OnClickListener {
            findNavController().navigateUp()
        })
        setupView()
        setupEvent()

        return _binding.root
    }

    private fun setupView() {
        val outerRecyclerView = _binding.recyclerView
        outerRecyclerView.layoutManager = LinearLayoutManager(context)

        // Sample data
        val innerItems1 = listOf(
            InnerItem("Title 1", "https://www.behavioralhealthnews.org/wp-content/uploads/2020/01/MHN-Summer2011Cover.jpg"),
            InnerItem("Title 2", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ9mwjd4uTI52xhnRnlmZ_AhTNkSqLOwF5MMg&s"),
                    InnerItem("Title 1", "https://www.behavioralhealthnews.org/wp-content/uploads/2020/01/MHN-Summer2011Cover.jpg"),
            InnerItem("Title 1", "https://www.behavioralhealthnews.org/wp-content/uploads/2020/01/MHN-Summer2011Cover.jpg"),
            InnerItem("Title 2", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ9mwjd4uTI52xhnRnlmZ_AhTNkSqLOwF5MMg&s"),
            InnerItem("Title 2", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ9mwjd4uTI52xhnRnlmZ_AhTNkSqLOwF5MMg&s"),
        )

        val innerItems2 = listOf(
            InnerItem("Title 3", "https://www.cause.org.uk/GetImage.aspx?IDMF=e9c4f862-63ef-4809-816b-edf52c2c6738&w=571&h=819&src=mc"),
            InnerItem("Title 4", "https://domf5oio6qrcr.cloudfront.net/medialibrary/335/Harvard_Mens_Health_Watch.jpg"),
            InnerItem("Title 1", "https://www.behavioralhealthnews.org/wp-content/uploads/2020/01/MHN-Summer2011Cover.jpg"),
            InnerItem("Title 2", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ9mwjd4uTI52xhnRnlmZ_AhTNkSqLOwF5MMg&s"),
            InnerItem("Title 1", "https://www.behavioralhealthnews.org/wp-content/uploads/2020/01/MHN-Summer2011Cover.jpg"),
            InnerItem("Title 2", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ9mwjd4uTI52xhnRnlmZ_AhTNkSqLOwF5MMg&s"),
        )

        val outerItems = listOf(
            OuterItem("Category 1", innerItems1),
            OuterItem("Category 2", innerItems2)
        )
        val outerAdapter = OuterAdapter(outerItems) { innerItem ->
            // Handle the click and navigate to the article details
            val action = ArticleFragmentDirections.actionArticleFragmentToArticleDetailsFragment(innerItem.title, innerItem.imageUrl)
            findNavController().navigate(action)
        }
        outerRecyclerView.adapter = outerAdapter

    }

    private fun setupEvent() {

    }


}