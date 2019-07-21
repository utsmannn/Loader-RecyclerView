package com.utsman.recyclerviewloader.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemKeyedDataSource
import com.utsman.recyclerviewloader.core.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PagingDataSource : ItemKeyedDataSource<Long, Pexel>() {

    private val perPage = 10
    private var page = 1
    var networkState = MutableLiveData<NetworkState>()

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Pexel>) {
        networkState.postValue(NetworkState.LOADING)
        RetrofitInstance.create().getCuratedPhoto(perPage, page)
            .enqueue(object : Callback<Responses> {
                override fun onResponse(call: Call<Responses>, response: Response<Responses>) {
                    val body = response.body()
                    if (body != null) {
                        page++
                        networkState.postValue(NetworkState.LOADED)
                        callback.onResult(body.photos)
                    }
                }

                override fun onFailure(call: Call<Responses>, t: Throwable) {
                    loge("anjay", t.localizedMessage)
                    networkState.postValue(NetworkState.error(t.localizedMessage))
                }

            })
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Pexel>) {
        networkState.postValue(NetworkState.LOADING)
        RetrofitInstance.create().getCuratedPhoto(perPage, page)
            .enqueue(object : Callback<Responses> {
                override fun onResponse(call: Call<Responses>, response: Response<Responses>) {
                    val body = response.body()
                    if (body != null) {
                        page++
                        networkState.postValue(NetworkState.LOADING)
                        callback.onResult(body.photos)
                    }
                }

                override fun onFailure(call: Call<Responses>, t: Throwable) {
                    loge("anjay", t.localizedMessage)
                    networkState.postValue(NetworkState.error(t.localizedMessage))
                }

            })
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Pexel>) {}

    override fun getKey(item: Pexel): Long = item.id
}