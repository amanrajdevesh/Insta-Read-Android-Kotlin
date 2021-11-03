package com.example.bookapp.modals

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchInfo(
    val textSnippet: String
) : Parcelable