package com.evosouza.news.ui.login.register.photofragment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.evosouza.news.R
import com.evosouza.news.data.database.NewsDB
import com.evosouza.news.data.database.repository.UserRepositoryImpl
import com.evosouza.news.data.model.User
import com.evosouza.news.databinding.FragmentPhotoBinding
import com.evosouza.news.ui.login.register.photofragment.photoviewmodel.PhotoViewModel
import com.evosouza.news.util.MessageDialog
import com.evosouza.news.util.WelcomeDialog

class PhotoFragment : Fragment() {

    private lateinit var binding: FragmentPhotoBinding
    private lateinit var viewModel: PhotoViewModel
    private lateinit var user: User
    private var imageUri: Uri? = null

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            imageUri = uri
            binding.profileImage.setImageURI(imageUri)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user = arguments?.getSerializable("user") as User
        Log.e("TAG", "onViewCreated: $user")

        val userDB = UserRepositoryImpl(NewsDB(requireContext()))
        viewModel =
            PhotoViewModel.PhotoViewModelProviderFactory(userDB).create(PhotoViewModel::class.java)

        buttonClickListeners()
        observerViewModelEvents()
    }

    private fun observerViewModelEvents() {
        viewModel.saveResult.observe(viewLifecycleOwner) {
            if (it) {
                showWelcomeMessage()
            } else {
                Toast.makeText(requireContext(), "falha ao salvar usuario", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun showWelcomeMessage() {
        val dialog = WelcomeDialog{
            findNavController().navigate(R.id.action_welcomeDialog_to_loginFragment)
        }
        dialog.show(parentFragmentManager, "WELCOME")
    }

    private fun saveUser() {
        imageUri?.let {
            user.photo = it.path
        }
        viewModel.saveUser(user)
    }

    private fun buttonClickListeners() {
        binding.btnSkype.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeDialog_to_loginFragment)
        }

        binding.profileImage.setOnClickListener {
            chooseImage()
        }

        binding.buttonNext.setOnClickListener {
            if (imageUri != null) {
                saveUser()
            } else {
                showDialog()
            }
        }

        binding.buttonPrevious.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun showDialog() {
        MessageDialog(
            "Do you want to continue?",
            "You have not choose a profile picture"
        ).apply {
            setYesListener{
                Toast.makeText(requireContext(), "next screen", Toast.LENGTH_SHORT).show()
            }

        }.show(parentFragmentManager, "PhotoFragment")
    }

    private fun chooseImage() = getContent.launch("image/*")

}