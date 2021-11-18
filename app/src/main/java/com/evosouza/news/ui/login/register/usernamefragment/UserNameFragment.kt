package com.evosouza.news.ui.login.register.usernamefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.evosouza.news.R
import com.evosouza.news.data.model.User
import com.evosouza.news.databinding.FragmentUserNameBinding
import com.evosouza.news.ui.login.register.usernamefragment.viewmodel.UserNameViewModel
import com.evosouza.news.util.setError

class UserNameFragment : Fragment() {

    lateinit var binding: FragmentUserNameBinding
    lateinit var viewModel: UserNameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = UserNameViewModel.UserNameViewModelProviderFactory()
            .create(UserNameViewModel::class.java)

        binding.buttonCreateNext.setOnClickListener {
            val username = binding.edtCreateUserName.text.toString()
            val email = binding.edtInsertEmail.text.toString()
            val confirmEmail = binding.edtConfirmEmail.text.toString()
            viewModel.validateUserName(username, email, confirmEmail)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.userNameFieldError.observe(viewLifecycleOwner) {
            binding.inputLayoutCreateUserName.setError(requireContext(), it)
        }

        viewModel.emailError.observe(viewLifecycleOwner) {
            binding.inputLayoutEmail.setError(requireContext(), it)
        }

        viewModel.confirmEmailError.observe(viewLifecycleOwner) {
            binding.inputLayoutConfirmEmail.setError(requireContext(), it)
        }

        viewModel.userData.observe(viewLifecycleOwner) {
            sendUserBundle(it)
        }
    }

    private fun sendUserBundle(userToSends: User) {
        findNavController().navigate(
            R.id.action_userNameFragment_to_passwordFragment,
            Bundle().apply {
                putSerializable("user", userToSends)
            })
    }

}