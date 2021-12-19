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
        if(smallThumbnail.isEmpty()){
            return smallThumbnail
        }
        this.image_url = "https"+smallThumbnail.substring(4)
        return image_url
    }
}