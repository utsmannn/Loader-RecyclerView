package com.utsman.recyclerviewloader.paging

import androidx.recyclerview.widget.DiffUtil
import com.utsman.recyclerviewloader.core.Pexel

class PagingDiffUtil : DiffUtil.ItemCallback<Pexel>() {
    override fun areItemsTheSame(oldItem: Pexel, newItem: Pexel): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Pexel, newItem: Pexel): Boolean = oldItem == newItem
}