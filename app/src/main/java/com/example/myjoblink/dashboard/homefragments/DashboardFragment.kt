package com.example.myjoblink.dashboard.homefragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myjoblink.R
import com.example.myjoblink.databinding.FragmentDashboardBinding
import com.example.myjoblink.job.JobDescription
import com.example.myjoblink.jobadapter.JobAdapter
import com.example.myjoblink.model.JobData
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class DashboardFragment : Fragment(), JobAdapter.onJobClickListener {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: JobAdapter
    private lateinit var recyclerView: RecyclerView
    private var jobArrayList = mutableListOf<JobData>()

    private lateinit var database: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getJobs()

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
        intent.putExtra("image", job.jobUrl.toString())
        intent.putExtra("title", job.jobTitle)
        intent.putExtra("location", job.jobLocation)
        intent.putExtra("nature", job.jobNature)
        intent.putExtra("pay", job.jobPay)
        intent.putExtra("description", job.jobDescription)
        startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getJobs() {
        database = Firebase.database.reference
        database.child("Jobs").get()
            .addOnSuccessListener { dataSnapshot ->
                for (jobSnapshot in dataSnapshot.children) {
                    val id = jobSnapshot.child("id").getValue(String::class.java)
                    val imageUrl = jobSnapshot.child("imageUrl").getValue(String::class.java)
                    val jobDescription = jobSnapshot.child("jobDescription").getValue(String::class.java)
                    val jobLocation = jobSnapshot.child("jobLocation").getValue(String::class.java)
                    val jobNature = jobSnapshot.child("jobNature").getValue(String::class.java)
                    val jobPay = jobSnapshot.child("jobPay").getValue(String::class.java)
                    val jobTitle = jobSnapshot.child("jobTitle").getValue(String::class.java)
                    val uid = jobSnapshot.child("uid").getValue(String::class.java)

                    if (id != null && imageUrl != null && jobDescription != null && jobLocation != null && jobNature != null && jobPay != null && jobTitle != null && uid != null) {
                        val job = JobData(id, imageUrl, jobTitle, jobLocation, jobNature, jobPay, jobDescription)
                        jobArrayList.add(job)
                    }
                }
                adapter.notifyDataSetChanged()

            }
    }





}