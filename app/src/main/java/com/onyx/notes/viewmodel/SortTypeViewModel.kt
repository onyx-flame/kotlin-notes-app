package com.onyx.notes.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SortTypeViewModel : ViewModel() {

    private val sortType = MutableLiveData<Boolean>(true)

    fun setSortByDate() {
        sortType.value = true
    }

    fun setSortByName() {
        sortType.value = false
    }

    fun isSortByDate(): Boolean {
        return (sortType.value == true)
    }

    fun isSortByName(): Boolean {
        return (sortType.value == false)
    }

}