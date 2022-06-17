package com.altintasomer.etscase.view

import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.log

private const val TAG = "PaginationScrollListene"
abstract class PaginationScrollListener(private val layoutManager: GridLayoutManager) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        val visibleItemCount  = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (!isLoading() && !isLastPage()){
            if ((visibleItemCount + firstVisibleItemPosition) <= totalItemCount && firstVisibleItemPosition >= 0){
                loadMoreItems()
            }
        }
    }


    abstract fun loadMoreItems()
    abstract fun isLastPage() : Boolean
    abstract fun isLoading() : Boolean


}