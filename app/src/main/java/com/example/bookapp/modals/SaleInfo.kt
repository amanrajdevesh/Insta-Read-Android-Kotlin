package com.example.bookapp.modals

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SaleInfo(
    val buyLink: String,
    val country: String,
    val isEbook: Boolean,
    val listPrice: ListPrice,
    val offers: List<Offer>,
    val retailPrice: RetailPriceX,
    val saleability: String
) : Parcelable