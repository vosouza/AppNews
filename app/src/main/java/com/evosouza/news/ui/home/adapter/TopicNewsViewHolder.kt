package com.evosouza.news.ui.home.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.evosouza.news.data.model.Article
import com.evosouza.news.data.model.InterestNews
import com.evosouza.news.databinding.HeaderInterestNewsBinding

class TopicNewsViewHolder(
    private val itemNewsBinding: HeaderInterestNewsBinding,
    private val itemClickedListener: (article: Article) -> Unit
): RecyclerView.ViewHolder(itemNewsBinding.root) {

    fun bind(newsResponse: InterestNews){
        itemNewsBinding.title.text = newsResponse.title
        with(itemNewsBinding.newsRecycler){
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = VerticalItemNewsAdapter(newsResponse.news.articles){
                itemClickedListener.invoke(it)
            }
        }
    }

}