package com.example.jobsity.details

import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobsity.R
import com.example.jobsity.common.BaseFragment
import com.example.jobsity.databinding.FragmentShowDetailsBinding
import com.example.jobsity.details.adapters.ShowDetailsAdapter
import com.example.jobsity.details.view_model.DetailsState
import com.example.jobsity.details.view_model.DetailsViewModel
import com.example.jobsity.utils.convertToJsonString
import com.example.jobsity.utils.observeFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowDetailsFragment : BaseFragment<FragmentShowDetailsBinding>() {

    private lateinit var detailsAdapter: ShowDetailsAdapter
    private val detailsViewModel: DetailsViewModel by viewModels()

    override fun onViewCreated() {
        setupRecycler()
        arguments?.getInt(SHOW_ID)?.let {
            detailsViewModel.getShowEpisodes(it)
        } ?: delayedBlock {
            findNavController().popBackStack()
        }
        observeFlow(detailsViewModel.detailsState, ::onDetailsState)
    }

    private fun onDetailsState(state: DetailsState?) {
        when (state) {
            is DetailsState.Loading -> {
                showDialog(requireContext())
            }
            is DetailsState.Error -> {
                delayedBlock {
                    closeDialog()
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.dialog_loading_error),
                        Toast.LENGTH_LONG
                    ).show()
                    findNavController().popBackStack()
                }
            }
            is DetailsState.ShowWithEpisodes -> {
                delayedBlock {
                    closeDialog()
                    detailsAdapter.submitList(state.list)
                }
            }
            else -> {}
        }
    }

    private fun setupRecycler() {
        with(binding.detailsRecycler) {
            detailsAdapter = ShowDetailsAdapter {
                val bundle = bundleOf(EPISODE_KEY to it.convertToJsonString())
                findNavController().navigate(
                    R.id.action_showDetailsFragment_to_episodeDetailsDialogFragment,
                    bundle
                )
            }
            adapter = detailsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    companion object {
        private const val SHOW_ID = "showId"
        private const val EPISODE_KEY = "episode"
    }
}