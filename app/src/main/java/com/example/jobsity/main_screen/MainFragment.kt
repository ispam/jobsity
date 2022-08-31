package com.example.jobsity.main_screen

import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobsity.R
import com.example.jobsity.common.BaseFragment
import com.example.jobsity.databinding.FragmentMainBinding
import com.example.jobsity.main_screen.adapters.MainAdapter
import com.example.jobsity.main_screen.view_model.MainState
import com.example.jobsity.main_screen.view_model.MainViewModel
import com.example.jobsity.utils.observeFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment: BaseFragment<FragmentMainBinding>() {

    private lateinit var mainAdapter: MainAdapter
    private val viewModel: MainViewModel by navGraphViewModels(R.id.main_graph) {
        defaultViewModelProviderFactory
    }

    override fun onViewCreated() {
        setupRecycler()

        observeFlow(viewModel.mainState, ::onMainState)
    }

    private fun onMainState(state: MainState?) {
        when (state) {
            is MainState.Loading -> {
                showDialog(requireContext())
            }
            is MainState.Error -> {
                delayedBlock {
                    closeDialog()
                }
            }
            is MainState.Loaded -> {
                delayedBlock {
                    closeDialog()
                    mainAdapter.submitList(state.list)
                }
            }
            else -> {}
        }
    }

    private fun setupRecycler() {
        binding.mainRecycler.apply {
            mainAdapter = MainAdapter {

            }
            adapter = mainAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}
