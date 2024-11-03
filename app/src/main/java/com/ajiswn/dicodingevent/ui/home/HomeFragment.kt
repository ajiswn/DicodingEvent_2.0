package com.ajiswn.dicodingevent.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajiswn.dicodingevent.R
import com.ajiswn.dicodingevent.data.Result
import com.ajiswn.dicodingevent.data.local.entity.EventEntity
import com.ajiswn.dicodingevent.data.remote.response.ListEventsItem
import com.ajiswn.dicodingevent.databinding.FragmentHomeBinding
import com.ajiswn.dicodingevent.ui.HomeActivity
import com.ajiswn.dicodingevent.ui.ViewModelFactory
import com.ajiswn.dicodingevent.ui.adapter.HorizontalEventAdapter
import com.ajiswn.dicodingevent.ui.adapter.VerticalEventAdapter
import com.ajiswn.dicodingevent.ui.setting.SettingPreferences
import com.ajiswn.dicodingevent.ui.setting.SettingViewModel
import com.ajiswn.dicodingevent.ui.setting.dataStore
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("View binding is null!")
    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }
    private lateinit var upcomingEventAdapter: HorizontalEventAdapter
    private lateinit var finishedEventAdapter: VerticalEventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val settingViewModel: SettingViewModel by viewModels {
            com.ajiswn.dicodingevent.ui.setting.ViewModelFactory(
                SettingPreferences.getInstance(requireActivity().application.dataStore)
            )
        }

        settingViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        upcomingEventAdapter = HorizontalEventAdapter()
        getUpcomingEvents()
        binding.rvUpcomingEvent.apply {
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL,false)
            adapter = upcomingEventAdapter
        }


        finishedEventAdapter = VerticalEventAdapter()
        getFinishedEvents()
        binding.rvFinishedEvent.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = finishedEventAdapter
        }
    }

    private fun getUpcomingEvents() {
        viewModel.getUpcomingEvent().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    showLoading(false)
                    Log.d("HomeFragment", "Upcoming events count: ${result.data.size}")
                    setUpcomingEventData(result.data)
                }
                is Result.Error -> {
                    showLoading(false)
                    showErrorSnackbar(result.error)
                }
            }
        }
    }

    private fun getFinishedEvents() {
        viewModel.getFinishedEvent().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    showLoading(false)
                    setFinishedEventData(result.data)
                }
                is Result.Error -> {
                    showLoading(false)
                    showErrorSnackbar(result.error)
                }
            }
        }
    }

    private fun setUpcomingEventData(events: List<EventEntity>) {
        val listEventsItems = events.take(5).map { event ->
            ListEventsItem(
                id = event.id,
                name = event.name,
                imageLogo = event.imageLogo
            )
        }
        upcomingEventAdapter.submitList(listEventsItems)
    }

    private fun setFinishedEventData(events: List<EventEntity>) {
        val listEventsItems = events.take(5).map { event -> // Limit to 5 finished events
            ListEventsItem(
                id = event.id,
                name = event.name,
                summary = event.summary,
                imageLogo = event.imageLogo
            )
        }
        finishedEventAdapter.submitList(listEventsItems)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showErrorSnackbar(error: String) {
        val snackbar = Snackbar.make(binding.root, getString(R.string.error) + error, Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(R.string.refresh)) {
                getUpcomingEvents()
                getFinishedEvents()
            }
        val bottomNavView = requireActivity().findViewById<View>(R.id.nav_view)
        snackbar.anchorView = bottomNavView
        snackbar.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
