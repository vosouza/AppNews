package com.evosouza.news.ui.login.register.passwordfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.evosouza.news.R
import com.evosouza.news.data.model.User
import com.evosouza.news.databinding.FragmentPasswordBinding
import com.evosouza.news.ui.login.register.passwordfragment.viewmodel.PasswordViewModel
import com.evosouza.news.util.setError

class PasswordFragment : Fragment() {

    private lateinit var binding: FragmentPasswordBinding
    private lateinit var viewModel: PasswordViewModel
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         super.onViewCreated(view, savedInstanceState)

        user = arguments?.getSerializable("user") as User
        viewModel = PasswordViewModel.PasswordViewModelProviderFactory().create(PasswordViewModel::class.java)

        binding.buttonCreatePrevious.setOnClickListener(previousScreen())
        binding.buttonCreateNext.setOnClickListener(validatePassword())

        observeVM()
    }

    private fun previousScreen(): (v: View) -> Unit = {
        findNavController().popBackStack()
    }

    private fun validatePassword(): (v: View) -> Unit = {
        val password = binding.createPasswordField.text.toString()
        val confirm = binding.confirmPasswordField.text.toString()
        viewModel.validatePassword(password, confirm)
    }

    private fun observeVM() {
        viewModel.confirmPasswordResId.observe(viewLifecycleOwner){
            binding.inputLayoutConfirmPassword.setError(requireContext(), it)
        }
        viewModel.passwordErrorResId.observe(viewLifecycleOwner){
            binding.inputLayoutPassword.setError(requireContext(), it)
        }
        viewModel.passwordValid.observe(viewLifecycleOwner){ passwordText ->
            passwordText?.let { password ->
                user.password = password
                sendUserBundle(user)
            }
        }
    }

    private fun sendUserBundle(userToSend: User){
        findNavController().navigate(R.id.action_passwordFragment_to_photoFragment, Bundle().apply {
            putSerializable("user", userToSend)
        })
    }

}