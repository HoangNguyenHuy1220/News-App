package com.example.newsapp.viewmodel

import androidx.lifecycle.*
import com.example.newsapp.models.Article
import com.example.newsapp.models.NewsResponse
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {

    private val _breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val breakingNews: LiveData<Resource<NewsResponse>>
        get() = _breakingNews

    private val _searchedNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val searchedNews: LiveData<Resource<NewsResponse>>
        get() = _searchedNews

    private val _allSavedArticles: LiveData<List<Article>> = repository.getAllArticles().asLiveData()
    val allSavedArticles: LiveData<List<Article>>
        get() = _allSavedArticles

    private var page = 1

    init {
        getBreakingNews("technology")
    }

    fun getBreakingNews(category: String) = viewModelScope.launch {
        _breakingNews.value = Resource.Loading()
        val response = repository.getBreakingNews("us", category, page)
        _breakingNews.value = handleBreakingNewsResponse(response)
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun searchNews(query: String) = viewModelScope.launch {
        _searchedNews.value = Resource.Loading()
        val response = repository.searchNews(query, page)
        _searchedNews.value = handleSearchNewsResponse(response)
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun getNewspaper(date: String) = viewModelScope.launch {
        _searchedNews.value = Resource.Loading()
        val response = repository.getNewspaper(date, page)
        _searchedNews.value = handleGetNewspaperResponse(response)
    }

    private fun handleGetNewspaperResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        repository.insert(article)
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        repository.delete(article)
    }

    fun isArticleSaved(article: Article): Boolean {
        return if (_allSavedArticles.value.isNullOrEmpty()) {
            false
        } else {
            allSavedArticles.value!!.any { it.title == article.title }
        }
    }
}