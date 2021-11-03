package com.example.bookapp.modals

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RetailPrice(
    //val amountInMicros: Int,
    val currencyCode: String
) : Parcelable