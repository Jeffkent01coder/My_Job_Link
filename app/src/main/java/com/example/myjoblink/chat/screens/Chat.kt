package com.example.myjoblink.chat.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myjoblink.auth.Login
import com.example.myjoblink.chat.adapter.ChatAdapter
import com.example.myjoblink.chat.model.Message
import com.example.myjoblink.databinding.ActivityChatBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Chat : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    private lateinit var adapter: ChatAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private val messagesList = mutableListOf<Message>() // Define messagesList here


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityChatBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance() // Corrected line

        if (auth.currentUser == null) {
            // User not logged in, navigate to login screen
            startActivity(Intent(this, Login::class.java))
            finish()
            return
        }

        database = FirebaseDatabase.getInstance().reference.child("messages")

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        adapter = ChatAdapter(messagesList, auth.currentUser!!.uid)
        binding.recyclerView.adapter = adapter

        binding.sendButton.setOnClickListener {
            val messageText = binding.editText.text.toString()
            sendMessage(messageText)
        }

        listenForMessages()
    }
    private fun sendMessage(messageText: String) {
        val senderId = auth.currentUser!!.uid
        val receiverId = "" // Implement logic to get receiver ID

        val message = Message(messageText, senderId, receiverId)
        database.push().setValue(message)
        binding.editText.text.clear()
    }

    private fun listenForMessages() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messagesList.clear()
                for (messageSnapshot in snapshot.children) {
                    val message = messageSnapshot.getValue(Message::class.java)
                    if (message != null) {
                        messagesList.add(message)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                Toast.makeText(this@Chat,"Error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}