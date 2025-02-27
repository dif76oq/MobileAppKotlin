package com.example.androidapplication

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class CarRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getCars(callback: (List<Car>) -> Unit) {
        firestore.collection("cars")
            .get()
            .addOnSuccessListener { documents ->
                val cars = mutableListOf<Car>()
                for (document in documents) {
                    val car = document.toObject(Car::class.java)
                    car.id = document.id
                    cars.add(car)
                }
                callback(cars)
            }
            .addOnFailureListener { exception ->
                Log.e("CarRepository", "Error getting documents: ", exception)
            }
    }
}
