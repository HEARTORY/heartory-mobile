package com.heartsteel.heartory.service.model.domain

data class ArticleList (
    val id: Int? = null,
    val blogType: String? = null,
    val blogs : List<Article>
)

