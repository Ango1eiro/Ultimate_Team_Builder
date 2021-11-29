package com.example.anitultimateteambuilder.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.anitultimateteambuilder.domain.NewsApiArticle
import com.example.anitultimateteambuilder.domain.Player

class HomeViewModel : ViewModel() {

    private val _news = MutableLiveData<List<NewsApiArticle>>()
    val news: LiveData<List<NewsApiArticle>>
        get() = _news

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    var cPage : Int = 1

    fun updateNews(pNews:List<NewsApiArticle>){
        val cNews = news.value
        if (cNews == null) {
            _news.postValue(pNews)
        } else
        {
            _news.postValue(cNews+pNews)
        }
    }

    fun setLoadingState(status: Boolean){
        _isLoading.postValue(status)
    }

}