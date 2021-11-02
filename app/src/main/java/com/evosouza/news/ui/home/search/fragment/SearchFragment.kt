package com.evosouza.news.ui.home.search.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.evosouza.news.BuildConfig
import com.evosouza.news.R
import com.evosouza.news.core.Status
import com.evosouza.news.data.model.Article
import com.evosouza.news.data.network.ApiService
import com.evosouza.news.data.repository.NewsRepositoryImpl
import com.evosouza.news.databinding.FragmentSearchBinding
import com.evosouza.news.ui.home.adapter.NewsAdapter
import com.evosouza.news.ui.home.homeactivity.HomeActivity
import com.evosouza.news.ui.home.search.viewmodel.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var viewModel: SearchViewModel
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = NewsRepositoryImpl(ApiService.service)
        viewModel = SearchViewModel.SearchViewModelProviderFactory(Dispatchers.IO, repository)
            .create(SearchViewModel::class.java)
        
        observeVMEvents()

        binding.edtSearch.addTextChangedListener { editable ->
            MainScope().launch {
                delay(1500L)
                hideKeyboard()
                editable?.let {
                    if (editable.toString().isNotEmpty()) getNews(editable.toString(), 1)
                }
            }
        }
    }

    private fun getNews(query: String, page: Int) {
        viewModel.searchNews(query, page, BuildConfig.API_KEY)
    }

    private fun observeVMEvents() {
        viewModel.response.observe(viewLifecycleOwner){
            when(it.status){
                Status.ERROR ->{
                    binding.searchProgressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "error na api", Toast.LENGTH_SHORT).show()
                }
                Status.LOADING ->{
                    binding.searchProgressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS ->{
                    binding.searchProgressBar.visibility = View.GONE
                    it?.data?.let { response ->
                        setRecyclerView(response.articles)
                    }

                }
            }
        }
    }

    
    private fun setAdapter(list: List<Article>) {
        searchAdapter = NewsAdapter(list) { article ->
            findNavController().navigate(
                R.id.action_searchFragment_to_articleFragment,
                Bundle().apply {
                    putSerializable("article", article)
                })
        }
    }

    private fun setRecyclerView(list: List<Article>){
       setAdapter(list)
       with(binding.searchRecyclerView){
           layoutManager = LinearLayoutManager(requireContext())
           setHasFixedSize(true)
           adapter = searchAdapter
       }
    }

    private fun hideKeyboard(){
        (activity as HomeActivity).hideKeyboard()
    }
}