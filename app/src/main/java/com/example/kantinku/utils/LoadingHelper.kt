package com.example.kantinku.utils

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class LoadingHelper(
    private val swipeRefreshLayout: SwipeRefreshLayout? = null,
    private val progressBar: ProgressBar? = null,
    private val emptyView: TextView? = null
) {

    fun showLoading() {
        swipeRefreshLayout?.isRefreshing = true
        progressBar?.visibility = View.VISIBLE
        emptyView?.visibility = View.GONE
    }

    fun hideLoading() {
        swipeRefreshLayout?.isRefreshing = false
        progressBar?.visibility = View.GONE
    }

    fun showEmpty(message: String = "Data tidak ditemukan") {
        emptyView?.text = message
        emptyView?.visibility = View.VISIBLE
        hideLoading()
    }

    fun showContent() {
        emptyView?.visibility = View.GONE
        hideLoading()
    }
}