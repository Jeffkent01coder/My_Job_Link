package com.example.myjoblink.dashboard.homefragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myjoblink.auth.Login
import com.example.myjoblink.databinding.FragmentProfileBinding
import com.example.myjoblink.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("registeredUser")
        getData()

        binding.btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun getData() {
        val uid = auth.currentUser?.uid
        if (uid.isNullOrEmpty()) {
            // User not authenticated
            return
        }

        // Get a reference to the user data in the database
        val ref = database.child(uid)

        // Retrieve user data from the database
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // User data found, parse it and display in the UI
                    val userData = snapshot.getValue(UserData::class.java)
                    userData?.let {
                        // Update UI with user data
                        binding.userName.text = it.name
                        binding.userEmail.text = it.email
                        binding.userPhone.text = it.phone
                    }
                } else {
                    // User data not found
                    Toast.makeText(requireContext(), "User data not found", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Error fetching user data
                Log.e(TAG, "Failed to fetch user data: ${error.message}")
                Toast.makeText(requireContext(), "Failed to fetch user data", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun logout() {
        auth.signOut()
        // Redirect to LoginActivity
        val intent = Intent(requireContext(), Login::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    companion object {
        private const val TAG = "ProfileFragment"
    }


}