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
import com.ajiswn.dicodingevent.R
import com.ajiswn.dicodingevent.databinding.ActivityDetailBinding
import com.ajiswn.dicodingevent.data.remote.response.Event
import com.bumptech.glide.Glide
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()

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
            viewModel.getDetailEvent(eventId)
            observeViewModel()
        }
    }

    private fun observeViewModel() {
        viewModel.eventDetail.observe(this) { event ->
            if (event != null) {
                updateUI(event)
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateUI(event: Event) {
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
}
