package com.egorshustov.vpoiske.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.egorshustov.vpoiske.data.User
import com.egorshustov.vpoiske.databinding.ItemUserBinding
import com.egorshustov.vpoiske.searchprocess.SearchProcessViewModel

class UsersAdapter(private val viewModel: SearchProcessViewModel) :
    ListAdapter<User, UsersAdapter.ViewHolder>(UserDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let { holder.bind(viewModel, it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent)

    class ViewHolder private constructor(val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: SearchProcessViewModel, item: User) = with(binding) {
            viewmodel = viewModel
            user = item
            executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemUserBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class UserDiffCallback : DiffUtil.ItemCallback<User>() {

    override fun areItemsTheSame(oldItem: User, newItem: User) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: User, newItem: User) =
        oldItem.inBlacklist == newItem.inBlacklist
                && oldItem.isFavorite == newItem.isFavorite
                && oldItem.photoMaxOrig == newItem.photoMaxOrig
}
