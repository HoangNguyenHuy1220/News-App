package com.example.newsapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.databinding.ItemCategoryNewsBinding

class CategoryAdapter(
    private val context: Context,
    private val onClick: (String) -> Unit)
    : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private val categories = context.resources.getStringArray(R.array.category_array)

    class ViewHolder(val binding: ItemCategoryNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: String) {
            binding.apply {
                category = item
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryNewsBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(categories[position])
        holder.itemView.setOnClickListener {
            onClick(categories[position])
        }
    }

    override fun getItemCount() = categories.size
}