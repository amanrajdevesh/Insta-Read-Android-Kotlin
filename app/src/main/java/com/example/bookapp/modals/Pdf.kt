package com.example.bookapp.modals

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pdf(
    val acsTokenLink: String,
    val isAvailable: Boolean
) : Parcelable