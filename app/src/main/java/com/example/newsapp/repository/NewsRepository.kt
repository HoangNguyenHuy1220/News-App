package com.example.newsapp.repository

import com.example.newsapp.api.RetrofitInstance
import com.example.newsapp.database.ArticleDatabase
import com.example.newsapp.models.Article

class NewsRepository(private val db: ArticleDatabase) {
    suspend fun getBreakingNews(countryCode: String, category: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, category, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    suspend fun getNewspaper(date: String, pageNumber: Int) =
        RetrofitInstance.api.getNewspapers(date, pageNumber)

    suspend fun insert(article: Article) = db.articleDao.insert(article)

    suspend fun delete(article: Article) = db.articleDao.delete(article)

    fun getAllArticles() = db.articleDao.getAllArticles()
}