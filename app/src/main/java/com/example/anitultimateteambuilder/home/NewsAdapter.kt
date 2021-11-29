package com.example.anitultimateteambuilder.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.anitultimateteambuilder.R
import com.example.anitultimateteambuilder.domain.NewsApiArticle
import com.squareup.picasso.Picasso


class NewsAdapter(
    private val context: Context?,
    var listOfNews: List<NewsApiArticle>,
    private val resource: Int,
    private val action: () -> Unit
) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private val differ: AsyncListDiffer<NewsApiArticle> = AsyncListDiffer(this, DiffCallback())

    class MyDiffCallback(val newPersons: List<NewsApiArticle>,val oldPersons: List<NewsApiArticle>) :
        DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldPersons.size
        }

        override fun getNewListSize(): Int {
            return newPersons.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldPersons[oldItemPosition].url === newPersons[newItemPosition].url
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldPersons[oldItemPosition].equals(newPersons[newItemPosition])
        }

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            //you can return particular field for changed item.
            return super.getChangePayload(oldItemPosition, newItemPosition)
        }

    }

    private class DiffCallback : DiffUtil.ItemCallback<NewsApiArticle>() {

        override fun areItemsTheSame(oldItem: NewsApiArticle, newItem: NewsApiArticle) =
            oldItem.url == newItem.url

        override fun areContentsTheSame(oldItem: NewsApiArticle, newItem: NewsApiArticle) =
            oldItem == newItem
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        var tvArticleTitle: TextView = view.findViewById(R.id.tvArticleTitle)
        var tvArticleDescription: TextView = view.findViewById(R.id.tvArticleDescription)
        var cvArticle: CardView = view.findViewById(R.id.cvArticle)
        var ivArticle: ImageView = view.findViewById(R.id.imageViewArticle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create a new view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(resource, parent, false)

        return ViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listOfNews[position]

        holder.tvArticleTitle.text = item.title
        holder.tvArticleDescription.text = item.description
//        holder.ivArticle.setImageBitmap(item.urlToImage)

        Picasso.get().load(item.urlToImage).into(holder.ivArticle);
        holder.cvArticle.setOnClickListener(){
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(item.url)
            startActivity(context!!,openURL,null)
        }
    }

    override fun getItemCount() = listOfNews.size

}

