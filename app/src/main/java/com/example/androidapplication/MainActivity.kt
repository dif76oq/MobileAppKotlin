package com.example.androidapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.util.Log

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        //auth.signOut()

        val user = auth.currentUser
        Log.d("FirebaseAuth", "onCreate: currentUser = $user")

        if (user == null) {
            Log.d("FirebaseAuth", "onCreate: Пользователь не найден, переходим на LoginActivity")
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            Log.d("FirebaseAuth", "onCreate: Пользователь найден: ${user.email}")
            setContentView(R.layout.activity_main)
        }
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            Log.d("FirebaseAuth", "onStart: currentUser = $user")

            if (user == null) {
                Log.d("FirebaseAuth", "onStart: Пользователь вышел, переходим на LoginActivity")
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }
}
