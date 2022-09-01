package com.example.jobsity.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.navigation.fragment.findNavController
import com.example.jobsity.data.local.entities.EpisodeItem
import com.example.jobsity.databinding.FragmentDialogEpisodeBinding
import com.example.jobsity.utils.toModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso

class EpisodeDetailsDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentDialogEpisodeBinding? = null
    val binding: FragmentDialogEpisodeBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDialogEpisodeBinding.inflate(
            LayoutInflater.from(requireContext()),
            container,
            false
        )
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getString(EPISODE_KEY)?.let {
            val episodeItem = it.toModel<EpisodeItem>()
            with(binding) {
                Picasso.get().load(episodeItem?.image?.medium).into(episodeImg)
                episodeName.text = episodeItem?.name
                val rating = if (episodeItem?.rating?.average ?: 0.0 <= 0.0) {
                    "No rating available"
                } else {
                    "Rating ${episodeItem?.rating?.average}"
                }
                episodeRating.text = rating
                episodeSeason.text =
                    "Season ${episodeItem?.season} - Episode ${episodeItem?.number}"
                episodeSummary.text = HtmlCompat.fromHtml(
                    episodeItem?.summary.orEmpty(),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            }
        } ?: findNavController().popBackStack()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val EPISODE_KEY = "episode"
    }
}