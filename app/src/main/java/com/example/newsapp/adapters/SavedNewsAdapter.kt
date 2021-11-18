package com.example.newsapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.databinding.ItemArticlePreviewBinding
import com.example.newsapp.models.Article

class SavedNewsAdapter(private val onClick: (Article) -> Unit)
    : ListAdapter<Article, SavedNewsAdapter.ArticleViewHolder>(diffCallback) {

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ArticleViewHolder(private val binding: ItemArticlePreviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(currentArticle: Article) {
            binding.apply {
                article = currentArticle
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticlePreviewBinding.inflate(LayoutInflater.from(parent.context))
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = getItem(position)
        holder.onBind(article)
        holder.itemView.setOnClickListener { onClick(article) }
    }
}

