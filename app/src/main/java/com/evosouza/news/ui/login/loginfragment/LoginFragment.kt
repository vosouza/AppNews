package com.evosouza.news.ui.login.loginfragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.evosouza.news.R
import com.evosouza.news.data.database.NewsDB
import com.evosouza.news.data.database.repository.UserRepositoryImpl
import com.evosouza.news.data.model.User
import com.evosouza.news.databinding.FragmentLoginBinding
import com.evosouza.news.ui.home.homeactivity.HomeActivity
import com.evosouza.news.ui.login.loginfragment.viewmodel.LoginViewModel
import com.evosouza.news.util.setError
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
        viewModel = LoginViewModel.LoginViewModelProvider(db).create(LoginViewModel::class.java)


        viewModel.insertUser(User("vini@a.com", "vini12", "123456", ""))


        binding.buttonLogin.setOnClickListener{
           login(binding.emailTextEDT.text.toString(), binding.passeordEDT.text.toString())
        }

        binding.textCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_userNameFragment2)
        }

        observeVmEvents()
    }

    private fun login(email: String, password: String){
        viewModel.login(email, password)?.observe(viewLifecycleOwner){ user ->
            user?.let {
                openHomeActivity()
            } ?: kotlin.run {
                Toast.makeText(requireContext(), "User didnt find", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun observeVmEvents(){

        viewModel.loading.observe(viewLifecycleOwner){
            if(it) showLoading() else hideLoading()
        }

        viewModel.loginFieldErrorResId.observe(viewLifecycleOwner){
            binding.inputLayoutUserName.setError(requireContext(), it)
        }

        viewModel.passwordErrorResId.observe(viewLifecycleOwner){
            binding.inputLayoutPassword.setError(requireContext(), it)
        }

    }

    private fun openHomeActivity() {
        startActivity(Intent(requireContext(), HomeActivity::class.java))
    }

    private fun showLoading() {
        binding.loadingProgressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.loadingProgressBar.visibility = View.GONE
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}