package com.example.bookapp.modals

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PanelizationSummary(
    val containsEpubBubbles: Boolean = false,
    val containsImageBubbles: Boolean = false
): Parcelable