package com.example.myjoblink.dashboard.homefragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myjoblink.R
import com.example.myjoblink.databinding.FragmentDashboardBinding
import com.example.myjoblink.job.JobDescription
import com.example.myjoblink.jobadapter.JobAdapter
import com.example.myjoblink.model.JobData

class DashboardFragment : Fragment(), JobAdapter.onJobClickListener {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: JobAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var jobArrayList : ArrayList<JobData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataInitialize()
        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.listOfJobsRecycler)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = JobAdapter(jobArrayList, this)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

    }

    override fun onJObClick(job: JobData, position: Int) {
        val intent = Intent(requireActivity(), JobDescription::class.java)
        intent.putExtra("title", job.jobTitle)
        intent.putExtra("location", job.jobLocation)
        intent.putExtra("nature", job.jobNature)
        intent.putExtra("pay", job.jobPay)
        startActivity(intent)
    }

    private fun dataInitialize(){
        jobArrayList = arrayListOf(
            JobData("", "", "Construction", "Kasarani", "Part-time", "1500 kenya shilling per day",""),
            JobData("", "", "Medical", "Kasarani", "Part-time", "1500 kenya shilling per day",""),
            JobData("", "", "software eng", "Kasarani", "Part-time", "1500 kenya shilling per day",""),
            JobData("", "", "data analysis", "Kasarani", "Part-time", "1500 kenya shilling per day",""),
            JobData("", "", "Construction", "Kasarani", "Part-time", "1500 kenya shilling per day",""),
            JobData("", "", "data manager", "Kasarani", "Part-time", "1500 kenya shilling per day",""),
            JobData("", "", "Construction", "Kasarani", "Part-time", "1500 kenya shilling per day",""),
            JobData("", "", "Teacher", "Kasarani", "Part-time", "1500 kenya shilling per day","")
        )

    }


}