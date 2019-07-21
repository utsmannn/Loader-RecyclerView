package com.utsman.recyclerviewloader.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.utsman.recyclerviewloader.core.NetworkState
import com.utsman.recyclerviewloader.core.Pexel

class PagingViewModel : ViewModel() {

    private var pagingDataFactory: PagingDataFactory? = null

    private fun configPaged(size: Int): PagedList.Config = PagedList.Config.Builder()
        .setPageSize(size)
        .setInitialLoadSizeHint(size * 2)
        .setEnablePlaceholders(true)
        .build()

    fun getCuratedPhoto(): LiveData<PagedList<Pexel>> {
        pagingDataFactory = PagingDataFactory()
        return LivePagedListBuilder(pagingDataFactory!!, configPaged(4)).build()
    }

    fun getLoader(): LiveData<NetworkState> = Transformations.switchMap<PagingDataSource, NetworkState>(
        pagingDataFactory?.pagingLiveData!!
    ) { it.networkState }
}