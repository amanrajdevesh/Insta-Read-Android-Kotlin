package com.example.bookapp.modals

import android.os.Parcelable
import com.example.bookapp.firebaseModals.User
import kotlinx.parcelize.Parcelize

@Parcelize
class VolumeInfo(
    val allowAnonLogging: Boolean = false,
    val authors: List<String>? = null,
    val averageRating: Double = 0.0,
    val canonicalVolumeLink: String = "",
    val categories: List<String> = ArrayList<String>(),
    val contentVersion: String = "",
    val description: String = "",
    val imageLinks: ImageLinks = ImageLinks(),
    val industryIdentifiers: List<IndustryIdentifier> = ArrayList<IndustryIdentifier>(),
    val infoLink: String = "",
    val language: String = "",
    val maturityRating: String = "",
    val pageCount: Int = 0,
    val panelizationSummary: PanelizationSummary = PanelizationSummary() ,
    val previewLink: String = "",
    val printType: String = "",
    val publishedDate: String = "",
    val publisher: String = "",
    val ratingsCount: Int = 0,
    val readingModes: ReadingModes = ReadingModes() ,
    val subtitle: String = "",
    val title: String = "",
    var favourite : Boolean = false,
    var readLater : Boolean = false,
    var user : User? = User(),
) : Parcelable{
    fun addUser(user: User?) {
        this.user = user
    }
}