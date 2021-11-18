package com.example.newsapp.ui.fragment

import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.adapters.SavedNewsAdapter
import com.example.newsapp.models.Article
import com.example.newsapp.models.NewsResponse
import com.example.newsapp.utils.Resource

@BindingAdapter("imgUrl")
fun bindImgUrl(imageView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        Glide.with(imageView)
            .load(imgUrl)
            .into(imageView)
    }
}

// Breaking news list
@BindingAdapter("newsList")
fun bindBreakingNewsRecycler(recyclerView: RecyclerView, data: List<Article>?) {
    data?.let {
        val adapter = recyclerView.adapter as NewsAdapter
        adapter.submitList(data)
    }
}

// Saved news list
@BindingAdapter("savedList")
fun bindSavedNewsRecycler(recyclerView: RecyclerView, data: List<Article>?) {
    data?.let {
        val adapter = recyclerView.adapter as SavedNewsAdapter
        adapter.submitList(data)
    }
}

@BindingAdapter("newsApiStatus")
fun bindStatus(imageView: ImageView, status: Resource<NewsResponse>) {
    when (status) {
        is Resource.Loading -> {
            imageView.visibility = View.VISIBLE
            imageView.setImageResource(R.drawable.loading_animation)
        }
        is Resource.Error -> {
            imageView.visibility = View.VISIBLE
            imageView.setImageResource(R.drawable.ic_connection_error)
        }
        else -> View.GONE
    }
}

@BindingAdapter("loadUrl")
fun bindWebUrl(webView: WebView, webUrl: String?) {
    webUrl?.let {
        webView.webViewClient = WebViewClient()
        webView.loadUrl(it)
    }
}