package com.example.newsapp.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.newsapp.R
import com.example.newsapp.database.ArticleDatabase
import com.example.newsapp.databinding.FragmentArticleBinding
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.viewmodel.NewsViewModel
import com.example.newsapp.viewmodel.NewsViewModelFactory
import com.google.android.material.snackbar.Snackbar

class ArticleFragment : Fragment() {

    private lateinit var binding: FragmentArticleBinding

    private val args : ArticleFragmentArgs by navArgs()

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
            inflater, R.layout.fragment_article, container, false
        )
        binding.article = args.article
        return binding.root
    }

    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.share_menu, menu)

        if (getShareIntent().resolveActivity(requireActivity().packageManager) == null) {
            menu.findItem(R.id.share_news).isVisible = false
        }

        setSaveArticleIcon(menu.findItem(R.id.save_news))
    }

    private fun setSaveArticleIcon(findItem: MenuItem?) {
        findItem?.icon =
            if (sharedViewModel.isArticleSaved(args.article)) {
                ContextCompat.getDrawable(requireContext(), R.drawable.icon_save)
            } else {
                ContextCompat.getDrawable(requireContext(), R.drawable.icon_unsave)
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share_news -> startActivity(getShareIntent())
            else -> onSaveArticle(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getShareIntent(): Intent {
        return Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT,  args.article.url)
        }
    }

    private fun onSaveArticle(findItem: MenuItem) {
        val article = args.article
        if (sharedViewModel.isArticleSaved(article)) {
            sharedViewModel.deleteArticle(article)
            findItem.icon = ContextCompat.getDrawable(requireContext(), R.drawable.icon_unsave)
            Snackbar.make(requireView(), getString(R.string.delete_article), Snackbar.LENGTH_LONG).show()
        } else {
            sharedViewModel.saveArticle(article)
            ContextCompat.getDrawable(requireContext(), R.drawable.icon_save)
            Snackbar.make(requireView(), getString(R.string.save_article), Snackbar.LENGTH_LONG).show()
        }
    }

}