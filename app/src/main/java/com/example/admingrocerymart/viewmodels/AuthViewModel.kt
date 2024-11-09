package com.example.admingrocerymart.viewmodels

import androidx.lifecycle.ViewModel
import com.example.admingrocerymart.mesg
import kotlinx.coroutines.flow.MutableStateFlow

class AuthViewModel:ViewModel() {

    private val _currentuser= MutableStateFlow(false)
    val currentuser=_currentuser

    init{
        mesg.getAuthInstance().currentUser?.let {
            _currentuser.value=true
        }
    }
}