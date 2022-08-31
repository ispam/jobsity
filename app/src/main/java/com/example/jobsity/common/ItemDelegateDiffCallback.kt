package com.example.jobsity.common

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

class ItemDelegateDiffCallback : DiffUtil.ItemCallback<ItemDelegate>() {

    override fun areItemsTheSame(oldItem: ItemDelegate, newItem: ItemDelegate): Boolean =
        oldItem::class == newItem::class && oldItem.id() == newItem.id()

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ItemDelegate, newItem: ItemDelegate): Boolean =
        oldItem.content() == newItem.content()
}