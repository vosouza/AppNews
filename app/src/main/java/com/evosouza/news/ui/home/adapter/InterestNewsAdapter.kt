package com.evosouza.news.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.evosouza.news.data.model.Article
import com.evosouza.news.data.model.HeaderTitle
import com.evosouza.news.data.model.SubjectAdapterModel
import com.evosouza.news.databinding.HeaderInterestNewsBinding
import com.evosouza.news.databinding.ItemNewsBinding

class InterestNewsAdapter(
    private val listNews: List<SubjectAdapterModel>,
    private val itemClickedListener: ((article: Article) -> Unit)
): RecyclerView.Adapter<InterestNewsAdapter.NewsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        return when(viewType){
            HEADER_HOLDER ->{
                val view = HeaderInterestNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HeaderViewHolder(view)
            }
            else -> {
                val view = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                AdapterViewHolder(view, itemClickedListener)
            }
        }
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = listNews.size


    override fun getItemViewType(position: Int): Int {
        return when(listNews[position]){
            is HeaderTitle -> HEADER_HOLDER
            is Article -> ARTICLE_HOLDER
            else -> NOT_DEFINED
        }
    }

    abstract class NewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        abstract fun bind(position: Int)
    }

    inner class AdapterViewHolder(
        private val itemNewsBinding: ItemNewsBinding,
        private val itemClickedListener: (article: Article) -> Unit
    ): NewsHolder(itemNewsBinding.root) {

        override fun bind(position: Int){
            val article = listNews[position] as Article

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

    inner class HeaderViewHolder(
        private val itemNewsBinding: HeaderInterestNewsBinding,
    ): NewsHolder(itemNewsBinding.root){
        override fun bind(position: Int){
            val header = listNews[position] as HeaderTitle
            itemNewsBinding.title.text = header.title
        }
    }

    companion object{
        private const val ARTICLE_HOLDER = 0
        private const val HEADER_HOLDER = 1
        private const val NOT_DEFINED = -1
    }
}