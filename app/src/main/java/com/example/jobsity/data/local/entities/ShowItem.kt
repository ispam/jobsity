package com.example.jobsity.data.local.entities

import com.example.jobsity.common.delegate.ItemDelegate
import com.example.jobsity.utils.SHOW_TYPE
import com.google.gson.annotations.SerializedName

data class ShowItem(
    val id: Int,
    val name: String,
    val genres: List<String>,
    val image: Image,
    val summary: String,
    val premiered: String,
    val schedule: Schedule,
    @SerializedName("_embedded")
    val embedded: Embedded? = null,
    val showSummary: Boolean = false
) : ItemDelegate {
    override fun getViewType(): Int = SHOW_TYPE
    override fun id(): String = id.toString()
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

