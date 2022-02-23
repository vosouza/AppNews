package com.evosouza.news.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.evosouza.news.data.model.Article
import com.evosouza.news.data.model.InterestNews
import com.evosouza.news.databinding.HeaderInterestNewsBinding
import com.evosouza.news.databinding.ItemNewsBinding

class InterestNewsAdapter(
    private val listNews: List<InterestNews>,
    private val itemClickedListener: ((article: Article) -> Unit)
): RecyclerView.Adapter<InterestNewsAdapter.NewsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
//        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
//        return AdapterViewHolder(itemView)
        val itemBinding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdapterViewHolder(itemBinding, itemClickedListener)
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        holder.bind(listNews[position])
        holder.itemView
    }

    override fun getItemCount(): Int = listNews.size


    abstract class NewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        abstract fun bind(position: Int)
    }

    inner class AdapterViewHolder(
        private val itemNewsBinding: ItemNewsBinding,
        private val itemClickedListener: (article: Article) -> Unit
    ): NewsHolder(itemNewsBinding.root) {

        override fun bind(position: Int){
            val article = listNews[position].news.articles[position]

            itemNewsBinding.run {
                textTitle.text = article.title
                textSource.text = article.source?.name
                textDescription.text = article.description
                textPublishedAt.text = article.publishedAt

                Glide.with(itemView)
                    .load(article.urlToImage)
                    .into(imageNews)

                itemView.setOnClickListener{
                    itemClickedListener.invoke(article)
                }
            }

        }

    }

    class HeaderViewHolder(
        private val itemNewsBinding: HeaderInterestNewsBinding,
    ): NewsHolder(itemNewsBinding.root){
        override fun bind(position: Int){
//            itemNewsBinding.title.text = title
        }
    }
}