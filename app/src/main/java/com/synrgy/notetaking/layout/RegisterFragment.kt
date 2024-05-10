package com.synrgy.notetaking.layout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.synrgy.notetaking.UserViewModel
import com.synrgy.notetaking.ViewModelFactory
import com.synrgy.notetaking.data.database.User
import com.synrgy.notetaking.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _fragmentRegisterBinding: FragmentRegisterBinding? = null
    private val fragmentRegisterBinding get() = _fragmentRegisterBinding!!

    private val viewModel by viewModels<UserViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _fragmentRegisterBinding =  FragmentRegisterBinding.inflate(inflater, container, false)
        return fragmentRegisterBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = fragmentRegisterBinding.etUsername.text.toString()
        val email = fragmentRegisterBinding.etEmail.text.toString()
        val password = fragmentRegisterBinding.etPassword.text.toString()
        val passwordConfirm = fragmentRegisterBinding.etConfirmPassword.text.toString()

        actionRegister(username, email, password)
    }



    private fun actionRegister(
        username: String,
        email: String,
        password: String,
    ){
        fragmentRegisterBinding.actionRegister.setOnClickListener {
            viewModel.register(
                User(email = email, username = username, password = password)
            )
        }
    }

    private fun checkPassword(password: String, passwordConfirm: String){
        if (password != passwordConfirm){
            fragmentRegisterBinding.confirmPassword.error = "Password not match"
            fragmentRegisterBinding.confirmPassword.requestFocus()
            fragmentRegisterBinding.actionRegister.isEnabled = false
        }
    }

}