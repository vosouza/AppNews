package com.evosouza.news.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.evosouza.news.data.model.Article
import com.evosouza.news.data.model.InterestNews
import com.evosouza.news.databinding.HeaderInterestNewsBinding

class InterestNewsAdapter(
    private val listNews: List<InterestNews>,
    private val changeInterest: () -> Unit,
    private val itemClickedListener: ((article: Article) -> Unit),
) : RecyclerView.Adapter<InterestNewsAdapter.TopicNewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicNewsViewHolder {
        val view =
            HeaderInterestNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TopicNewsViewHolder(view, itemClickedListener, changeInterest)
    }

    override fun onBindViewHolder(holder: TopicNewsViewHolder, position: Int) {
        holder.bind(listNews[position])
    }

    override fun getItemCount(): Int = listNews.size

    class TopicNewsViewHolder(
        private val itemNewsBinding: HeaderInterestNewsBinding,
        private val itemClickedListener: (article: Article) -> Unit,
        private val changeInterest: () -> Unit,
    ) : RecyclerView.ViewHolder(itemNewsBinding.root) {

        fun bind(newsResponse: InterestNews) {
            if (newsResponse.isInterestNews) {
                setupButton()
            } else {
                setupItem(newsResponse)
            }
        }

        fun setupButton() {
            itemNewsBinding.newsRecycler.visibility = View.GONE
            itemNewsBinding.title.visibility = View.GONE
            itemNewsBinding.btnChangeInterests.visibility = View.VISIBLE
            itemNewsBinding.btnChangeInterests.setOnClickListener {
                changeInterest()
            }
        }

        fun setupItem(newsResponse: InterestNews) {
            itemNewsBinding.title.text = newsResponse.title
            with(itemNewsBinding.newsRecycler) {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                setHasFixedSize(true)
                adapter = VerticalItemNewsAdapter(newsResponse.news.articles) {
                    itemClickedListener.invoke(it)
                }
            }
        }

    }
}