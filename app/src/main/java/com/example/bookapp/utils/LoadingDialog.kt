package com.example.bookapp.utils

import android.app.Activity
import android.app.AlertDialog
import com.example.bookapp.R

class LoadingDialog(val activity: Activity) {

    private var alertDialog: AlertDialog? = null

    fun startLoading() {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.item_progress,null))
        alertDialog = builder.create()
        alertDialog!!.show()
        alertDialog!!.setCancelable(true)
    }

    fun stopLoading() {
        alertDialog!!.dismiss()
    }

}