package com.sklin.termproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiActivity
import com.google.android.gms.tasks.Task
import com.sklin.termproject.databinding.ActivityLoginBinding

private val RC_SIGN_IN: Int = 1

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var signInButton: SignInButton
//    private lateinit var signInButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        signInButton = findViewById(R.id.sign_in_button)

        // signInButton = binding.signInButton!!
        //        signInButton.setOnClickListener { view: View ->
        //            Intent(this, MainActivity::class.java).also {
        //                startActivity(it)
        //            }
        //        }

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

//        @BindingAdapter("android:onClick")
//        fun bindSignInClick(button: SignInButton, method: () -> Unit) {
//            button.setOnClickListener { method.invoke() }
//        }

        // Build a GoogleSignInClient with the options specified by gso.
        var mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signInButton.setSize(SignInButton.SIZE_WIDE)

        //signInButton = (SignInButton) findViewById(R.id.sign_in_button).setOnClickListener(this);

        signInButton.setOnClickListener { view: View ->
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            var task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task);
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount> ) {
        try {
            var account = completedTask.getResult(ApiException::class.java)
            //val account = completedTask.getResult()
            // Signed in successfully, show authenticated UI.
            //add account to database here?
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
            }
        } catch (e: ApiException) {
            Log.d("LOGIN", "Error")
        }
    }
}