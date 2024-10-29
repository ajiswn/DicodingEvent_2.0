package com.ajiswn.dicodingevent.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ajiswn.dicodingevent.data.remote.response.ListEventsItem
import com.ajiswn.dicodingevent.databinding.ItemEventCardBinding
import com.ajiswn.dicodingevent.ui.detail.DetailActivity
import com.ajiswn.dicodingevent.ui.detail.DetailActivity.Companion.EVENT_ID
import com.ajiswn.dicodingevent.ui.home.HomeFragment
import com.bumptech.glide.Glide

class HorizontalEventAdapter : ListAdapter<ListEventsItem, HorizontalEventAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemEventCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }

    class MyViewHolder(private val binding: ItemEventCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            binding.eventName.text = event.name
            Glide.with(binding.root.context)
                .load(event.imageLogo)
                .into(binding.eventLogo)

            binding.root.setOnClickListener {
                val context = it.context
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(EVENT_ID, event.id)
                context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
