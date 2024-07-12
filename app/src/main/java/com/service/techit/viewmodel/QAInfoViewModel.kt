package com.service.techit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.service.techit.model.QAInfo
import com.service.techit.model.database.dao.QAInfoDao
import com.service.techit.model.database.entity.QAInfoEntity
import com.service.techit.utils.NetworkApiCallInterface
import com.service.techit.utils.firebase.FirestoreHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import javax.inject.Inject


class QAInfoViewModel @Inject constructor(private val retrofit: Retrofit, private val qaInfoDao: QAInfoDao) : ViewModel() {
    var _isLoader = MutableStateFlow(false)
    val isProgress: StateFlow<Boolean> get() = _isLoader
    private val _firestoreQAInfoList = MutableStateFlow<List<QAInfo>>(emptyList())
    val firestoreQAInfoList: StateFlow<List<QAInfo>> get() = _firestoreQAInfoList

    private val _qaInfoList = MutableStateFlow<List<QAInfoEntity>>(emptyList())
    val qaInfoLists: StateFlow<List<QAInfoEntity>> get() = _qaInfoList
    val _addedSuccess = MutableStateFlow(false)
    val addedSuccess: StateFlow<Boolean> get() = _addedSuccess


    fun provideFireStoreQAList() {
        _isLoader.value = true
        viewModelScope.launch {
            _firestoreQAInfoList.value = FirestoreHelper.getQAInfoList()
            _isLoader.value = false
        }
    }


    fun fetchQAInfoByType(type:String) {
        viewModelScope.launch {
            _isLoader.value = true
            qaInfoDao.getQAByType(type).collect{
                _qaInfoList.value = it
            }
        }
    }

    fun addQAInfoByType(qaInfoEntity: QAInfoEntity) {
        viewModelScope.launch {
            _isLoader.value = true
             if (qaInfoDao.insertQAInfo(qaInfoEntity)>0){
                 _addedSuccess.value = true
             }else{
                 _addedSuccess.value = false
             }
        }
    }

    fun fetchQAInfoList(url: String) {
        viewModelScope.launch {
            _isLoader.value = true
            retrofit.create(NetworkApiCallInterface::class.java).makeHttpGetRequestString(url)
        }
    }
}