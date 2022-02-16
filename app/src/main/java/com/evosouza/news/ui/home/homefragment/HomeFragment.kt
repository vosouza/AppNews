package com.evosouza.news.ui.home.homefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.evosouza.news.BuildConfig
import com.evosouza.news.R
import com.evosouza.news.core.Status
import com.evosouza.news.data.model.Article
import com.evosouza.news.data.network.ApiService
import com.evosouza.news.data.repository.NewsRepositoryImpl
import com.evosouza.news.data.sharedpreference.SharedPreference
import com.evosouza.news.databinding.FragmentHomeBinding
import com.evosouza.news.ui.home.adapter.NewsAdapter
import com.evosouza.news.ui.home.homefragment.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers


class HomeFragment : Fragment() {

    lateinit var viewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val newsRepository = NewsRepositoryImpl(ApiService.service)
        val cache = SharedPreference(requireContext())
        viewModel = HomeViewModel.HomeViewModelProviderFactory(
            Dispatchers.IO,
            newsRepository,
            cache
        ).create(HomeViewModel::class.java)

        observeVMEvents()
        getNews()
        getSubjects()

        binding.swipeLayout.setOnRefreshListener {
            getNews()
        }

    }

    private fun getSubjects() {
        viewModel.getSubjects()
    }

    private fun observeVMEvents() {
        viewModel.response.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { newsResponse ->
                        setCarrousel(newsResponse.articles)
                        setRecyclerView(newsResponse.articles)
                    }
                    binding.swipeLayout.isRefreshing = false
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), "Erro: ${it.error}", Toast.LENGTH_SHORT).show()
                    binding.swipeLayout.isRefreshing = false
                }
                Status.LOADING -> {
                    binding.swipeLayout.isRefreshing = true
                    //binding.progressBar.visibility = if(it.loading == true) View.VISIBLE else View.GONE
                }
            }
        }

        viewModel.interests.observe(viewLifecycleOwner){ list ->
            when (list.status) {
                Status.SUCCESS -> {
                    viewModel.getListOfInterest()
                }
                Status.ERROR -> {
                    //fazer alguma coisa
                }
                Status.LOADING -> {
                    //fazer alguma outra coisa
                }
            }
        }
    }

    private fun setCarrousel(articles: List<Article>) {
        binding.carrousel.setList(articles as MutableList<Article>)
        binding.carrousel.setupCarrousel()
    }

    private fun setAdapter(list: List<Article>) {
        newsAdapter = NewsAdapter(list) { article ->
            findNavController().navigate(R.id.action_homeFragment_to_articleFragment,
                Bundle().apply {
                    putSerializable("article", article)
                })
        }
    }

    private fun setRecyclerView(list: List<Article>) {
        setAdapter(list)
        with(binding.rvHome) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = newsAdapter
        }
    }

    private fun getNews() {
        viewModel.getBreakNews("us", 1, BuildConfig.API_KEY)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}