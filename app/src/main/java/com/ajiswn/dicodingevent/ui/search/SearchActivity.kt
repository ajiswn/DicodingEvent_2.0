package com.ajiswn.dicodingevent.ui.search

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajiswn.dicodingevent.R
import com.ajiswn.dicodingevent.data.Result
import com.ajiswn.dicodingevent.data.local.entity.EventEntity
import com.ajiswn.dicodingevent.data.remote.response.ListEventsItem
import com.ajiswn.dicodingevent.databinding.ActivitySearchBinding
import com.ajiswn.dicodingevent.ui.ViewModelFactory
import com.ajiswn.dicodingevent.ui.adapter.VerticalEventAdapter
import com.google.android.material.snackbar.Snackbar

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var eventAdapter: VerticalEventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Search Event"

        setupRecyclerView()
        setupSearchBar()
    }

    private fun setupRecyclerView() {
        eventAdapter = VerticalEventAdapter()
        binding.rvSearchEvent.layoutManager = LinearLayoutManager(this)
        binding.rvSearchEvent.adapter = eventAdapter
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
        viewModel.searchEvent(keyword).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        setEventData(result.data)
                    }
                    is Result.Error -> {
                        showLoading(false)
                        val snackbar = Snackbar.make(binding.root, getString(R.string.error) + result.error, Snackbar.LENGTH_INDEFINITE)
                            .setAction(getString(R.string.refresh)) { searchEvent(keyword) }
                        val btn = this.findViewById<View>(R.id.btnRegister)
                        snackbar.anchorView = btn
                        snackbar.show()
                    }
                }
            }
        }
    }

    private fun setEventData(events: List<EventEntity>) {
        val listEventsItems = events.map { event ->
            ListEventsItem(
                id = event.id,
                name = event.name,
                summary = event.summary,
                imageLogo = event.imageLogo
            )
        }
        eventAdapter.submitList(listEventsItems)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}

