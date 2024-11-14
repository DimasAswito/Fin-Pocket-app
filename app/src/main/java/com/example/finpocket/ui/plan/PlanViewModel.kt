package com.example.finpocket.ui.plan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlanViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Plan Fragment"
    }
    val text: LiveData<String> = _text
}