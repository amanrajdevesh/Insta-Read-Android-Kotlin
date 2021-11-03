package com.example.bookapp.modals

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Offer(
    val finskyOfferType: Int,
    val listPrice: ListPriceX,
    val retailPrice: RetailPrice
): Parcelable