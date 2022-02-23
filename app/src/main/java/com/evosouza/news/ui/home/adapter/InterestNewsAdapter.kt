package com.evosouza.news.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.evosouza.news.data.model.Article
import com.evosouza.news.data.model.HeaderTitle
import com.evosouza.news.data.model.InterestNews
import com.evosouza.news.databinding.HeaderInterestNewsBinding
import com.evosouza.news.databinding.ItemNewsBinding

class InterestNewsAdapter(
    private val listNews: List<InterestNews>,
    private val itemClickedListener: ((article: Article) -> Unit)
): RecyclerView.Adapter<InterestNewsAdapter.NewsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        val view = HeaderInterestNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HeaderViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = listNews.size

    companion object{
        private const val ARTICLE_HOLDER = 0
        private const val HEADER_HOLDER = 1
        private const val NOT_DEFINED = -1
    }
}