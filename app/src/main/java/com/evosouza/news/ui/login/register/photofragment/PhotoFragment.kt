package com.evosouza.news.ui.login.register.photofragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
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
import com.evosouza.news.util.bitmapToString
import timber.log.Timber

class PhotoFragment : Fragment() {

    private lateinit var binding: FragmentPhotoBinding
    private lateinit var viewModel: PhotoViewModel
    private lateinit var user: User
    private var imageBitmap: Bitmap? = null

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                try {
                    val imaStream = requireActivity().contentResolver.openInputStream(uri)
                    imageBitmap = BitmapFactory.decodeStream(imaStream)
                    binding.profileImage.setImageBitmap(imageBitmap)
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }

        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user = arguments?.getSerializable("user") as User
        Timber.e("onViewCreated: $user")

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
        navigateNextScreen()
    }

    private fun saveUser() {
        imageBitmap?.let {
            user.photo = it.bitmapToString()
        }
        viewModel.saveUser(user)
    }

    private fun buttonClickListeners() {
        binding.btnSkype.setOnClickListener {
            showDialog()
        }

        binding.profileImage.setOnClickListener {
            chooseImage()
        }

        binding.buttonNext.setOnClickListener {
            if (imageBitmap != null) {
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
            setYesListener {
                saveUser()
            }

        }.show(parentFragmentManager, "PhotoFragment")
    }

    private fun navigateNextScreen() =
        findNavController().navigate(R.id.action_photoFragment_to_welcomeFragment)

    private fun chooseImage() = getContent.launch("image/*")

}