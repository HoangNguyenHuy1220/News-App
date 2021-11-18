package com.example.newsapp.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.database.ArticleDatabase
import com.example.newsapp.databinding.FragmentSearchNewsBinding
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.viewmodel.NewsViewModel
import com.example.newsapp.viewmodel.NewsViewModelFactory
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class SearchNewsFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var binding: FragmentSearchNewsBinding

    private val sharedViewModel: NewsViewModel by activityViewModels {
        NewsViewModelFactory(
            NewsRepository(ArticleDatabase.getInstance(requireContext()))
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_search_news, container, false
        )
        binding.apply {
            viewModel = sharedViewModel
            lifecycleOwner = this@SearchNewsFragment

            recyclerSearchNews.adapter = NewsAdapter {
                val action = SearchNewsFragmentDirections.actionSearchNewsFragmentToArticleFragment(it)
                findNavController().navigate(action)
            }
            recyclerSearchNews.layoutManager = LinearLayoutManager(requireContext())
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)

        val search = menu.findItem(R.id.search_news)
        val searchView = search.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.calendar -> calendarDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun calendarDialog() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.calendar))
                .build()
        datePicker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.time = Date(it)
            getNewsByDate(calendar.time)
        }
        datePicker.addOnCancelListener {
            datePicker.dismiss()
        }
        datePicker.show(parentFragmentManager, "adsf")

    }

    @SuppressLint("SimpleDateFormat")
    private fun getNewsByDate(date: Date) {
        sharedViewModel.getNewspaper(SimpleDateFormat("yyyy-MM-dd").format(date))
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()) {
            sharedViewModel.searchNews(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (!query.isNullOrEmpty()) {
            sharedViewModel.searchNews(query)
        }
        return true
    }

}