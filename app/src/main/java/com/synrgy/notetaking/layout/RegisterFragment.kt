package com.synrgy.notetaking.layout

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.synrgy.notetaking.State
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
        _fragmentRegisterBinding = FragmentRegisterBinding.inflate(inflater, container, false)
        return fragmentRegisterBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        actionRegister()
        checkConfirmationPassword()
    }

    private fun actionRegister() {
        fragmentRegisterBinding.actionRegister.setOnClickListener {
            val username = fragmentRegisterBinding.etUsername.text.toString()
            val email = fragmentRegisterBinding.etEmail.text.toString()
            val password = fragmentRegisterBinding.etPassword.text.toString()
            val passwordConfirm = fragmentRegisterBinding.etConfirmPassword.text.toString()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showToast("Please fill all fields")
            } else {
                viewModel.register(
                    User(email = email, username = username, password = password)
                ).observe(viewLifecycleOwner) {
                    when (it) {
                        is State.Success -> {
                            showToast("Register Success")
                            findNavController().popBackStack()
                        }

                        is State.Error -> showToast(it.error)
                    }
                }
            }
        }
    }

    private fun checkConfirmationPassword(){
        fragmentRegisterBinding.etPassword.addTextChangedListener(passwordTextWatcher)
        fragmentRegisterBinding.etConfirmPassword.addTextChangedListener(passwordTextWatcher)
    }

    private val passwordTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            checkPassword(s.toString(), fragmentRegisterBinding.etConfirmPassword.text.toString())
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            checkPassword(s.toString(), fragmentRegisterBinding.etConfirmPassword.text.toString())
        }

        override fun afterTextChanged(s: Editable?) {
            checkPassword(s.toString(), fragmentRegisterBinding.etConfirmPassword.text.toString())
        }
    }

    private fun checkPassword(password: String, passwordConfirm: String) {
        if (passwordConfirm != password) {
            fragmentRegisterBinding.confirmPassword.error = "Password not match"
            fragmentRegisterBinding.actionRegister.isEnabled = false
        } else {
            fragmentRegisterBinding.confirmPassword.error = null
            fragmentRegisterBinding.actionRegister.isEnabled = true
        }
    }

    private fun showToast(mssg: String?) {
        Toast.makeText(requireContext(), mssg, Toast.LENGTH_LONG).show()
    }

}