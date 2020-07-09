package com.egorshustov.vpoiske.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.egorshustov.vpoiske.data.SearchWithUsers
import com.egorshustov.vpoiske.databinding.ItemSearchBinding
import com.egorshustov.vpoiske.pastsearchlist.PastSearchListViewModel
import com.egorshustov.vpoiske.util.getFavoritesCount
import com.egorshustov.vpoiske.util.getUsersCount

class SearchWithUsersAdapter(private val viewModel: PastSearchListViewModel) :
    PagedListAdapter<SearchWithUsers, SearchWithUsersAdapter.ViewHolder>(
        SearchWithUsersDiffCallback()
    ) {

    fun getItemAtPosition(position: Int): SearchWithUsers? = getItem(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(viewModel, it) }
    }

    class ViewHolder private constructor(val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: PastSearchListViewModel, item: SearchWithUsers) = with(binding) {
            viewmodel = viewModel
            searchWithUsers = item
            executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemSearchBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class SearchWithUsersDiffCallback : DiffUtil.ItemCallback<SearchWithUsers>() {

    override fun areItemsTheSame(oldItem: SearchWithUsers, newItem: SearchWithUsers) =
        oldItem.search.id == newItem.search.id

    override fun areContentsTheSame(oldItem: SearchWithUsers, newItem: SearchWithUsers) =
        oldItem.search.cityTitle == newItem.search.cityTitle
                && oldItem.search.ageFrom == newItem.search.ageTo
                && oldItem.search.withPhoneOnly == newItem.search.withPhoneOnly
                && oldItem.search.startUnixSeconds == newItem.search.startUnixSeconds
                && oldItem.getUsersCount() == newItem.getUsersCount()
                && oldItem.getFavoritesCount() == newItem.getFavoritesCount()
                && arePhotoUrlsTheSame(oldItem, newItem)

    private fun arePhotoUrlsTheSame(oldItem: SearchWithUsers, newItem: SearchWithUsers): Boolean {
        for (i in 0..8) {
            if (oldItem.userList.getOrNull(i)?.photo50 != newItem.userList.getOrNull(i)?.photo50) return false
        }
        return true
    }
}
