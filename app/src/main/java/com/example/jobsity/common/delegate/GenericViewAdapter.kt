package com.example.jobsity.common.delegate

import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.lang.IllegalStateException

open class GenericViewAdapter : ListAdapter<ItemDelegate, RecyclerView.ViewHolder> {

    var delegateAdapters: SparseArrayCompat<DelegateAdapter<RecyclerView.ViewHolder, ItemDelegate>>

    constructor() : super(ItemDelegateDiffCallback()) {
        delegateAdapters = SparseArrayCompat()
    }

    constructor(
        delegateAdapterMap: Map<Int, DelegateAdapter<RecyclerView.ViewHolder, ItemDelegate>>
    ) : super(ItemDelegateDiffCallback()) {
        delegateAdapters = SparseArrayCompat(delegateAdapterMap.size)
        for ((key, value) in delegateAdapterMap) {
            delegateAdapters.put(key, value)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val delegateAdapter = delegateAdapters.get(viewType)
            ?: throw IllegalStateException(
                "The method onCreateViewHolder cannot return view type $viewType null"
            )
        return delegateAdapter.onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters.get(getItemViewType(position))
            ?.onBindViewHolder(holder, currentList[position])
    }

    override fun getItemCount(): Int = currentList.size

    override fun getItemViewType(position: Int): Int = currentList[position].getViewType()

    override fun getItemId(position: Int): Long = position.toLong()

}
