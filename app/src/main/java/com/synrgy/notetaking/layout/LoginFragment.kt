package com.synrgy.notetaking.layout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.synrgy.notetaking.R
import com.synrgy.notetaking.UserViewModel
import com.synrgy.notetaking.ViewModelFactory
import com.synrgy.notetaking.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _fragmentLoginBinding: FragmentLoginBinding? = null
    private val fragmentLoginBinding get() = _fragmentLoginBinding!!

    private val viewModel by viewModels<UserViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _fragmentLoginBinding =  FragmentLoginBinding.inflate(inflater, container, false)
        return fragmentLoginBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toRegisterPage()
        actionLogin()
    }

    private fun toRegisterPage() {
        fragmentLoginBinding.register.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun actionLogin(){
        fragmentLoginBinding.actionLogin.setOnClickListener {
            val email = fragmentLoginBinding.etEmail.text.toString()
            val password = fragmentLoginBinding.etPassword.text.toString()
            viewModel.login(email, password)
        }
    }
}