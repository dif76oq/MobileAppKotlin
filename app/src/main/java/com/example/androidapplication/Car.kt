package com.example.androidapplication

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Car(
    var id: String? = null,

    //General
    val brand: String? = null,
    val model: String? = null,
    val color: String? = null,
    val seats: Int = 0,
    val price: Int = 0,
    val year: Int = 0,

    //Details
    val bodyType: String? = null,
    val driveType: String? = null,
    val fuelType: String? = null,
    val engine: String? = null,
    val horsepower: Int = 0,
    val description: String? = null,
    val weight: Int = 0,

    // Transmission
    val transmission: String? = null,

    // Images (Base64 encoded or URLs)
    val images: List<String> = emptyList()
) : Parcelable
