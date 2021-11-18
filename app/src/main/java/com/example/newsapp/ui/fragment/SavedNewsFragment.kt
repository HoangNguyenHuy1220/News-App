package com.example.newsapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.adapters.SavedNewsAdapter
import com.example.newsapp.database.ArticleDatabase
import com.example.newsapp.databinding.FragmentSavedNewsBinding
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.viewmodel.NewsViewModel
import com.example.newsapp.viewmodel.NewsViewModelFactory
import com.google.android.material.snackbar.Snackbar

class SavedNewsFragment : Fragment() {

    private lateinit var binding: FragmentSavedNewsBinding

    private lateinit var adapter: SavedNewsAdapter

    private val sharedViewModel: NewsViewModel by activityViewModels {
        NewsViewModelFactory(
            NewsRepository(ArticleDatabase.getInstance(requireContext()))
        )
    }

    private val itemTouchHelper by lazy {
        val simpleCallBack = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = adapter.currentList[viewHolder.adapterPosition]
                sharedViewModel.deleteArticle(item)
                Snackbar.make(requireView(), getString(R.string.delete_article), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.undo)) {
                        sharedViewModel.saveArticle(item)
                    }
                    .show()
            }

        }
        ItemTouchHelper(simpleCallBack)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_saved_news, container, false
        )
        binding.apply {
            viewModel = sharedViewModel
            lifecycleOwner = this@SavedNewsFragment

            adapter = SavedNewsAdapter {
                val action = SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleFragment(it)
                findNavController().navigate(action)
            }
            recyclerSavedNews.adapter = adapter
            recyclerSavedNews.layoutManager = LinearLayoutManager(requireContext())
            recyclerSavedNews.addItemDecoration(
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            )
            itemTouchHelper.attachToRecyclerView(recyclerSavedNews)
        }
        return binding.root
    }

}