package com.example.bookapp.modals

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReadingModes(
    val image: Boolean = false,
    val text: Boolean = false
): Parcelable