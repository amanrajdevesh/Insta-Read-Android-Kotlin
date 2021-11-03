package com.example.bookapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.bookapp.R
import com.example.bookapp.dao.UserDao
import com.example.bookapp.databinding.ActivityLoginBinding
import com.example.bookapp.firebaseModals.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    lateinit var auth : FirebaseAuth
    lateinit var binding: ActivityLoginBinding
    companion object{
        private const val TAG = "LoginActivity"
        private const val RC_SIGN_IN = 32
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        binding.loginButton.setOnClickListener {
            loginWithEmail()
        }
        binding.signupButton.setOnClickListener {
            val intent = Intent(this , SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.signInButton.setOnClickListener{
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun updateUI(firebaseUser: FirebaseUser?) {
        if(firebaseUser==null){
            Log.d(TAG , "User is null")
            return
        }else{
            val user = User(firebaseUser.uid , firebaseUser.displayName.toString() , firebaseUser.photoUrl.toString())
            val userDao = UserDao()
            userDao.addUser(user)
            startActivity(Intent(this , MainActivity::class.java))
            finish()
        }
    }

    private fun loginWithEmail() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Log.d(TAG, "Email or Password is empty")
        }else{
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this , OnCompleteListener { task ->
                if(task.isSuccessful){
                    if(task.isSuccessful){
                        Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show()
                    }
                }
            })
        }
    }

}