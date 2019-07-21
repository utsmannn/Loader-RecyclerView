package com.utsman.recyclerviewloader.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.utsman.recyclerviewloader.core.Pexel

class PagingDataFactory : DataSource.Factory<Long, Pexel>() {

    val pagingLiveData = MutableLiveData<PagingDataSource>()
    override fun create(): DataSource<Long, Pexel> {
        val data = PagingDataSource()
        pagingLiveData.postValue(data)
        return data
    }
}