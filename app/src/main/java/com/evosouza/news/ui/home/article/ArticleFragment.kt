package com.evosouza.news.ui.home.article

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import com.evosouza.news.data.database.NewsDB
import com.evosouza.news.data.database.repository.DBRepositoryImpl
import com.evosouza.news.data.model.Article
import com.evosouza.news.data.sharedpreference.SharedPreference
import com.evosouza.news.databinding.FragmentArticleBinding
import com.evosouza.news.ui.home.article.viewmodel.ArticleViewModel
import kotlinx.coroutines.Dispatchers

class ArticleFragment : Fragment() {

    private lateinit var viewModel: ArticleViewModel
    private lateinit var binding: FragmentArticleBinding
    private lateinit var article: Article
    private var userID: Long? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        article = arguments?.getSerializable("article") as Article

        val repository = DBRepositoryImpl(NewsDB(requireContext()))
        val cache = SharedPreference(requireContext())

        viewModel = ArticleViewModel.ArticleViewModelProviderFactory(
            Dispatchers.IO,
            cache,
            repository
        ).create(ArticleViewModel::class.java)

        userID = viewModel.getUserId()

        binding.webView.apply {
            webViewClient = WebViewClient()
            article.url?.let { loadUrl(it) }
        }

        binding.fab.setOnClickListener {
            userID?.let { article.userId = it }
            viewModel.saveArticle(article)
            Toast.makeText(requireContext(), "artigo salvo", Toast.LENGTH_SHORT).show()
        }
    }
}