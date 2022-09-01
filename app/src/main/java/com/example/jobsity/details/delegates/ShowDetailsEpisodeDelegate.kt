package com.example.jobsity.details.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsity.common.delegate.DelegateAdapter
import com.example.jobsity.data.local.entities.EpisodeItem
import com.example.jobsity.databinding.DelegateShowEpisodesBinding
import com.squareup.picasso.Picasso

class ShowDetailsEpisodeDelegate(
    private val onClick: (EpisodeItem) -> Unit
): DelegateAdapter<ShowDetailsEpisodeDelegate.ViewHolder, EpisodeItem> {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ViewHolder(
            DelegateShowEpisodesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: EpisodeItem) {
        viewHolder.bind(item)
    }

    inner class ViewHolder(
        private val binding: DelegateShowEpisodesBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: EpisodeItem) {
            with(binding) {
                Picasso.get().load(item.image?.medium).into(episodeImg)
                episodeName.text = item.name
                val rating = if (item.rating?.average ?: 0.0 <= 0.0) {
                    "No rating available"
                } else {
                    "Rating ${item.rating?.average}"
                }
                episodeRating.text = rating
                episodeRuntime.text = "Runtime ${item.runtime} minutes"
                root.setOnClickListener {
                    onClick.invoke(item)
                }
            }
        }
    }
}