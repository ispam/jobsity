package com.example.jobsity.data.local

import com.example.jobsity.common.delegate.ItemDelegate
import com.example.jobsity.utils.SHOW_TYPE

data class ShowItem(
    val id: String,
    val name: String,
    val genres: List<String>,
    val image: Image,
    val summary: String,
    val premiered: String,
    val ended: String,
    val schedule: Schedule
):ItemDelegate {
    override fun getViewType(): Int = SHOW_TYPE
    override fun id(): String = id
    override fun content(): Any = this
}

data class Image(
    val medium: String,
    val original: String
)

data class Schedule(
    val time: String,
    val days: List<String>
)

