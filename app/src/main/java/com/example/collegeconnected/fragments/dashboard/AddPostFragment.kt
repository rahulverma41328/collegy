package com.example.collegeconnected.fragments.dashboard

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.collegeconnected.R
import com.example.collegeconnected.activities.Dashboard
import com.example.collegeconnected.data.UserPost
import com.example.collegeconnected.databinding.FragmentAddPostBinding
import com.example.collegeconnected.util.Resources
import com.example.collegeconnected.viewModel.AddPostViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.jetbrains.annotations.TestOnly
import java.util.UUID

@AndroidEntryPoint
class AddPostFragment:Fragment(R.layout.fragment_add_post) {

    private lateinit var binding:FragmentAddPostBinding
    private lateinit var imageUri: Uri
    private val viewModel by viewModels<AddPostViewModel>()
    val PICK_IMAGE_REQUEST = 1
    private val firestoreStorage = Firebase.firestore
    private val storage = Firebase.storage.reference
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddPostBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.postImage.setOnClickListener {
            openGallery()
        }
        binding.apply {
            uploadPost.setOnClickListener {
                val title:String = binding.postTitle.text.toString().trim()
                val description:String = binding.postDescription.text.toString().trim()

                savePost { downloadUrl->
                    val userPost = UserPost(
                        UUID.randomUUID().toString(),
                        title,
                        description,
                        downloadUrl,
                        "0",
                        "0"
                    )
                    viewModel.storeDataToFirebase(userPost)
                }


            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.storeData.collect{
                when(it){
                    is Resources.Loading ->{
                        Toast.makeText(requireContext(),"Post Uploading...",Toast.LENGTH_LONG).show()
                    }
                    is Resources.Success ->{
                        Toast.makeText(requireContext(),"Post Uploaded",Toast.LENGTH_LONG).show()
                    }
                    is Resources.Error -> {
                        Toast.makeText(requireContext(),"Error uploading post",Toast.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun savePost(callback:(String)->Unit) {

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                async {
                    val id = UUID.randomUUID().toString()
                    val storageRef = storage.child("postImg/images/$id")
                    val result = storageRef.putFile(imageUri).await()
                    val downloadUrl = result.storage.downloadUrl.await().toString()
                    callback(downloadUrl)
                }.await()
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == PICK_IMAGE_REQUEST || resultCode == Activity.RESULT_OK){
            val selectedImageUri: Uri? = data?.data
            imageUri= data?.data!!
            if (selectedImageUri!=null){
                Glide.with(this).load(selectedImageUri).into(binding.showPostImg)
            }
        }
    }
}