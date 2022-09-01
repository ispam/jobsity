package com.example.jobsity.search

import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobsity.R
import com.example.jobsity.common.BaseFragment
import com.example.jobsity.data.local.entities.ShowItem
import com.example.jobsity.databinding.FragmentSearchBinding
import com.example.jobsity.main_screen.adapters.ShowAdapter
import com.example.jobsity.main_screen.view_model.ShowViewModel
import com.example.jobsity.utils.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    private lateinit var showAdapter: ShowAdapter
    private val viewModel: ShowViewModel by navGraphViewModels(R.id.main_graph) {
        defaultViewModelProviderFactory
    }

    override fun onViewCreated() {
        setupRecycler()
        binding.searchView.clearFocus()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query != null) {
                    viewModel.searchShowName(query)
                }
                return false
            }
        })

        observe(viewModel.searchList, ::onSearchList)
    }

    private fun onSearchList(list: List<ShowItem>) {
        showAdapter.submitList(list)
    }

    private fun setupRecycler() {
        binding.searchRecycler.apply {
            showAdapter = ShowAdapter {
                val bundle = bundleOf(SHOW_ID to it.id)
                findNavController().navigate(
                    R.id.action_searchFragment_to_showDetailsFragment,
                    bundle
                )
            }
            adapter = showAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    companion object {
        private const val SHOW_ID = "showId"
    }
}