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
import com.evosouza.news.data.sharedpreference.SharedPreference
import com.evosouza.news.databinding.FragmentLoginBinding
import com.evosouza.news.ui.home.homeactivity.HomeActivity
import com.evosouza.news.ui.login.loginfragment.viewmodel.LoginViewModel
import com.evosouza.news.util.setError
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private lateinit var sharedPreference: SharedPreference
    private lateinit var viewModel: LoginViewModel
    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding get() = _binding!!
    private var isChecked = false

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

        sharedPreference  = SharedPreference(requireContext())

//        viewModel.insertUser(User("vini@a.com", "vini12", "123456", ""))

        sharedPreference.getData(SharedPreference.EMAIL)?.let {
            binding.emailTextEDT.setText(it)
            binding.checkboxPassword.isChecked = true
            isChecked = true
        }

        binding.buttonLogin.setOnClickListener{
           login(binding.emailTextEDT.text.toString(), binding.passwordEDT.text.toString())
        }

        binding.textCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_userNameFragment2)
        }

        observeVmEvents()
    }

    private fun login(email: String, password: String){
        viewModel.login(email, password)?.observe(viewLifecycleOwner){ user ->
            user?.let {
                openHomeActivity(user.email)
            } ?: kotlin.run {
                binding.errorText.visibility  = View.VISIBLE
            }
        }
    }

    private fun observeVmEvents(){
        viewModel.loginFieldErrorResId.observe(viewLifecycleOwner){
            binding.inputLayoutUserName.setError(requireContext(), it)
        }

        viewModel.passwordErrorResId.observe(viewLifecycleOwner){
            binding.inputLayoutPassword.setError(requireContext(), it)
        }
    }

    private fun saveEmailChecked(){
        if(binding.checkboxPassword.isChecked == true) isChecked = true
    }

    private fun openHomeActivity(email : String) {
        if (!isChecked){
            sharedPreference.saveData(SharedPreference.EMAIL, email)
        }
        startActivity(Intent(requireContext(), HomeActivity::class.java))
        activity?.finish()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}