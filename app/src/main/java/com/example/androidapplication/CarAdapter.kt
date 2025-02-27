package com.example.androidapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CarAdapter(private val cars: List<Car>, private val onClick: (Car) -> Unit) :
    RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        holder.bind(cars[position], onClick)
    }

    override fun getItemCount(): Int = cars.size

    class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val carImageView: ImageView = itemView.findViewById(R.id.carImage)
        private val carBrandText: TextView = itemView.findViewById(R.id.carBrand)
        private val carModelText: TextView = itemView.findViewById(R.id.carModel)
        private val carPriceText: TextView = itemView.findViewById(R.id.carPrice)
        private val carYearText: TextView = itemView.findViewById(R.id.carYear)
        private val carBodyTypeText: TextView = itemView.findViewById(R.id.carBodyType)

        fun bind(car: Car, onClick: (Car) -> Unit) {
            carBrandText.text = car.brand
            carModelText.text = car.model
            carPriceText.text = "${car.price} USD"
            carYearText.text = car.year.toString()
            carBodyTypeText.text = car.bodyType

            Glide.with(itemView.context)
                .load(car.images[0])
                //.placeholder(R.drawable.ic_car_placeholder)
                //.error(R.drawable.ic_error_placeholder)
                .into(carImageView)

            itemView.setOnClickListener { onClick(car) }
        }
    }
}
