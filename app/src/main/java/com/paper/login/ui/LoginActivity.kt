package com.paper.login.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.paper.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

  companion object {
    private const val REQUEST_CODE_GOOGLE_LOGIN = 1
  }

  private lateinit var googleSignInClient: GoogleSignInClient
  private lateinit var auth: FirebaseAuth

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_login)

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
      .requestIdToken(getString(R.string.default_web_client_id))
      .requestEmail()
      .build()

    googleSignInClient = GoogleSignIn.getClient(this, gso)
    auth = FirebaseAuth.getInstance()

    loginButton.setOnClickListener { loginWithGoogle() }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == REQUEST_CODE_GOOGLE_LOGIN && resultCode == Activity.RESULT_OK) {
      val task = GoogleSignIn.getSignedInAccountFromIntent(data)

      try {
        val account = task.getResult(ApiException::class.java)
        account?.let { firebaseAuthWithGoogle(it) }
      } catch (e: ApiException) {
      }
    }
  }

  private fun loginWithGoogle() {
    startActivityForResult(googleSignInClient.signInIntent, REQUEST_CODE_GOOGLE_LOGIN)
  }

  private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
    auth.signInWithCredential(credential)
      .addOnCompleteListener(this) {
        if (it.isSuccessful) {
          auth.currentUser
        }
      }
  }
}