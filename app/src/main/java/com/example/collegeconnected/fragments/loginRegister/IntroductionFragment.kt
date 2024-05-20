package com.example.collegeconnected.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.collegeconnected.activities.Dashboard
import com.example.collegeconnected.R
import com.example.collegeconnected.databinding.FragmentIntroductionBinding
import com.example.collegeconnected.viewModel.IntroductionViewModel
import com.example.collegeconnected.viewModel.IntroductionViewModel.Companion.ACCOUNT_OPTION_FRAGMENT
import com.example.collegeconnected.viewModel.IntroductionViewModel.Companion.SHOPPING_ACTIVITY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IntroductionFragment:Fragment(R.layout.fragment_introduction) {

    private lateinit var binding:FragmentIntroductionBinding
    private val viewModel by viewModels<IntroductionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIntroductionBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.navigate.collect{
                when(it){
                    SHOPPING_ACTIVITY ->{
                        Intent(requireContext(), Dashboard::class.java).also { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }

                    ACCOUNT_OPTION_FRAGMENT ->{
                        findNavController().navigate(it)
                    }
                    else -> Unit
                }
            }
        }

        binding.apply {
            buttonStart.setOnClickListener {
                viewModel.startButtonClick()
                findNavController().navigate(R.id.action_introductionFragment_to_accountOptionsFragment)
            }
        }
    }
}