package com.example.collegeconnected.viewModel

import androidx.lifecycle.ViewModel
import com.example.collegeconnected.data.User
import com.example.collegeconnected.util.Constants.USER_COLLECTION
import com.example.collegeconnected.util.RegisterFieldState
import com.example.collegeconnected.util.RegisterValidation
import com.example.collegeconnected.util.Resources
import com.example.collegeconnected.util.validateEmail
import com.example.collegeconnected.util.validatePassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth:FirebaseAuth,
    private val db:FirebaseFirestore
):ViewModel() {

    private val _register = MutableStateFlow<Resources<User>>(Resources.Unspecified())
    val register:Flow<Resources<User>> = _register

    private val _validation = Channel<RegisterFieldState>()
    val validation = _validation.receiveAsFlow()

    fun createAccountWithEmailAndPassword(user: User,password:String){
        if (checkValidation(user,password)){

            runBlocking {
                _register.emit(Resources.Loading())
            }
            firebaseAuth.createUserWithEmailAndPassword(user.email,password)
                .addOnSuccessListener {
                    it.user?.let {
                        saveUserInfo(it.uid,user)
                    }
                }.addOnFailureListener {
                    _register.value = Resources.Error(it.message.toString())
                }
    }else{
        val registerFieldState = RegisterFieldState(
            validateEmail(user.email),
            validatePassword(password)
        )
            runBlocking {
                _validation.send(registerFieldState)
            }
        }
}

    private fun saveUserInfo(userUid: String, user: User) {
        db.collection(USER_COLLECTION)
            .document(userUid)
            .set(user)
            .addOnSuccessListener {
                _register.value = Resources.Success(user)
            }.addOnFailureListener {
                _register.value = Resources.Error(it.message.toString())
            }
    }

    private fun checkValidation(user: User, password: String): Boolean {
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(password)
        val shouldRegister = emailValidation is RegisterValidation.Success &&
                passwordValidation is RegisterValidation.Success

        return shouldRegister
    }
    }
