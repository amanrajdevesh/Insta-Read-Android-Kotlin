package com.example.bookapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.bookapp.R
import com.example.bookapp.databinding.ActivitySignUpBinding
import com.example.bookapp.utils.LoadingDialog

class SignUpActivity : AppCompatActivity() {

    lateinit var binding : ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up)

        val loading = LoadingDialog(this)
        binding.button.setOnClickListener {
            loading.startLoading()
        }
    }
}