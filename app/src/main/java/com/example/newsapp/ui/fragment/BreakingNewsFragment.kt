package com.example.newsapp.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.adapters.CategoryAdapter
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.database.ArticleDatabase
import com.example.newsapp.databinding.FragmentBreakingNewsBinding
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.viewmodel.NewsViewModel
import com.example.newsapp.viewmodel.NewsViewModelFactory


class BreakingNewsFragment : Fragment() {

    private lateinit var binding: FragmentBreakingNewsBinding

    private val sharedViewModel: NewsViewModel by activityViewModels {
        NewsViewModelFactory(
            NewsRepository(ArticleDatabase.getInstance(requireContext()))
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_breaking_news, container, false
        )
        binding.apply {
            viewModel = sharedViewModel
            lifecycleOwner = this@BreakingNewsFragment

            recyclerCategories.adapter = CategoryAdapter (requireContext()) {
                sharedViewModel.getBreakingNews(it)
            }
            recyclerCategories.layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
            )

            recyclerNews.adapter = NewsAdapter {
                val action = BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(it)
                findNavController().navigate(action)
            }
            recyclerNews.layoutManager = LinearLayoutManager(requireContext())
            recyclerNews.addItemDecoration(
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            )
        }

        return binding.root
    }
}