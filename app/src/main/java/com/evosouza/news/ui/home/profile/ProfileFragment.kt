package com.evosouza.news.ui.home.profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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

        getUserData()
        setupObservers()
        setupClickListener()

    }

    private fun setupClickListener() {
        binding.txtChangeData.setOnClickListener {
            showFormData()
        }

        binding.btnChangeInterests.setOnClickListener {
            openSubjectsFragments()
        }

        binding.imgCamera.setOnClickListener {
            chooseImage()
        }

        binding.btnSaveData.setOnClickListener {
            binding.apply {
                viewModel.validateUserInput(
                    formData.edtUserName.text.toString(),
                    formData.edtUserEmail.text.toString(),
                    formData.edtLastUserPassword.text.toString(),
                    formData.edtNewUserPassword.text.toString(),
                    imageBitmap?.bitmapToString()
                )
            }
        }

        binding.formData.cancelButton.setOnClickListener {
            showDataText()
        }
    }

    private fun openSubjectsFragments() {
        findNavController().navigate(R.id.action_profileFragment_to_subjectChoseFragment)
    }

    private fun setupObservers() {
        viewModel.updateUserData.observe(viewLifecycleOwner) { done ->
            when (done.status) {
                Status.ERROR -> {
                    showDataText()
                    binding.progressBar.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    getUserData()
                    showDataText()
                    binding.progressBar.visibility = View.GONE
                }
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }

        viewModel.lastPassword.observe(viewLifecycleOwner) {
            binding.formData.inputLayoutLastUserPassword.setErrorResId(requireContext(), it)
        }

        viewModel.newPassword.observe(viewLifecycleOwner) {
            binding.formData.inputNewUserPassword.setErrorResId(requireContext(), it)
        }

        viewModel.userEmail.observe(viewLifecycleOwner) {
            binding.formData.inputLayoutUserEmail.setErrorResId(requireContext(), it)
        }

        viewModel.userName.observe(viewLifecycleOwner) {
            binding.formData.inputLayoutUserName.setErrorResId(requireContext(), it)
        }
    }

    private fun getUserData() {
        viewModel.getUserData().observe(viewLifecycleOwner) { userData ->
            user = userData
            fillUserFields(user)
            fillEdtUser(user)
            user.photo?.let { setImageResourceFromPath(it) }
        }
    }

    private fun fillUserFields(user: User) {
        binding.apply {
            textData.txtUserEmail.text = resources.getString(R.string.email, user.email)
            textData.txtUserName.text = resources.getString(R.string.username, user.userName)
            textData.txtUserPassword.text =
                resources.getString(R.string.password, user.password.toStars())
        }
    }

    private fun setImageResourceFromPath(path: String) {
        if (path.isEmpty() || path.isBlank()) return
        val bitmap = path.stringBase64ToBitmap()
        binding.profileImage.setImageBitmap(bitmap)
    }

    private fun fillEdtUser(user: User) {
        binding.apply {
            formData.edtUserEmail.setText(user.email)
            formData.edtUserName.setText(user.userName)
        }
    }

    private fun chooseImage() = getContent.launch("image/*")

    private fun showDataText() {
        binding.textData.root.visibility = View.VISIBLE
        binding.btnChangeInterests.visibility = View.VISIBLE
        binding.txtChangeData.visibility = View.VISIBLE
        binding.btnSaveData.visibility = View.GONE
        binding.formData.root.visibility = View.GONE
        binding.imgCamera.visibility = View.GONE
    }

    private fun showFormData() {
        binding.textData.root.visibility = View.GONE
        binding.btnChangeInterests.visibility = View.GONE
        binding.txtChangeData.visibility = View.GONE
        binding.btnSaveData.visibility = View.VISIBLE
        binding.formData.root.visibility = View.VISIBLE
        binding.imgCamera.visibility = View.VISIBLE
    }
}