package com.evosouza.news.ui.home.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.evosouza.news.R
import com.evosouza.news.data.database.NewsDB
import com.evosouza.news.data.database.repository.DBRepositoryImpl
import com.evosouza.news.data.model.Article
import com.evosouza.news.databinding.FragmentFavoriteBinding
import com.evosouza.news.ui.home.adapter.NewsAdapter
import com.evosouza.news.ui.home.favorites.viewmodel.FavoritesViewModel

class FavoriteFragment : Fragment() {

    private lateinit var viewModel: FavoritesViewModel
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var newsAdapter: NewsAdapter

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
                setRecyclerView(it)
            }
        }

    }

    private fun setRecyclerView(list: List<Article>) {
        setAdapter(list)
        binding.favoriteRecyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun setAdapter(list: List<Article>){
        newsAdapter = NewsAdapter(list){
            findNavController().navigate(R.id.action_favoriteFragment_to_articleFragment,
            Bundle().apply
             {
                 putSerializable("article", it)
             })
        }
    }

}