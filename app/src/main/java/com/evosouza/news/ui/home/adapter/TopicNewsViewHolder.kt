package com.evosouza.news.ui.home.adapter

import androidx.recyclerview.widget.RecyclerView
import com.evosouza.news.data.model.Article
import com.evosouza.news.data.model.NewsResponse
import com.evosouza.news.databinding.ItemNewsBinding

class TopicNewsViewHolder(
    private val itemNewsBinding: ItemNewsBinding,
    private val itemClickedListener: (article: Article) -> Unit
): RecyclerView.ViewHolder(itemNewsBinding.root) {

    fun bind(newsResponse: NewsResponse){

    }

}