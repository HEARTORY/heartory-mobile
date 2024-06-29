package com.heartsteel.heartory.service.api
import com.heartsteel.heartory.service.model.domain.Article
import com.heartsteel.heartory.service.model.domain.ArticleList
import com.heartsteel.heartory.service.model.response.ResponseObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ArticleApi {
    @GET("blog")
    suspend fun getBlogs(): Response<ResponseObject<MutableList<ArticleList>>>

    @GET("blog/{id}")
    suspend fun getBlogById(@Path("id") id: Int): Response<ResponseObject<Article>>
}