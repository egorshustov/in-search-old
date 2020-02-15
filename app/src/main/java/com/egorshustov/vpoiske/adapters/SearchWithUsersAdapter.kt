package com.egorshustov.vpoiske.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.egorshustov.vpoiske.data.SearchWithUsers
import com.egorshustov.vpoiske.databinding.ItemSearchBinding
import com.egorshustov.vpoiske.searchlist.SearchListViewModel

class SearchWithUsersAdapter(private val viewModel: SearchListViewModel) :
    PagedListAdapter<SearchWithUsers, SearchWithUsersAdapter.ViewHolder>(
        SearchWithUsersDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(viewModel, it) }
    }

    class ViewHolder private constructor(val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: SearchListViewModel, item: SearchWithUsers) = with(binding) {
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
    override fun areItemsTheSame(oldItem: SearchWithUsers, newItem: SearchWithUsers): Boolean {
        return oldItem.search.id == newItem.search.id
    }

    override fun areContentsTheSame(oldItem: SearchWithUsers, newItem: SearchWithUsers): Boolean {
        return oldItem.search.countryTitle == newItem.search.countryTitle
                && oldItem.search.cityTitle == newItem.search.cityTitle
        //todo add rest of the necessary fields
    }
}
