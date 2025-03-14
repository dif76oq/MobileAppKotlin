package com.example.androidapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {
    private var etUserame: EditText? = null
    private var etDateOfBirth: EditText? = null
    private var etEmail: EditText? = null
    private var etPhone: EditText? = null
    private var etAddress: EditText? = null
    private var etCity: EditText? = null
    private var etCountry: EditText? = null
    private var etPostalCode: EditText? = null
    private var etOccupation: EditText? = null
    private var etBio: EditText? = null
    private var db: FirebaseFirestore? = null

    private val USER_ID = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        db = FirebaseFirestore.getInstance()

        etUserame = findViewById<EditText>(R.id.etUserame)
        etDateOfBirth = findViewById<EditText>(R.id.etDateOfBirth)
        etEmail = findViewById<EditText>(R.id.etEmail)
        etPhone = findViewById<EditText>(R.id.etPhone)
        etAddress = findViewById<EditText>(R.id.etAddress)
        etCity = findViewById<EditText>(R.id.etCity)
        etCountry = findViewById<EditText>(R.id.etCountry)
        etPostalCode = findViewById<EditText>(R.id.etPostalCode)
        etOccupation = findViewById<EditText>(R.id.etOccupation)
        etBio = findViewById<EditText>(R.id.etBio)

        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnDelete = findViewById<Button>(R.id.btnDelete)

        btnSave.setOnClickListener { v: View? -> saveProfile() }
        btnDelete.setOnClickListener { v: View? -> deleteAccount() }

        loadUserProfile()
    }
    private fun loadUserProfile() {
        db!!.collection("users").document(USER_ID)
            .get()
            .addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
                if (documentSnapshot.exists()) {
                    etUserame!!.setText(documentSnapshot.getString("username"))
                    etDateOfBirth!!.setText(documentSnapshot.getString("dateOfBirth"))
                    etEmail!!.setText(documentSnapshot.getString("email"))
                    etPhone!!.setText(documentSnapshot.getString("phone"))
                    etAddress!!.setText(documentSnapshot.getString("address"))
                    etCity!!.setText(documentSnapshot.getString("city"))
                    etCountry!!.setText(documentSnapshot.getString("country"))
                    etPostalCode!!.setText(documentSnapshot.getString("postalCode"))
                    etOccupation!!.setText(documentSnapshot.getString("occupation"))
                    etBio!!.setText(documentSnapshot.getString("bio"))
                } else {
                    Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e: Exception? ->
                Log.e("Firestore", "Error loading user data", e)
                Toast.makeText(
                    this,
                    "Failed to load profile data",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun saveProfile() {
        val userData: MutableMap<String, Any> = HashMap()
        userData["name"] = etUserame!!.text.toString().trim { it <= ' ' }
        userData["dateOfBirth"] = etDateOfBirth!!.text.toString().trim { it <= ' ' }
        userData["email"] = etEmail!!.text.toString().trim { it <= ' ' }
        userData["phone"] = etPhone!!.text.toString().trim { it <= ' ' }
        userData["address"] = etAddress!!.text.toString().trim { it <= ' ' }
        userData["city"] = etCity!!.text.toString().trim { it <= ' ' }
        userData["country"] = etCountry!!.text.toString().trim { it <= ' ' }
        userData["postalCode"] = etPostalCode!!.text.toString().trim { it <= ' ' }
        userData["occupation"] = etOccupation!!.text.toString().trim { it <= ' ' }
        userData["bio"] = etBio!!.text.toString().trim { it <= ' ' }

        db!!.collection("users").document(USER_ID)
            .set(userData)
            .addOnSuccessListener { aVoid: Void? ->
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT)
                    .show()
            }
            .addOnFailureListener { e: Exception? ->
                Log.e("Firestore", "Error updating profile", e)
                Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun deleteAccount() {
        val user = FirebaseAuth.getInstance().currentUser

        db!!.collection("users").document(USER_ID)
            .delete()
            .addOnSuccessListener {
                user?.delete()?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, RegisterActivity::class.java)
                        //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Failed to delete account", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error deleting account", e)
                Toast.makeText(this, "Failed to delete account", Toast.LENGTH_SHORT).show()
            }
    }

}