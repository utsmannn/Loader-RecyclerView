package com.utsman.recyclerviewloader.paging

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.utsman.recyclerviewloader.core.*
import kotlinx.android.synthetic.main.item_loader.view.*
import kotlinx.android.synthetic.main.item_view.view.*

class PagingAdapter : PagedListAdapter<Pexel, RecyclerView.ViewHolder>(PagingDiffUtil()) {

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            R.layout.item_view -> MainViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false))
            R.layout.item_loader -> NetworkViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_loader, parent, false))
            else  -> throw IllegalArgumentException("not found view holder")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)) {
            R.layout.item_view -> {
                val mainHolder = holder as MainViewHolder
                val pexel = getItem(position)
                val imgView = mainHolder.itemView.main_image

                if (pexel != null) imgView.loadUrl(pexel.src.small)
            }
            R.layout.item_loader -> (holder as NetworkViewHolder).bind(networkState)
        }
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount -1 ) {
            R.layout.item_loader
        } else {
            R.layout.item_view
        }
    }

    override fun getItemCount(): Int = super.getItemCount() + if (hasExtraRow()) 1 else 0

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }
}

class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class NetworkViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    @SuppressLint("SetTextI18n")
    fun bind(networkState: NetworkState?) = itemView.run {
        progress_circular.visibility = toVisibility(networkState?.status == Status.RUNNING)
        error_text_view.visibility = toVisibility(networkState?.status == Status.FAILED)

        error_text_view.text = "Network error: ${networkState?.msg}"
    }
}