package com.example.jobsity.favorites

import android.widget.Toast
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobsity.R
import com.example.jobsity.common.BaseFragment
import com.example.jobsity.data.local.entities.ShowItem
import com.example.jobsity.databinding.FragmentFavoritesBinding
import com.example.jobsity.main_screen.adapters.ShowAdapter
import com.example.jobsity.main_screen.view_model.ShowViewModel
import com.example.jobsity.utils.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : BaseFragment<FragmentFavoritesBinding>() {

    private val viewModel: ShowViewModel by navGraphViewModels(R.id.main_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var showAdapter: ShowAdapter

    override fun onViewCreated() {
        setupRecycler()

        viewModel.getAllFavorites()
        observe(viewModel.favoriteList, ::onFavoriteList)
    }

    private fun onFavoriteList(list: List<ShowItem>) {
        if (list.isEmpty()) {
            Toast.makeText(
                requireContext(),
                getString(R.string.favorites_empty),
                Toast.LENGTH_LONG
            ).show()
            return
        }
        showAdapter.submitList(list)
    }

    private fun setupRecycler() {
        with(binding.favoritesRecycler) {
            showAdapter = ShowAdapter {
                val listToUpdate = showAdapter.currentList.toMutableList()
                val index = listToUpdate.indexOf(it)
                if (index >= 0) {
                    listToUpdate.removeAt(index)
                }
                showAdapter.submitList(listToUpdate)
            }
            adapter = showAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}