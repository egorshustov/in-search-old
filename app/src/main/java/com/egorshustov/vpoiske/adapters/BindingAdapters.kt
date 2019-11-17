package com.egorshustov.vpoiske.adapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.egorshustov.vpoiske.data.User

@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, userList: List<User>?) {
    (listView.adapter as UsersAdapter).submitList(userList)
}