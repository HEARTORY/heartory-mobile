package com.heartsteel.heartory.ui.article

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthcarecomp.base.BaseFragment
import com.heartsteel.heartory.R
import com.heartsteel.heartory.common.util.Resource
import com.heartsteel.heartory.databinding.FragmentArticleBinding
import com.heartsteel.heartory.service.model.domain.ArticleList
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty

@AndroidEntryPoint
class ArticleFragment : BaseFragment(R.layout.fragment_article) {

    private lateinit var _binding: FragmentArticleBinding
    private lateinit var articleViewModel: ArticleViewModel
    private lateinit var linearLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleBinding.inflate(layoutInflater, container, false)
        _binding.topAppBar.setOnClickListener(View.OnClickListener {
            findNavController().navigateUp()
        })
        articleViewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)
        setupView()
        setupEvent()

        return _binding.root
    }

    private fun setupView() {
        val outerRecyclerView = _binding.recyclerView
        outerRecyclerView.layoutManager = LinearLayoutManager(context)
        articleViewModel.getArticleList()
        articleViewModel.articleList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    showLoading2()
                }

                is Resource.Success -> {
                    hideLoading2()
                    if(it.data == null){
                        Toasty.info(
                            requireContext(),
                            "No data found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    val outerAdapter = OuterAdapter(it.data!!) { innerItem ->
                        // Handle the click and navigate to the article details
                        val action = ArticleFragmentDirections.actionArticleFragmentToArticleDetailsFragment(innerItem.id!!,innerItem.title!!)
                        findNavController().navigate(action)
                    }
                    outerRecyclerView.adapter = outerAdapter
                }

                is Resource.Error -> {
                    hideLoading2()
                    Toasty.error(
                        requireContext(),
                        it.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }


    private fun setupEvent() {

    }


}