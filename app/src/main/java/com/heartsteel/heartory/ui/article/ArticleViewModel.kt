package com.heartsteel.heartory.ui.article

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.healthcarecomp.base.BaseViewModel
import com.google.gson.Gson
import com.heartsteel.heartory.common.util.Resource
import com.heartsteel.heartory.service.model.domain.Article
import com.heartsteel.heartory.service.model.domain.ArticleList
import com.heartsteel.heartory.service.model.response.ResponseObject
import com.heartsteel.heartory.service.repository.ArticleRepository
import com.heartsteel.heartory.service.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel  @Inject constructor(
    override val userRepository: UserRepository,
    val ArticleRepository: ArticleRepository
): BaseViewModel(userRepository) {
    val articleList = MutableLiveData<Resource<MutableList<ArticleList>>>()
    val articleDetail = MutableLiveData<Resource<Article>>()

    fun getArticleList(){
        articleList.value = Resource.Loading()
        viewModelScope.launch {
            val res = ArticleRepository.getArticleList()
            if(res.isSuccessful){
                articleList.value = Resource.Success(res.body()?.data!!)
            }
            else{
                val errorBody = res.errorBody()
                val errorMsg = Gson().fromJson(errorBody?.string(), ResponseObject::class.java).message
                articleList.value = Resource.Error(errorMsg)
            }
        }
    }

    fun getArticleById(id: String){
        articleDetail.value = Resource.Loading()
        viewModelScope.launch {
            val res = ArticleRepository.getArticleById(id)
            if(res.isSuccessful){
                articleDetail.value = Resource.Success(res.body()?.data!!)
            }
            else{
                val errorBody = res.errorBody()
                val errorMsg = Gson().fromJson(errorBody?.string(), ResponseObject::class.java).message
                articleDetail.value = Resource.Error(errorMsg)
            }
        }
    }
}