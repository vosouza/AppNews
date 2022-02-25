package com.evosouza.news.ui.home.profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.evosouza.news.core.Status
import com.evosouza.news.data.database.NewsDB
import com.evosouza.news.data.database.repository.UserRepositoryImpl
import com.evosouza.news.data.model.User
import com.evosouza.news.data.sharedpreference.SharedPreference
import com.evosouza.news.databinding.FragmentProfileBinding
import com.evosouza.news.ui.home.profile.viewmodel.ProfileViewModel
import com.evosouza.news.util.bitmapToString
import com.evosouza.news.util.setError
import com.evosouza.news.util.stringBase64ToBitmap
import kotlinx.coroutines.Dispatchers


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var user: User
    private var imageBitmap: Bitmap? = null
    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            val imaStream = requireActivity().contentResolver.openInputStream(uri)
            imageBitmap = BitmapFactory.decodeStream(imaStream)
            binding.profileImage.setImageBitmap(imageBitmap)
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

        viewModel.getUserData()
        setupObservers()
        getUserData()
        setupClickListener()

    }

    private fun setupClickListener() {
        binding.txtChangeData.setOnClickListener {
            binding.changeData.visibility = View.VISIBLE
            binding.showData.visibility = View.GONE
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
                    edtUserName.text.toString(),
                    edtUserEmail.text.toString(),
                    edtLastUserPassword.text.toString(),
                    edtNewUserPassword.text.toString(),
                    imageBitmap?.bitmapToString()
                )
            }
        }

        binding.cancelButton.setOnClickListener {
            binding.changeData.visibility = View.GONE
            binding.showData.visibility = View.VISIBLE
        }
    }

    private fun openSubjectsFragments() {
        //
    }

    private fun setupObservers() {
        viewModel.updateUserData.observe(viewLifecycleOwner) { done ->
            when (done.status) {
                Status.ERROR -> {
                    binding.changeData.visibility = View.GONE
                    binding.showData.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    getUserData()
                    binding.changeData.visibility = View.GONE
                    binding.showData.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }

        viewModel.lastPassword.observe(viewLifecycleOwner) {
            binding.inputLayoutLastUserPassword.setError(requireContext(), it)
        }

        viewModel.newPassword.observe(viewLifecycleOwner) {
            binding.inputNewUserPassword.setError(requireContext(), it)
        }

        viewModel.userEmail.observe(viewLifecycleOwner) {
            binding.inputLayoutUserEmail.setError(requireContext(), it)
        }

        viewModel.userName.observe(viewLifecycleOwner) {
            binding.inputLayoutUserName.setError(requireContext(), it)
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
            txtUserEmail.text = "Email: ${user.email}"
            txtUserName.text = "User Name: ${user.userName}"
            txtUserPassword.text = "Password: ${user.password}"
        }
    }

    private fun setImageResourceFromPath(path: String) {
        if (path.isEmpty() || path.isBlank()) return
        val bitmap = path.stringBase64ToBitmap()
        binding.profileImage.setImageBitmap(bitmap)
    }

    private fun fillEdtUser(user: User) {
        binding.apply {
            edtUserEmail.setText(user.email)
            edtUserName.setText(user.userName)
        }
    }

    private fun chooseImage() = getContent.launch("image/*")
}