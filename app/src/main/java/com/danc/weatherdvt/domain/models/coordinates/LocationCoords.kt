package com.danc.weatherdvt.domain.models.coordinates

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationCoords(
    val lat: Double,
    val lon: Double
) : Parcelable