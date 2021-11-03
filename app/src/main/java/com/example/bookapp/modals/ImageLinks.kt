package com.example.bookapp.modals

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ImageLinks(
    val smallThumbnail: String = "",
    val thumbnail: String = "",
    var image_url : String = ""
) : Parcelable{
    fun getImage() : String{
        this.image_url = "https"+thumbnail.substring(4)
        return image_url
    }
}