package com.evosouza.news.ui.login.loginfragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.evosouza.news.R
import com.evosouza.news.data.database.NewsDB
import com.evosouza.news.data.database.repository.UserRepositoryImpl
import com.evosouza.news.data.sharedpreference.SharedPreference
import com.evosouza.news.databinding.FragmentLoginBinding
import com.evosouza.news.ui.home.homeactivity.HomeActivity
import com.evosouza.news.ui.login.loginfragment.viewmodel.LoginViewModel
import com.evosouza.news.util.setError

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = UserRepositoryImpl(NewsDB(requireContext()))
        val cache = SharedPreference(requireContext())
        viewModel = LoginViewModel.LoginViewModelProvider(db, cache).create(LoginViewModel::class.java)

        viewModel.getUserSavedEmail()

        binding.buttonLogin.setOnClickListener {
            login(binding.emailTextEDT.text.toString(), binding.passwordEDT.text.toString())
        }

        binding.textCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_userNameFragment2)
        }

        binding.checkBoxSaveLogin.setOnClickListener {
            if (!binding.checkBoxSaveLogin.isChecked) viewModel.deleteUserEmailLogin()
        }

        observeVmEvents()
    }

    private fun login(email: String, password: String) {
        viewModel.login(email, password)?.observe(viewLifecycleOwner) { user ->
            user?.let {
                saveEmailText(user.email)
                saveUserId(user.id)
                openHomeActivity()
            } ?: kotlin.run {
                binding.errorText.visibility = View.VISIBLE
            }
        }
    }

    private fun saveEmailText(email: String) {
        if (binding.checkBoxSaveLogin.isChecked) {
            viewModel.saveUserEmailLogin(email)
        } else {
            viewModel.deleteUserEmailLogin()
        }
    }

    private fun saveUserId(id: Long){
        viewModel.saveUserID(id)
    }

    private fun observeVmEvents() {

        viewModel.userEmailSavedLogin.observe(viewLifecycleOwner) {
            binding.emailTextEDT.setText(it)
            if (it.isNotEmpty()) binding.checkBoxSaveLogin.isChecked = true
        }

        viewModel.loginFieldErrorResId.observe(viewLifecycleOwner) {
            binding.inputLayoutUserName.setError(requireContext(), it)
        }

        viewModel.passwordErrorResId.observe(viewLifecycleOwner) {
            binding.inputLayoutPassword.setError(requireContext(), it)
        }

    }

    private fun openHomeActivity() {
        startActivity(Intent(requireContext(), HomeActivity::class.java))
        activity?.finish()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}