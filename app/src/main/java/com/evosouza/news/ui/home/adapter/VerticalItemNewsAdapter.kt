package com.evosouza.news.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.evosouza.news.data.model.Article
import com.evosouza.news.databinding.ItemNewsVerticalBinding

class VerticalItemNewsAdapter(
    private val articles: List<Article>,
    private val itemClicked : ((article: Article)->Unit)
) : RecyclerView.Adapter<VerticalItemNewsAdapter.VerticalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerticalViewHolder {
        val itemView = ItemNewsVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VerticalViewHolder(itemView, itemClicked)
    }

    override fun onBindViewHolder(holder: VerticalViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount(): Int = articles.count()

    class VerticalViewHolder(
        private val itemViewNews: ItemNewsVerticalBinding,
        private val itemClicked : (article: Article)->Unit
    ) : RecyclerView.ViewHolder(itemViewNews.root) {

        fun bind(article: Article){
            itemView.run {
                Glide.with(itemView)
                    .load(article.urlToImage)
                    .into(itemViewNews.imageNews)

                itemViewNews.textTitle.text = article.title

                setOnClickListener {
                    itemClicked.invoke(article)
                }
            }
        }

    }
}
