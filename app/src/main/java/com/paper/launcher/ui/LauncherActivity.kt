package com.paper.launcher.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.paper.login.ui.LoginActivity

class LauncherActivity : AppCompatActivity() {

  private lateinit var auth: FirebaseAuth

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    auth = FirebaseAuth.getInstance()
  }

  override fun onResume() {
    super.onResume()
    if (auth.currentUser == null) {
      navigateToLogin()
    } else {
      navigateToMain()
    }
  }

  private fun navigateToMain() {
    // Do Nothing yet
  }

  private fun navigateToLogin() {
    startActivity(Intent(this, LoginActivity::class.java))
  }
}