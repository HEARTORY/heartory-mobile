package com.heartsteel.heartory.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.healthcarecomp.base.BaseFragment
import com.heartsteel.heartory.R
import com.heartsteel.heartory.common.util.Resource
import com.heartsteel.heartory.databinding.FragmentArticleDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.avatarview.coil.loadImage
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint

class ArticleDetailsFragment : BaseFragment(R.layout.fragment_article_details) {

    private lateinit var binding: FragmentArticleDetailsBinding
    private lateinit var articleViewModel: ArticleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleDetailsBinding.inflate(inflater, container, false)

//        val args: ArticleDetailFragmentArgs by navArgs()
//        binding.title.text = args.title
//        Glide.with(binding.image.context)
//            .load(args.imageUrl)
//            .into(binding.image)
        binding.avUserAvatar.loadImage(R.drawable.heartory_app_logo)
        binding.topAppBar.setOnClickListener(View.OnClickListener {
            findNavController().navigateUp()
        })
        val args: ArticleDetailsFragmentArgs by navArgs()

        val articleId = args.id

        articleViewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)
        articleViewModel.getArticleById(articleId)

        articleViewModel.articleDetail.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Show loading state
                    showLoading2()
                }
                is Resource.Success -> {
                    // Hide loading state
                    hideLoading2()
                    // Bind article details to the view
                    val article = resource.data
                    binding.txtTitle.text = article?.title
                    if (article != null) {
                        binding.tvDate.text = article.createdAt?.let {
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it)
                        } ?: "Unknown Date"
                    }
                    if (article != null) {
                        binding.txtContent.text = android.text.Html.fromHtml(article.content, android.text.Html.FROM_HTML_MODE_COMPACT)
                    }
                    Glide.with(binding.imgThumbnail.context)
                        .load(article?.imageUrl)
                        .into(binding.imgThumbnail)
                }
                is Resource.Error -> {
                    // Hide loading state and show error message
                    hideLoading2()
//                    binding.progressBar.visibility = View.GONE
//                    binding.txtTitle.text = getString(R.string.error_loading_article)
                }
            }
        })

        binding.topAppBar.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }
}
