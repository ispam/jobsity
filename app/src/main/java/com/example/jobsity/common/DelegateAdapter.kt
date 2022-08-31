package com.example.jobsity.common

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface DelegateAdapter<in VH : RecyclerView.ViewHolder, T : ItemDelegate> {

    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    fun onBindViewHolder(viewHolder: VH, item: T)

}