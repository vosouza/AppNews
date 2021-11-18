package com.evosouza.news.ui.login.register.photofragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.evosouza.news.R
import com.evosouza.news.data.model.User
import com.evosouza.news.databinding.FragmentPhotoBinding

class PhotoFragment : Fragment() {

    private lateinit var binding: FragmentPhotoBinding
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user = arguments?.getSerializable("user") as User

        buttonPrevious()
        buttonNext()
        getImageButton()
    }

    private fun getImageButton() {

    }

    fun buttonNext(){
        binding.buttonNext.setOnClickListener {

        }
    }

    fun buttonPrevious(){
        binding.buttonPrevious.setOnClickListener {
            findNavController().popBackStack()
        }
    }



}