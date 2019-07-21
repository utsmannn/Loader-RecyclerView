package com.utsman.recyclerviewloader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.utsman.recyclerviewloader.core.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val list: MutableList<Pexel> = mutableListOf()
    private val adapter = EndlessAdapter(list)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gridLayoutManager = GridLayoutManager(this, 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapter.getItemViewType(position)) {
                    R.layout.item_view -> 1
                    R.layout.item_loader -> 2
                    else -> 1
                }
            }
        }

        main_recycler_view.layoutManager = gridLayoutManager
        main_recycler_view.adapter = adapter

        getApi(1)
        main_recycler_view.addOnScrollListener(object : EndlessRecyclerViewScrollListener(gridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                getApi(page+1)
            }
        })

    }

    private fun getApi(page: Int) {
        adapter.setNetworkState(NetworkState.LOADING)
        adapter.notifyDataSetChanged()
        RetrofitInstance.create().getCuratedPhoto(10, page)
                .enqueue(object : Callback<Responses> {
                    override fun onResponse(call: Call<Responses>, response: Response<Responses>) {
                        val body = response.body()
                        if (body != null) {
                            list.addAll(body.photos)
                            adapter.notifyDataSetChanged()
                            adapter.setNetworkState(NetworkState.LOADED)

                        }
                    }

                    override fun onFailure(call: Call<Responses>, t: Throwable) {
                        loge("anjay", t.localizedMessage)
                        adapter.setNetworkState(NetworkState.error(t.localizedMessage))
                    }
                })
    }
}
