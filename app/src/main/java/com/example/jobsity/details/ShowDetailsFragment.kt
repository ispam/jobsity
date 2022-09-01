package com.example.jobsity.details

import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobsity.R
import com.example.jobsity.common.BaseFragment
import com.example.jobsity.data.local.entities.ShowItem
import com.example.jobsity.databinding.FragmentShowDetailsBinding
import com.example.jobsity.details.adapters.ShowDetailsAdapter
import com.example.jobsity.main_screen.view_model.DetailsState
import com.example.jobsity.main_screen.view_model.ShowViewModel
import com.example.jobsity.utils.convertToJsonString
import com.example.jobsity.utils.observeFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowDetailsFragment : BaseFragment<FragmentShowDetailsBinding>() {

    private lateinit var detailsAdapter: ShowDetailsAdapter

    private val viewModel: ShowViewModel by navGraphViewModels(R.id.main_graph) {
        defaultViewModelProviderFactory
    }

    override fun onViewCreated() {
        setupRecycler()
        arguments?.getInt(SHOW_ID)?.let {
            viewModel.getShowWithEpisodes(it)
        } ?: delayedBlock {
            findNavController().popBackStack()
        }

        observeFlow(viewModel.detailsState, ::onDetailsState)
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
                    with(binding.favoriteImg) {
                        val showItem = state.list.first() as ShowItem
                        setImageDrawable(getCorrectDrawable(showItem.isFavorite))
                        setOnClickListener {
                            viewModel.setFavoriteShow(showItem.id)
                        }
                    }
                }
            }
            is DetailsState.UpdateShow -> {
                detailsAdapter.submitList(state.list)
                val showItem = state.list.first() as ShowItem
                binding.favoriteImg.setImageDrawable(getCorrectDrawable(showItem.isFavorite))
                binding.favoriteImg.invalidate()
            }
            else -> {}
        }
    }

    private fun getCorrectDrawable(isFavorite: Boolean): Drawable? {
        return if (isFavorite) {
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_favorite)
        } else {
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_favorite_empty)
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