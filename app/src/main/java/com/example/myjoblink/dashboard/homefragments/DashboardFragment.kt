package com.example.myjoblink.dashboard.homefragments

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.myjoblink.model.UserData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class DashboardFragment : Fragment(), JobAdapter.onJobClickListener {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: JobAdapter
    private lateinit var recyclerView: RecyclerView
    private var jobArrayList = mutableListOf<JobData>()
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        // Fetch and display user name
        fetchAndDisplayUserName()

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
                    val jobDescription =
                        jobSnapshot.child("jobDescription").getValue(String::class.java)
                    val jobLocation = jobSnapshot.child("jobLocation").getValue(String::class.java)
                    val jobNature = jobSnapshot.child("jobNature").getValue(String::class.java)
                    val jobPay = jobSnapshot.child("jobPay").getValue(String::class.java)
                    val jobTitle = jobSnapshot.child("jobTitle").getValue(String::class.java)
                    val uid = jobSnapshot.child("uid").getValue(String::class.java)

                    if (id != null && imageUrl != null && jobDescription != null && jobLocation != null && jobNature != null && jobPay != null && jobTitle != null && uid != null) {
                        val job = JobData(
                            id,
                            imageUrl,
                            jobTitle,
                            jobLocation,
                            jobNature,
                            jobPay,
                            jobDescription
                        )
                        jobArrayList.add(job)
                    }
                }
                adapter.notifyDataSetChanged()

            }
    }

    private fun fetchAndDisplayUserName() {
        val currentUser = auth.currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            val userRef =
                FirebaseDatabase.getInstance().getReference("registeredUser").child(userId)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        Log.e(TAG, "Exists")
                        val userData = snapshot.getValue(UserData::class.java)
                        Log.e(TAG, userData.toString())
                        if (userData != null) {
                            val userName = userData.name ?: "Unknown"
                            Log.e(TAG, "HomeFragmentUser name retrieved: %s")
                            binding.userName.text = userName
                        } else {
                            Log.e(TAG, "user is null")
                        }
                    } else {
                        Log.e(TAG, "Data snapshot does not exist")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                    Log.e(TAG, "Failed to fetch")
                    Toast.makeText(
                        requireContext(),
                        "Failed to fetch user data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        } else {
            Log.e(TAG, "Current user id is null")
        }
    }

}