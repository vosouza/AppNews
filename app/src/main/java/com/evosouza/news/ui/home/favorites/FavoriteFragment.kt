package com.evosouza.news.ui.home.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.evosouza.news.R
import com.evosouza.news.data.database.NewsDB
import com.evosouza.news.data.database.repository.DBRepositoryImpl
import com.evosouza.news.data.model.Article
import com.evosouza.news.data.sharedpreference.SharedPreference
import com.evosouza.news.databinding.FragmentFavoriteBinding
import com.evosouza.news.ui.home.adapter.NewsAdapter
import com.evosouza.news.ui.home.favorites.viewmodel.FavoritesViewModel

class FavoriteFragment : Fragment() {

    private lateinit var viewModel: FavoritesViewModel
    private var _binding: FragmentFavoriteBinding? = null
    private val binding: FragmentFavoriteBinding get() = _binding!!
    private lateinit var newsAdapter: NewsAdapter
    private var userID: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = DBRepositoryImpl(NewsDB(requireContext()))
        val cache = SharedPreference(requireContext())
        viewModel = FavoritesViewModel.FavoritesViewModelProviderFactory(cache, repository)
            .create(FavoritesViewModel::class.java)
        userID = viewModel.getUserId()

        userID?.let { id ->
            viewModel.getAllArticles(id).observe(viewLifecycleOwner) { listArticles ->
                listArticles?.let {
                    setRecyclerView(it)
                    if (it.count() > 0 ){
                        binding.logoImage.visibility = View.GONE
                        binding.errorMessage.visibility = View.GONE
                    }
                }
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

    private fun setAdapter(list: List<Article>) {
        newsAdapter = NewsAdapter(list) {
            findNavController().navigate(R.id.action_favoriteFragment_to_articleFragment,
                Bundle().apply
                {
                    putSerializable("article", it)
                })
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}