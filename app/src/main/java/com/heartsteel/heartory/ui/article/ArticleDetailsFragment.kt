package com.heartsteel.heartory.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.heartsteel.heartory.databinding.FragmentArticleDetailsBinding

class ArticleDetailsFragment : Fragment() {

    private lateinit var binding: FragmentArticleDetailsBinding

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

        return binding.root
    }
}
