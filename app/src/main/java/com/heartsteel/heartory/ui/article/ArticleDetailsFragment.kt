package com.heartsteel.heartory.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.heartsteel.heartory.R
import com.heartsteel.heartory.databinding.FragmentArticleDetailsBinding
import io.getstream.avatarview.coil.loadImage

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
        binding.avUserAvatar.loadImage(R.drawable.heartory_app_logo)
        binding.topAppBar.setOnClickListener(View.OnClickListener {
            findNavController().navigateUp()
        })

        return binding.root
    }
}
