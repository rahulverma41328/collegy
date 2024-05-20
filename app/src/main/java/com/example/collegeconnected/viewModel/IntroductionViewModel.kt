package com.example.collegeconnected.viewModel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.collegeconnected.R
import com.example.collegeconnected.util.Constants.INTRODUCTION_KEY
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroductionViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val firebaseAuth: FirebaseAuth,
): ViewModel() {

    private val _navigate = MutableStateFlow(0)
    val navigate:StateFlow<Int> = _navigate
    private val user = firebaseAuth.currentUser

    companion object{
        const val SHOPPING_ACTIVITY = 23
        val ACCOUNT_OPTION_FRAGMENT = R.id.action_introductionFragment_to_accountOptionsFragment
    }
    init {
        val isButtonClicked = sharedPreferences.getBoolean(INTRODUCTION_KEY,false)
        val user = firebaseAuth.currentUser

        if (user!=null){
            viewModelScope.launch {
                _navigate.emit(SHOPPING_ACTIVITY)
                Log.d("TAG","user is null")
            }
        }else if (isButtonClicked){
            viewModelScope.launch {
                _navigate.emit(ACCOUNT_OPTION_FRAGMENT)
            }
        }
        else{
            Unit
        }
    }
    fun startButtonClick() {
        if (user != null) {
            sharedPreferences.edit().putBoolean(INTRODUCTION_KEY, true).apply()
        }
    }

}