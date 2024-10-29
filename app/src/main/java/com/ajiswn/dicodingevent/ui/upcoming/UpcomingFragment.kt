package com.ajiswn.dicodingevent.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajiswn.dicodingevent.R
import com.ajiswn.dicodingevent.data.Result
import com.ajiswn.dicodingevent.data.local.entity.EventEntity
import com.ajiswn.dicodingevent.databinding.FragmentUpcomingBinding
import com.ajiswn.dicodingevent.data.remote.response.ListEventsItem
import com.ajiswn.dicodingevent.ui.ViewModelFactory
import com.ajiswn.dicodingevent.ui.adapter.VerticalEventAdapter
import com.google.android.material.snackbar.Snackbar

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("View binding is null!")
    private val viewModel: UpcomingViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }
    private lateinit var eventAdapter: VerticalEventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventAdapter = VerticalEventAdapter()

        binding.rvEvent.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = eventAdapter
        }

        getUpcomingEvent()
    }

    private fun getUpcomingEvent(){
        viewModel.getUpcomingEvent().observe(viewLifecycleOwner) { result ->
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
                        showErrorSnackbar()
                    }
                }
            }
        }
    }

    private fun setEventData(events: List<EventEntity>) {
        val listEventsItems = arrayListOf<ListEventsItem>()
        events.map { event->
            val item = ListEventsItem(
                id = event.id,
                name = event.name,
                summary = event.summary,
                imageLogo = event.imageLogo)
            listEventsItems.add(item)
        }
        eventAdapter.submitList(listEventsItems)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showErrorSnackbar() {
        val snackbar = Snackbar.make(binding.root, getString(R.string.error), Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(R.string.refresh)) { getUpcomingEvent() }
        val bottomNavView = requireActivity().findViewById<View>(R.id.nav_view)
        snackbar.anchorView = bottomNavView
        snackbar.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}