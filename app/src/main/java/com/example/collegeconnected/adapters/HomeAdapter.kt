package com.example.collegeconnected.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.collegeconnected.data.PostData
import com.example.collegeconnected.databinding.PostCardBinding

class HomeAdapter:RecyclerView.Adapter<HomeAdapter.HomeAdapterViewHolder>() {
    inner class HomeAdapterViewHolder(private val binding:PostCardBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(postData: PostData){
            binding.apply {
              //  Glide.with(itemView).load(postData.communityImage).into(communityImage)
             //   communityUsername.text = postData.communityName
              //  userUsername.text = postData.postUsername

                Glide.with(itemView).load(postData.postImg).into(userPostImage)
                title.text = postData.title
                postDescription.text = postData.postDescription
                postUpVote.text = postData.postLike.toString()
                postComment.text = postData.postComment.toString()
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<PostData>() {
        override fun areItemsTheSame(oldItem: PostData, newItem: PostData): Boolean {
            return oldItem.postId == newItem.postId
        }

        override fun areContentsTheSame(oldItem: PostData, newItem: PostData): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,diffCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapterViewHolder {
        return HomeAdapterViewHolder(
            PostCardBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: HomeAdapterViewHolder, position: Int) {
        val post = differ.currentList[position]
        holder.bind(post)
    }
}