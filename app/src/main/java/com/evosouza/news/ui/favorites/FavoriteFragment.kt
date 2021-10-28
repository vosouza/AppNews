package com.evosouza.news.ui.favorites

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.evosouza.news.R
import com.evosouza.news.data.database.NewsDB
import com.evosouza.news.data.database.repository.DBRepositoryImpl
import com.evosouza.news.databinding.FragmentFavoriteBinding
import com.evosouza.news.ui.favorites.viewmodel.FavoritesViewModel

class FavoriteFragment : Fragment() {

    private lateinit var viewModel: FavoritesViewModel
    private lateinit var binding: FragmentFavoriteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFavoriteBinding.inflate(inflater,container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = DBRepositoryImpl(NewsDB(requireContext()))
        viewModel = FavoritesViewModel.FavoritesViewModelProviderFactory(repository).create(FavoritesViewModel::class.java)

        viewModel.getAllArticles().observe(viewLifecycleOwner){ listArticles ->
            listArticles?.let {
                Log.d("NOTICIAS", it.toString())
            }
        }
    }

}