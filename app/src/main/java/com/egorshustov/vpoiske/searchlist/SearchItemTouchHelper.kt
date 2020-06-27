package com.egorshustov.vpoiske.searchlist

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.egorshustov.vpoiske.adapters.SearchWithUsersAdapter

class SearchItemTouchHelper(
    dragDirs: Int,
    swipeDirs: Int,
    private val searchItemTouchHelperListener: SearchItemTouchHelperListener
) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        val cardSearch = (viewHolder as SearchWithUsersAdapter.ViewHolder).binding.cardSearch
        ItemTouchHelper.Callback.getDefaultUIUtil().clearView(cardSearch)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val cardSearch = (viewHolder as SearchWithUsersAdapter.ViewHolder).binding.cardSearch
        ItemTouchHelper.Callback.getDefaultUIUtil()
            .onDraw(c, recyclerView, cardSearch, dX, dY, actionState, isCurrentlyActive)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        searchItemTouchHelperListener.onSwiped(viewHolder, direction, viewHolder.adapterPosition)
    }

    interface SearchItemTouchHelperListener {
        fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int)
    }
}