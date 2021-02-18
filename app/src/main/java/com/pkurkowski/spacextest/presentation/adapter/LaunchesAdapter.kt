package com.pkurkowski.spacextest.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.pkurkowski.spacextest.R
import com.pkurkowski.spacextest.databinding.ItemLaunchBinding
import com.pkurkowski.spacextest.domain.LaunchData
import com.pkurkowski.spacextest.domain.LaunchStatus
import com.pkurkowski.spacextest.presentation.TimeFormatter

class LaunchesAdapter : RecyclerView.Adapter<LaunchesAdapter.ViewHolder>() {

    private var data: List<LaunchData> = emptyList()

    fun update(newData: List<LaunchData>) {
        data = newData
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLaunchBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: ItemLaunchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(launch: LaunchData) {
            if (binding.launch == launch) return
            binding.launch = launch
            binding.statusImage.setImageResource(
                when (launch.launchStatus) {
                    is LaunchStatus.Upcoming -> R.drawable.ic_baseline_question_24
                    LaunchStatus.Fail -> R.drawable.ic_baseline_fail_24
                    LaunchStatus.Success -> R.drawable.ic_baseline_done_24
                }
            )

            binding.sinceFromTextView.text =
                TimeFormatter.getSinceFromText(launch.date, binding.root.resources)

            Glide.with(binding.pathImage)
                .load(launch.missionPathUrl)
                .placeholder(R.drawable.ic_baseline_loop_24)
                .error(R.drawable.ic_baseline_error_outline_24)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(binding.pathImage)

        }
    }


}