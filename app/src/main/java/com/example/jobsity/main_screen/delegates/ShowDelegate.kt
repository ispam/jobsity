package com.example.jobsity.main_screen.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsity.common.delegate.DelegateAdapter
import com.example.jobsity.data.local.ShowItem
import com.example.jobsity.databinding.DelegateShowBinding
import com.squareup.picasso.Picasso

class ShowDelegate(
    private val onClick: (ShowItem) -> Unit
) : DelegateAdapter<ShowDelegate.ViewHolder, ShowItem> {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ViewHolder(
            DelegateShowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: ShowItem) {
        viewHolder.bind(item)
    }

    inner class ViewHolder(
        private val binding: DelegateShowBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ShowItem) {

            with(binding) {
                root.setOnClickListener {
                    onClick.invoke(item)
                }
                showName.text = item.name
                showGenres.text = item.genres.toString()
                showPremiered.text = "Premiered ${item.premiered}"
                Picasso.get().load(item.image.medium).into(showImg)
            }
        }
    }
}