package com.service.techit.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.service.techit.model.database.dao.QAInfoDao
import com.service.techit.viewmodel.QAInfoViewModel
import retrofit2.Retrofit

class TechITViewModelFactory(
    private val retrofit: Retrofit,
    private val qaInfoDao: QAInfoDao
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(QAInfoViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return QAInfoViewModel(retrofit, qaInfoDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }