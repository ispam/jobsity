package com.example.jobsity.data.local.entities

import com.example.jobsity.common.delegate.ItemDelegate
import com.example.jobsity.utils.SHOW_DETAILS_TYPE

data class Embedded(
    val episodes: List<EpisodeItem>
)

data class EpisodeItem(
    val id: Int?,
    val name: String?,
    val season: Int?,
    val number: Int?,
    val runtime: Int?,
    val rating: Rating?,
    val image: Image?,
    val summary: String?,
    val ended: String?
): ItemDelegate {
    override fun getViewType(): Int = SHOW_DETAILS_TYPE
    override fun id(): String = id.toString()
    override fun content(): Any = this
}

data class Rating(
    val average: Double
)