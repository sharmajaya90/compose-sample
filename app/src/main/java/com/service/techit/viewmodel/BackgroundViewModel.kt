package com.service.techit.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.service.techit.extensions.colorStringToComposeColor
import com.service.techit.model.TechITLanguage
import com.service.techit.utils.firebase.FirestoreHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class BackgroundViewModel : ViewModel() {
    private val _language = MutableStateFlow(TechITLanguage())
    val language: StateFlow<TechITLanguage> = _language
    private val _backgroundColor = MutableStateFlow(Color.White)
    val backgroundColor: StateFlow<Color> = _backgroundColor

    private val _topAppBarColor = MutableStateFlow(Color.White)
    val topAppBarColor: StateFlow<Color> = _topAppBarColor
    private val _headerColor = MutableStateFlow(Color.Black)
    val headerColor: StateFlow<Color> = _headerColor

    private val _contentBGColor = MutableStateFlow(Color.White)
    val contentBGColor: StateFlow<Color> = _contentBGColor

    private val _contentColor = MutableStateFlow(Color.Black)
    val contentColor: StateFlow<Color> = _contentColor

    private val _imageUrl = MutableStateFlow(arrayListOf<String>())
    val imageUrl: StateFlow<ArrayList<String>> = _imageUrl

    init {
        fetchUpdatedTechITStyle()
    }


    private fun fetchUpdatedTechITStyle() {
        viewModelScope.launch {
            val styles = FirestoreHelper.provideTechITStyles()
            _backgroundColor.value = styles?.backgroundColor?.colorStringToComposeColor()?: Color.White
            _topAppBarColor.value = styles?.topAppBarColor?.colorStringToComposeColor()?: Color.Black
            _contentColor.value = styles?.contentColor?.colorStringToComposeColor()?: Color.Black
            _contentBGColor.value = styles?.contentBGColor?.colorStringToComposeColor()?: Color.White
            _headerColor.value = styles?.headerColor?.colorStringToComposeColor()?: Color.White
            _imageUrl.value = styles?.landingImages?: arrayListOf()
            _language.value = styles?.language?:TechITLanguage()
        }
    }

}