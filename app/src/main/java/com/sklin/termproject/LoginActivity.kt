package com.sklin.termproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
//import com.google.android.gms.common.SignInButton
import com.sklin.termproject.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
//    private lateinit var signInButton: SignInButton
    private lateinit var signInButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signInButton = binding.signInButton

        signInButton.setOnClickListener { view: View ->
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
            }
        }

    }
}