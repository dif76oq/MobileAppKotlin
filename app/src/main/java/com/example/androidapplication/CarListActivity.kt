package com.example.androidapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class CarListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var carAdapter: CarAdapter
    private lateinit var btnLogout: Button
    private lateinit var btnProfile: Button
    private val carRepository = CarRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_list)

        recyclerView = findViewById(R.id.recyclerViewCars)
        recyclerView.layoutManager = LinearLayoutManager(this)

        carRepository.getCars { cars ->
            carAdapter = CarAdapter(cars) { car ->
                val intent = Intent(this, CarDetailActivity::class.java)
                intent.putExtra("car", car)
                startActivity(intent)
            }
            recyclerView.adapter = carAdapter
        }
        btnLogout = findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener{logout()}

        btnProfile = findViewById<Button>(R.id.btnProfile)
        btnProfile.setOnClickListener{profile()}
    }

    private fun profile() {
        startActivity(Intent(this, ProfileActivity::class.java))
        finish()
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
