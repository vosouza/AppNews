package com.evosouza.news.ui.login.register.usernamefragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.evosouza.news.R
import com.evosouza.news.databinding.FragmentUserNameBinding

class UserNameFragment : Fragment() {

    lateinit var binding: FragmentUserNameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserNameBinding.inflate(inflater, container, false)
        return binding.root
    }

}