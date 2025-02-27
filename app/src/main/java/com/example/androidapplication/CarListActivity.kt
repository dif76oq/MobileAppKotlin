package com.example.androidapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CarListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var carAdapter: CarAdapter
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
    }
}
