package com.heartsteel.heartory.service.repository

import com.heartsteel.heartory.service.api.retrofit.PrivateRetrofit
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    val privateRetrofit: PrivateRetrofit,
) {
    suspend fun getArticleList() = privateRetrofit.articleAPI.getBlogs()

    suspend fun  getArticleById(id: Int) = privateRetrofit.articleAPI.getBlogById(id)
}