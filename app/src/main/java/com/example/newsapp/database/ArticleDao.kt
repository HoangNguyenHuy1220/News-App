package com.example.newsapp.database

import androidx.room.*
import com.example.newsapp.models.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article)

    @Delete
    suspend fun delete(article: Article)

    @Query("SELECT * FROM articles")
    fun getAllArticles(): Flow<List<Article>>
}