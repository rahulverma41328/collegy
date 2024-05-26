package com.example.collegeconnected.fragments.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collegeconnected.R
import com.example.collegeconnected.adapters.HomeAdapter
import com.example.collegeconnected.databinding.FragmentHomeBinding
import com.example.collegeconnected.util.Resources
import com.example.collegeconnected.viewModel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
private val TAG = "Main Fragment"
@AndroidEntryPoint
class HomeFragment:Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var homeAdapter:HomeAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showPost()

        lifecycleScope.launchWhenCreated {
            viewModel.showPost.collectLatest {
                    when(it){
                        is Resources.Loading ->{
                          //  showLoading()
                        }
                        is Resources.Success ->{
                            homeAdapter.differ.submitList(it.data)
                        //    hideLoading()
                        }
                        is Resources.Error ->{
                          //  hideLoading()
                            Log.e(TAG,it.message.toString())
                            Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                        }
                        else -> Unit
                    }
            }
        }
    }

    private fun showPost() {
        homeAdapter = HomeAdapter()
        binding.container.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = homeAdapter
        }
    }
}