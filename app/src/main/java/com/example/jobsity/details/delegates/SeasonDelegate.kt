package com.example.jobsity.details.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsity.common.delegate.DelegateAdapter
import com.example.jobsity.common.delegate.ItemDelegate
import com.example.jobsity.databinding.DelegateSeasonBinding
import com.example.jobsity.utils.SEASON_TYPE

class SeasonDelegate: DelegateAdapter<SeasonDelegate.ViewHolder, SeasonDelegate.Model> {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ViewHolder(
            DelegateSeasonBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Model) {
        viewHolder.bind(item)
    }

    data class Model(val season: String) : ItemDelegate {
        override fun getViewType(): Int = SEASON_TYPE
        override fun id(): String = season
        override fun content(): Any = this
    }

    inner class ViewHolder(
        private val binding: DelegateSeasonBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Model) {
            binding.season.text = item.season
        }
    }
}