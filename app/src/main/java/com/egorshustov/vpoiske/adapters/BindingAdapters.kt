package com.egorshustov.vpoiske.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.egorshustov.vpoiske.data.User

@BindingAdapter("app:users")
fun RecyclerView.setUsers(userList: List<User>?) {
    (adapter as UsersAdapter).submitList(userList)
}

@BindingAdapter("app:imageFromUrl")
fun ImageView.bindImageFromUrl(imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Glide.with(context)
            .load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
    }
}