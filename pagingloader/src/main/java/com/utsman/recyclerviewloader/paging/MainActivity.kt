package com.utsman.recyclerviewloader.paging

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val pagingViewModel by lazy {
        ViewModelProviders.of(this)[PagingViewModel::class.java]
    }

    private val adapter = PagingAdapter()

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

        pagingViewModel.getCuratedPhoto().observe(this, Observer {dataPaging ->
            adapter.submitList(dataPaging)
        })

        pagingViewModel.getLoader().observe(this, Observer { dataNetwork ->
            adapter.setNetworkState(dataNetwork)
        })
    }
}
