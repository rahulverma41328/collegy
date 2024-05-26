package com.example.collegeconnected.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.collegeconnected.data.PostData
import com.example.collegeconnected.data.UserPost
import com.example.collegeconnected.util.Resources
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
):ViewModel() {

    private val _showPost = MutableStateFlow<Resources<List<PostData>>>(Resources.Unspecified())
    val showPost: StateFlow<Resources<List<PostData>>> = _showPost

    fun showAllPost(){
        viewModelScope.launch {
            _showPost.emit(Resources.Loading())
        }
        firebaseFirestore.collection("post").get().
                addOnSuccessListener { result->
                    val post = result.toObjects(PostData::class.java)
                    viewModelScope.launch {
                        _showPost.emit(Resources.Success(post))
                    }
                }.addOnFailureListener {
                    viewModelScope.launch {
                        _showPost.emit(Resources.Error(it.message.toString()))
                    }
        }
    }

}