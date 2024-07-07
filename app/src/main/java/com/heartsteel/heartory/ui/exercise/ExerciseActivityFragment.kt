package com.heartsteel.heartory.ui.exercise

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.heartsteel.heartory.R
import com.heartsteel.heartory.data.model.Exercise
import com.heartsteel.heartory.databinding.FragmentExerciseActivityListBinding
class ExerciseActivityFragment : Fragment() {

    private var _binding: FragmentExerciseActivityListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseActivityListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val category = arguments?.getString("category")

        Log.d("ExerciseActivityFragment", "Category: $category")

        setupRecyclerView(category)
        todayActivityClickListener()
        backClickListener()
    }

    private fun setupRecyclerView(category: String?) {
        val exerciseList = listOf(
            // Yoga
            Exercise(name = "Mountain pose ", category = "Yoga", instructorName = "John Doe", imageUrl = "https://vinmec-prod.s3.amazonaws.com/images/20210128_014054_218191_tu-the-ngon-nui.max-1800x1800.jpg", videoUrl = "https://www.youtube.com/embed/ATLU-XX_lro"),
            Exercise(name = "Warrior one", category = "Yoga", instructorName = "Jane Smith", imageUrl = "https://vinmec-prod.s3.amazonaws.com/images/20210128_013853_874707_tu-the-chien-binh-t.max-1800x1800.png",videoUrl = "https://www.youtube.com/embed/k4qaVoAbeHM"),
            Exercise(name = "Tree Pose", category = "Yoga", instructorName = "John Smith", imageUrl = "https://vinmec-prod.s3.amazonaws.com/images/20210128_013725_863484_tu-the-cai-cay.max-1800x1800.jpg",videoUrl = "https://www.youtube.com/embed/wdln9qWYloU"),
            Exercise(name = "Bridge Pose", category = "Yoga", instructorName = "Jane Doe", imageUrl = "https://vinmec-prod.s3.amazonaws.com/images/20210128_013528_609227_yoga-tu-the-cay-cau.max-1800x1800.jpg",videoUrl = "https://www.youtube.com/embed/XUcAuYd7VU0"),

            // Warm Up
            Exercise(name = "Knee Push Up", category = "Warm Up", instructorName = "John Doe", imageUrl = "https://www.wellandgood.com/wp-content/uploads/2020/04/Stocksy_txpf755269aTFh200_Small_2273968.jpg",videoUrl = "https://www.youtube.com/embed/WcHtt6zT3Go"),
            Exercise(name = "Arm Circles", category = "Warm Up", instructorName = "Jane Smith", imageUrl = "https://s3assets.skimble.com/assets/4831/skimble-workout-trainer-exercse-large-arm-circles-4_iphone.jpg",videoUrl = "https://www.youtube.com/embed/UVMEnIaY8aU"),
            Exercise(name = "Jumping Jacks", category = "Warm Up", instructorName = "John Smith", imageUrl = "https://cdn.prod.website-files.com/62e18da95149ec2ee0d87b5b/65b0d643eb8c14b2ff3c6eaf_thumbnail-image-65ae476a9d643.webp",videoUrl = "https://www.youtube.com/embed/CWpmIW6l-YA"),
            Exercise(name = "Leg Swings", category = "Warm Up", instructorName = "Jane Doe", imageUrl = "https://cdn.womensrunning.com/wp-content/uploads/2020/04/forward-leg-swing.jpg?width=720",videoUrl = "https://www.youtube.com/embed/E68-pMl1Im8"),

            // Push Up
            Exercise(name = "Strict Push Up", category = "Push Up", instructorName = "Jane Doe", imageUrl = "https://cdn.outsideonline.com/wp-content/uploads/2019/02/15/1-strict-a.jpg?height=230",videoUrl = "hhttps://www.youtube.com/embed/IODxDxX7oi4"),
            Exercise(name = "Incline Push Up", category = "Push Up", instructorName = "John Smith", imageUrl = "https://www.wellandgood.com/wp-content/uploads/2020/06/GettyImages-1180150335.jpg",videoUrl = "https://www.youtube.com/embed/Me9bHFAxnCs"),
            Exercise(name = "Decline Push Up", category = "Push Up", instructorName = "Jane Smith", imageUrl = "https://post.healthline.com/wp-content/uploads/2019/05/Female_Pushup_Bench_1296x728-header-1296x728.jpg",videoUrl = "https://www.youtube.com/embed/CC4kAF1HQ"),
            Exercise(name = "Wide Push Up", category = "Push Up", instructorName = "John Doe", imageUrl = "https://cdn.shopify.com/s/files/1/1876/4703/files/shutterstock_524469490_480x480.jpg?v=1633716844",videoUrl = "https://www.youtube.com/embed/m3FTgWtJsFE"),

            // Shoulder
            Exercise(name = "Front raise", category = "Shoulder", instructorName = "Jane Doe", imageUrl = "https://www.dmoose.com/cdn/shop/articles/1_4cd4953d-bb36-43b2-b137-0003b8e77955.jpg?v=1653303297",videoUrl = "https://www.youtube.com/embed/zkP0MsTcIVU"),
            Exercise(name = "Shoulder Press", category = "Shoulder", instructorName = "John Smith", imageUrl = "https://rogersathletic.com/wp-content/uploads/2023/04/overhead_press_001.jpg",videoUrl = "https://www.youtube.com/embed/3R14MnZbcpw"),
            Exercise(name = "Lateral Raise", category = "Shoulder", instructorName = "Jane Smith", imageUrl = "https://goodfit.vn/wp-content/uploads/2021/05/the-only-dumbbell-lateral-raise-article-you-need-header-v2-960x540-1.jpg",videoUrl = "https://www.youtube.com/embed/pOmbQuGeHf8"),
            Exercise(name = "Arnold Press", category = "Shoulder", instructorName = "John Doe", imageUrl = "https://www.triumph-physio.co.nz/wp-content/uploads/2023/01/IMG_1925.jpeg",videoUrl = "https://www.youtube.com/embed/pQDrcNoDNVM")
        )
        // Log the entire list to see if it contains the expected data
        Log.d("ExerciseActivityFragment", "Exercise List: $exerciseList")

        val filteredList = category?.let {
            exerciseList.filter { exercise -> exercise.category == it }
        } ?: exerciseList

        // Log the filtered list to ensure filtering logic is correct
        Log.d("ExerciseActivityFragment", "Filtered List: $filteredList")

        val adapter = ExerciseActivityAdapter(filteredList) { exercise ->
            val videoUrl = exercise.videoUrl ?: "https://www.youtube.com/embed/-p0PA9Zt8zk"
            val action = ExerciseActivityFragmentDirections.actionExerciseActivityFragmentToExerciseActivityVideoFragment(videoUrl, category)
            findNavController().navigate(action)
        }

        binding.recyclerViewActivity.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }
    }


    private fun todayActivityClickListener() {
        binding.todayActivity.setOnClickListener {
            findNavController().navigate(R.id.action_exerciseActivityListFragment_to_exerciseTodayActivityListFragment)
        }
    }

    private fun backClickListener() {
        binding.imageViewBackArrow.setOnClickListener {
            findNavController().navigate(R.id.action_exerciseActivityListFragment_to_exerciseFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}