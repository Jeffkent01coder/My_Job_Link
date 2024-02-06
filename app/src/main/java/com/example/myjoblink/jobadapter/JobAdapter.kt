package com.example.myjoblink.jobadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myjoblink.databinding.JobItemBinding
import com.example.myjoblink.model.JobData

class JobAdapter(private val list: MutableList<JobData>, val clickListener: onJobClickListener) :
    RecyclerView.Adapter<JobAdapter.JobViewHolder>() {

    inner class JobViewHolder(val jobItemBinding: JobItemBinding) :
        RecyclerView.ViewHolder(jobItemBinding.root) {
        fun setData(job: JobData, action: onJobClickListener) {
            jobItemBinding.apply {
                jobTitle.text = job.jobTitle
                jobLocation.text = job.jobLocation
                jobNature.text = job.jobNature
                jobPay.text = job.jobPay
            }
            jobItemBinding.root.setOnClickListener {
                action.onJObClick(job, adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        return JobViewHolder(
            JobItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = list[position]
        holder.setData(job, clickListener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface onJobClickListener {
        fun onJObClick(job: JobData, position: Int)
    }

}