package com.example.bookapp.modals

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item (
    val accessInfo: AccessInfo,
    val etag: String,
    val id: String,
    val kind: String,
    val saleInfo: SaleInfo,
    val searchInfo: SearchInfo,
    val selfLink: String,
    val volumeInfo: VolumeInfo
) : Parcelable