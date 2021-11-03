package com.example.bookapp.modals

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RetailPriceX(
    val amount: Double,
    val currencyCode: String
): Parcelable