package com.example.collegeconnected.viewModel

import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.collegeconnected.data.PostData
import com.example.collegeconnected.data.UserPost
import com.example.collegeconnected.util.Resources
import com.google.api.Context
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPostViewModel @Inject constructor(
    private val firebaseAuth:FirebaseAuth,
    private val databaseFirestore: FirebaseFirestore = Firebase.firestore,
):ViewModel(){
    private val _storeData = MutableSharedFlow<Resources<UserPost>>()
    val storeData = _storeData.asSharedFlow()


    fun storeDataToFirebase(data:UserPost){
        val storage = Firebase.storage.reference

        try {
            databaseFirestore.collection("post").add(data).addOnSuccessListener {

                viewModelScope.launch {
                    _storeData.emit(Resources.Success(data))
                }
            }.addOnFailureListener{
                viewModelScope.launch {
                    _storeData.emit(Resources.Error(it.message.toString()))
                }
            }

        }catch (e:Exception){
            viewModelScope.launch {
                _storeData.emit(Resources.Error(e.message?:"Firebase error"))
            }
        }
    }
}