package com.example.androidapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)
        val editTextUsername = findViewById<EditText>(R.id.editTextUsername)
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val editTextConfirmedPassword = findViewById<EditText>(R.id.editTextConfirmPassword)

        buttonLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        buttonRegister.setOnClickListener {
            val username = editTextUsername.text.toString().trim()
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString()
            val confirmedPassword = editTextConfirmedPassword.text.toString()

            if (validateInput(username, email, password, confirmedPassword)) {
                registerUser(username, email, password)
            }
        }
    }

    private fun validateInput(username: String, email: String, password: String, confirmedPassword: String): Boolean {
        when {
            email.isEmpty() -> {
                showToast("Введите email")
                return false
            }
            username.isEmpty() -> {
                showToast("Введите имя пользователя")
                return false
            }
            password.isEmpty() -> {
                showToast("Введите пароль")
                return false
            }
            confirmedPassword.isEmpty() -> {
                showToast("Подтвердите пароль")
                return false
            }
            password != confirmedPassword -> {
                showToast("Пароли не совпадают")
                return false
            }
            password.length < 6 -> {
                showToast("Пароль должен содержать минимум 6 символов")
                return false
            }
            else -> return true
        }
    }

    private fun registerUser(username: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    userId?.let { saveUserData(it, username, email) }
                } else {
                    showToast("Ошибка регистрации: ${task.exception?.message}")
                }
            }
    }

    private fun saveUserData(userId: String, username: String, email: String) {
        val userMap = hashMapOf(
            "username" to username,
            "email" to email
        )

        db.collection("users").document(userId)
            .set(userMap)
            .addOnSuccessListener {
                showToast("Регистрация успешна!")
                startActivity(Intent(this, CarListActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                showToast("Ошибка сохранения данных: ${e.message}")
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
