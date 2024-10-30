package com.ajiswn.dicodingevent.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.fragment.app.viewModels
import com.ajiswn.dicodingevent.R
import com.ajiswn.dicodingevent.data.Result
import com.ajiswn.dicodingevent.data.local.entity.EventEntity
import com.ajiswn.dicodingevent.databinding.ActivityDetailBinding
import com.ajiswn.dicodingevent.data.remote.response.Event
import com.ajiswn.dicodingevent.ui.ViewModelFactory
import com.ajiswn.dicodingevent.ui.upcoming.UpcomingViewModel
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    companion object {
        const val EVENT_ID = "EVENT_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = getString(R.string.title_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventId = intent.getIntExtra(EVENT_ID, -1)
        if (eventId != -1) {
            getDetailEvent(eventId)
        }
    }

    private fun getDetailEvent(id: Int){
        viewModel.getDetailEvent(id).observe(this) { result ->
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
                            .setAction(getString(R.string.refresh)) { getDetailEvent(id) }
                        val btn = this.findViewById<View>(R.id.btnRegister)
                        snackbar.anchorView = btn
                        snackbar.show()
                    }
                }
            }
        }
    }

    private fun setEventData(event: EventEntity) {
        with(binding) {
            ivMediaCover.loadImage(event.mediaCover)
            tvName.text = event.name
            tvCategory.text = event.category
            tvOwnerName.text = event.ownerName
            tvBeginTime.text = event.beginTime?.let { formatDate(it) }
            tvQuota.text = "${event.quota?.minus(event.registrants!!)}"
            tvDescription.text = event.description?.let {
                HtmlCompat.fromHtml(
                    it,
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            }
            tvScheduleStart.text = event.beginTime
            tvScheduleEnd.text = event.endTime
            tvCityName.text = event.cityName

            val link = event.link
            btnRegister.setOnClickListener {
                val web = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(web)
            }
        }
    }

    @SuppressLint("NewApi")
    private fun formatDate(inputDate: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val outputFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
        return LocalDateTime.parse(inputDate, inputFormatter).format(outputFormatter)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun ImageView.loadImage(url: String?) {
        Glide.with(this.context)
            .load(url)
            .centerCrop()
            .into(this)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
