package com.evosouza.news.ui.login.loginactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.evosouza.news.R
import com.evosouza.news.databinding.ActivityHomeBinding
import com.evosouza.news.databinding.ActivityLoginBinding
import com.evosouza.news.databinding.FragmentArticleBinding
import com.evosouza.news.ui.login.loginfragment.LoginFragment

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding: ActivityLoginBinding get() = _binding!!
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val navHost = supportFragmentManager.findFragmentById(R.id.navHostFragmentLogin)
//                as NavHostFragment
//        navController = navHost.navController
//
////        navController.navigate(R.layout.fragment_login)

    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}