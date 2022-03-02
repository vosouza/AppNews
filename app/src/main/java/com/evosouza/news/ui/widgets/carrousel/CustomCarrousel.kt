package com.evosouza.news.ui.widgets.carrousel

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewFlipper
import com.bumptech.glide.Glide
import com.evosouza.news.R
import com.evosouza.news.data.model.Article
import com.evosouza.news.data.model.Slide

class CustomCarrousel(context: Context?, attrs: AttributeSet?) : ViewFlipper(context, attrs) {

    private var list = mutableListOf<Article>()

    fun setList(list: MutableList<Article>) {
        this.list = list
    }

    private var itemClick: ((article: Article) -> Unit)? = null

    fun setClickListener(itemClick: (article: Article) -> Unit){
        this.itemClick = itemClick
    }

    fun setupCarrousel(){
        val flipperList = mutableListOf<Slide>()
        list.forEach { article ->
            flipperList.add(Slide(
                article.title ?: "",
                article.urlToImage ?: ""
            ))
        }

        val inflater =  context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        this.setInAnimation(context, android.R.anim.slide_in_left)
        this.setOutAnimation(context, android.R.anim.slide_out_right)

        for( i in flipperList.indices){
            val view: View = inflater.inflate(R.layout.item_slide, this, false)

            val title = view.findViewById<TextView>(R.id.title)
            title.text = flipperList[i].title

            val image = view.findViewById<ImageView>(R.id.background)
            Glide.with(context).load(flipperList[i].imageUrl).into(image)

            image.setOnClickListener {
                itemClick?.invoke(list[i])
            }

            title.setOnClickListener {
                itemClick?.invoke(list[i])
            }

            this.addView(view, i)
        }
    }
}