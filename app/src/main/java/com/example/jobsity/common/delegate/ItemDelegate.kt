package com.example.jobsity.common.delegate

import android.os.Parcelable

interface ItemDelegate {
    fun getViewType(): Int
    fun id(): String
    fun content(): Any
}

interface ParcelableItemDelegate : ItemDelegate, Parcelable