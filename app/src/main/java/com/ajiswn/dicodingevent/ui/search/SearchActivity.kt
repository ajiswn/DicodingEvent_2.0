package com.ajiswn.dicodingevent.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajiswn.dicodingevent.data.remote.response.ListEventsItem
import com.ajiswn.dicodingevent.databinding.ActivitySearchBinding
import com.ajiswn.dicodingevent.ui.adapter.VerticalEventAdapter

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var eventAdapter: VerticalEventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Search Event"

        setupRecyclerView()
        viewModel.searchResults.observe(this) { events ->
            setEventData(events)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.errorMessage.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
        setupSearchBar()
    }

    private fun setupRecyclerView() {
        eventAdapter = VerticalEventAdapter()
        binding.rvSearchEvent.layoutManager = LinearLayoutManager(this)
        binding.rvSearchEvent.adapter = eventAdapter
    }

    private fun setEventData(events: List<ListEventsItem>) {
        eventAdapter.submitList(events)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupSearchBar() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                val keyword = searchView.text.toString()
                searchEvent(keyword)
                searchBar.setText(keyword)
                searchView.hide()
                false
            }
        }
    }


    private fun searchEvent(keyword: String) {
        viewModel.searchEvent(keyword)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
