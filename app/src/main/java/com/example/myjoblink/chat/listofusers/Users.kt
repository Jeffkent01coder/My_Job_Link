package com.example.myjoblink.chat.listofusers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myjoblink.chat.adapter.UserAdapter
import com.example.myjoblink.chat.screens.Chat
import com.example.myjoblink.databinding.ActivityUsersBinding
import com.example.myjoblink.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Users : AppCompatActivity(), UserAdapter.OnUserClickListener {
    private lateinit var binding: ActivityUsersBinding
    private lateinit var adapter: UserAdapter
    private lateinit var recyclerView: RecyclerView
    private var userChatArrayList = mutableListOf<UserData>()

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUsersBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()


        // Initialize RecyclerView
        val layoutManager = LinearLayoutManager(this)
        recyclerView = binding.doctorsRecycler
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = UserAdapter(userChatArrayList, this)
        recyclerView.adapter = adapter

        // Fetch users from Firebase
        getUsers()

        binding.back.setOnClickListener {
            onBackPressed()
        }
    }

    // Function to fetch users from Firebase
    private fun getUsers() {
        val currentUserUid = auth.currentUser?.uid

        val ref = FirebaseDatabase.getInstance().getReference("registeredUser")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userChatArrayList.clear() // Clear existing data
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(UserData::class.java)
                    user?.let {
                        if (it.id != currentUserUid) { // Check if the user is not the current user
                            userChatArrayList.add(it) // Add user to the list
                        }
                    }
                }
                adapter.notifyDataSetChanged() // Notify adapter of data change
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Error fetching users: ${error.message}")
                Toast.makeText(
                    applicationContext,
                    "Error fetching users: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    override fun onUserClick(user: UserData, position: Int) {
        val intent = Intent(this, Chat::class.java)
        intent.putExtra("id", user.id)
        intent.putExtra("name", user.name)
        startActivity(intent)
    }

    companion object {
        private const val TAG = "UsersActivity"
    }
}
