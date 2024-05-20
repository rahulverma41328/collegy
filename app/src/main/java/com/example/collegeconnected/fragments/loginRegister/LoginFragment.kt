package com.example.collegeconnected.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.collegeconnected.activities.Dashboard
import com.example.collegeconnected.R
import com.example.collegeconnected.databinding.FragmentLoginBinding
import com.example.collegeconnected.dialog.setupBottomSheetDialog
import com.example.collegeconnected.util.Resources
import com.example.collegeconnected.viewModel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment:Fragment(R.layout.fragment_login) {

    private lateinit var binding:FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            buttonLoginLogin.setOnClickListener {
                val email = edEmailLogin.text.toString().trim()
                val password = edPasswordLogin.text.toString()

                if(email.isEmpty() or password.isEmpty()) {
                    Toast.makeText(requireContext(), "field can't be empty", Toast.LENGTH_LONG)
                        .show()
                }
                else {
                    viewModel.login(email, password)
                }
            }
        }
        binding.tvForgotPasswordLogin.setOnClickListener {
            setupBottomSheetDialog{email ->
                viewModel.resetPassword(email)
            }

        }
        lifecycleScope.launchWhenCreated {
            viewModel.login.collect{
                when(it){
                    is Resources.Loading ->{
                        binding.buttonLoginLogin.startAnimation()
                    }
                    is Resources.Success ->{
                        binding.buttonLoginLogin.revertAnimation()
                        Intent(requireActivity(), Dashboard::class.java).also { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }
                    is Resources.Error -> {
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                        binding.buttonLoginLogin.revertAnimation()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.resetPassword.collect{
                when(it){
                    is Resources.Loading ->{

                    }
                    is  Resources.Success ->{
                        Snackbar.make(requireView(),"Reset link was sent to your email",Snackbar.LENGTH_LONG).show()
                    }
                    is Resources.Error ->{
                        Snackbar.make(requireView(),"Error",Snackbar.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }
    }
}