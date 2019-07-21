package com.utsman.recyclerviewloader.core

import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide

fun ImageView.loadUrl(url: String) {
    Glide.with(this.context)
        .load(url)
        .centerCrop()
        .into(this)
}

fun logi(tag: String, msg: String) = Log.i(tag, msg)
fun loge(tag: String, msg: String) = Log.e(tag, msg)

fun Activity.toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

fun toVisibility(constraint: Boolean): Int {
    return if (constraint) View.VISIBLE
    else View.GONE
}