package com.example.jobsity.main_screen

import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobsity.R
import com.example.jobsity.common.BaseFragment
import com.example.jobsity.databinding.FragmentMainBinding
import com.example.jobsity.main_screen.adapters.ShowAdapter
import com.example.jobsity.main_screen.view_model.ShowState
import com.example.jobsity.main_screen.view_model.ShowViewModel
import com.example.jobsity.utils.observeFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>() {

    private lateinit var showAdapter: ShowAdapter
    private val viewModel: ShowViewModel by navGraphViewModels(R.id.main_graph) {
        defaultViewModelProviderFactory
    }

    override fun onViewCreated() {
        setupRecycler()

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_searchFragment)
        }
        binding.favoriteImg.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_favoritesFragment)
        }
        observeFlow(viewModel.showState, ::onShowState)
    }

    private fun onShowState(state: ShowState?) {
        when (state) {
            is ShowState.Loading -> {
                showDialog(requireContext())
            }
            is ShowState.Error -> {
                delayedBlock {
                    closeDialog()
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.dialog_loading_error),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            is ShowState.ShowsLoaded -> {
                delayedBlock {
                    closeDialog()
                    showAdapter.submitList(state.list)
                }
            }
            else -> {}
        }
    }

    private fun setupRecycler() {
        binding.mainRecycler.apply {
            showAdapter = ShowAdapter {
                val bundle = bundleOf(SHOW_ID to it.id)
                findNavController().navigate(R.id.action_global_showDetailsFragment, bundle)
            }
            adapter = showAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    companion object {
        private const val SHOW_ID = "showId"
    }
}
