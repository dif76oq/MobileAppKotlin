package com.example.androidapplication

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue

class CarDetailActivity : AppCompatActivity() {
    private lateinit var carImage: ImageView
    private lateinit var btnPrev: ImageButton
    private lateinit var btnNext: ImageButton
    private lateinit var btnAddToFavorite: ImageButton

    private var images: List<String> = emptyList()
    private var currentIndex = 0
    private lateinit var car: Car
    private lateinit var carDocumentId: String // ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_detail)

        carImage = findViewById(R.id.carImageDetail)
        btnPrev = findViewById(R.id.btnPrev)
        btnNext = findViewById(R.id.btnNext)
        btnAddToFavorite = findViewById(R.id.btnAddToFavorite)

        val car = intent.getParcelableExtra<Car>("car")

        car?.let {
            this.car = it
            this.carDocumentId = it.id ?: ""
            images = it.images

            if (images.isNotEmpty()) {
                loadImage(currentIndex)
            }

            findViewById<TextView>(R.id.modelText).text = "Model: ${it.model}"
            findViewById<TextView>(R.id.brandText).text = "Brand: ${it.brand}"
            findViewById<TextView>(R.id.priceText).text = "Price: ${it.price} $"
            findViewById<TextView>(R.id.yearText).text = "Year: ${it.year}"
            findViewById<TextView>(R.id.bodyTypeText).text = "Body Type: ${it.bodyType}"
            findViewById<TextView>(R.id.engineText).text = "Engine: ${it.engine}"
            findViewById<TextView>(R.id.horsepowerText).text = "Horsepower: ${it.horsepower}"
            findViewById<TextView>(R.id.fuelTypeText).text = "Fuel Type: ${it.fuelType}"
            findViewById<TextView>(R.id.transmissionText).text = "Transmission: ${it.transmission}"
            findViewById<TextView>(R.id.seatsText).text = "Seats: ${it.seats}"
            findViewById<TextView>(R.id.weightText).text = "Weight: ${it.weight} kg"
            findViewById<TextView>(R.id.driveTypeText).text = "Drive Type: ${it.driveType}"

            checkIfFavorite()

            btnPrev.setOnClickListener { prevImage() }
            btnNext.setOnClickListener { nextImage() }

            btnAddToFavorite.setOnClickListener {
                toggleFavorite()
            }
        }
    }

    private fun loadImage(index: Int) {
        Glide.with(this)
            .load(images[index])
            .into(carImage)
    }

    private fun prevImage() {
        if (currentIndex > 0) {
            currentIndex--
            loadImage(currentIndex)
        } else {
            currentIndex = images.size - 1
            loadImage(currentIndex)
        }
    }

    private fun nextImage() {
        if (currentIndex < images.size - 1) {
            currentIndex++
            loadImage(currentIndex)
        } else {
            currentIndex = 0
            loadImage(currentIndex)
        }
    }

    private fun checkIfFavorite() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            val db = FirebaseFirestore.getInstance()
            val userRef = db.collection("users").document(it)

            userRef.get().addOnSuccessListener { document ->
                if (document != null) {
                    val favoriteCars = document.get("favoriteCars") as? List<String>
                    if (favoriteCars != null && favoriteCars.contains(carDocumentId)) {

                        btnAddToFavorite.setImageResource(R.drawable.ic_heart_filled)
                    } else {

                        btnAddToFavorite.setImageResource(R.drawable.ic_heart_outline)
                    }
                }
            }
        }
    }

    private fun toggleFavorite() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            val db = FirebaseFirestore.getInstance()
            val userRef = db.collection("users").document(it)

            userRef.get().addOnSuccessListener { document ->
                if (document != null) {
                    val favoriteCars = document.get("favoriteCars") as? List<String>
                    if (favoriteCars != null && favoriteCars.contains(carDocumentId)) {

                        userRef.update("favoriteCars", FieldValue.arrayRemove(carDocumentId))
                            .addOnSuccessListener {
                                btnAddToFavorite.setImageResource(R.drawable.ic_heart_outline)
                                Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Failed to remove from favorites", Toast.LENGTH_SHORT).show()
                            }
                    } else {

                        userRef.update("favoriteCars", FieldValue.arrayUnion(carDocumentId))
                            .addOnSuccessListener {
                                btnAddToFavorite.setImageResource(R.drawable.ic_heart_filled)
                                Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Failed to add to favorites", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }
        }
    }
}
