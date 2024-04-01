package com.evosouza.news.ui.home.profile

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.evosouza.news.R
import com.evosouza.news.core.Status
import com.evosouza.news.data.database.NewsDB
import com.evosouza.news.data.database.repository.UserRepositoryImpl
import com.evosouza.news.data.model.Article
import com.evosouza.news.data.model.User
import com.evosouza.news.data.sharedpreference.SharedPreference
import com.evosouza.news.databinding.FragmentProfileBinding
import com.evosouza.news.ui.home.profile.viewmodel.ProfileViewModel
import com.evosouza.news.util.bitmapToString
import com.evosouza.news.util.setErrorResId
import com.evosouza.news.util.stringBase64ToBitmap
import com.evosouza.news.util.toStars
import kotlinx.coroutines.Dispatchers
import timber.log.Timber


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val cache = SharedPreference(requireContext())
        val dataBase = UserRepositoryImpl(NewsDB.invoke(requireContext()))
        viewModel = ProfileViewModel.ProfileViewModelProviderFactory(
            Dispatchers.IO,
            dataBase,
            cache
        ).create(ProfileViewModel::class.java)

        setupClickListener()
    }

    private fun setupClickListener() {
        binding.btnChangeInterests.setOnClickListener {
            openSubjectsFragments()
        }

        binding.site.setOnClickListener{
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://studioappnews.blogspot.com/p/app-news.html")
                )
            )
        }

        binding.policy.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://studioappnews.blogspot.com/p/privacy-policy.html")
                )
            )
        }
    }

    private fun openSubjectsFragments() {
        findNavController().navigate(R.id.action_profileFragment_to_subjectChoseFragment)
    }
}