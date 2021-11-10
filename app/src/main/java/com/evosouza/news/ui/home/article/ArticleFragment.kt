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
import com.evosouza.news.databinding.FragmentArticleBinding
import com.evosouza.news.ui.home.article.viewmodel.ArticleViewModel

class ArticleFragment : Fragment() {

    private lateinit var viewModel: ArticleViewModel
    private var _binding: FragmentArticleBinding? = null
    private val binding: FragmentArticleBinding get() = _binding!!
    private lateinit var article: Article

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        article = arguments?.getSerializable("article") as Article

        val repository = DBRepositoryImpl(NewsDB(requireContext()))
        viewModel = ArticleViewModel.ArticleViewModelProviderFactory(repository).create(ArticleViewModel::class.java)

        binding.webView.apply {
            webViewClient = WebViewClient()
            article.url?.let { loadUrl(it) }
        }

        binding.fab.setOnClickListener {
            viewModel.saveArticle(article)
            Toast.makeText(requireContext(), "artigo salvo", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}