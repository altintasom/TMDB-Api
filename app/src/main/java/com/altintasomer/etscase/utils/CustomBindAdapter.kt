package com.altintasomer.etscase.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.altintasomer.etscase.R
import com.squareup.picasso.Picasso

@BindingAdapter("android:customPicasso")
fun ImageView.setLoadCustomPicassoUrl(url: String?) {
    Picasso.get().load("https://image.tmdb.org/t/p/w500"+url ?: "").placeholder(R.drawable.ic_baseline_info_24).into(this)
}