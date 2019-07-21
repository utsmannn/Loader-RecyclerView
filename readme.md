## Sample Loader for RecyclerView


![](https://i.ibb.co/KjjGKhS/paging.gif)

Loader sample using network state and custom adapter, work with endless recyclerview listener and paging library.

- Edit your adapter like this
```kotlin
    /**
     * use network state
     * */
    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            R.layout.item_view -> MainViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false))
            R.layout.item_loader -> NetworkViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_loader, parent, false))
            else  -> throw IllegalArgumentException("not found view holder")
        }
    }

    /**
     * if has extra row, size + 1 for loader
     */
    override fun getItemCount(): Int = 
            list.size + if (hasExtraRow()) 1 else 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_view -> {
                // bind to your main holder
            }
            R.layout.item_loader -> {
                // bind to your network holder
            }
        }
    }

    /**
     * detect extra row
     * */
    private fun hasExtraRow() = 
            networkState != null && networkState != NetworkState.LOADED

    /**
     * set view type,
     * if has extra row, viewType is loader
     * */
    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount -1 ) {
            R.layout.item_loader
        } else {
            R.layout.item_view
        }
    }

    /**
     * set network state from activity / fragment
     * */
    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(itemCount)
            } else {
                notifyItemInserted(itemCount)
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }
```