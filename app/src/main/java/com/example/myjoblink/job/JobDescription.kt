package com.example.myjoblink.job

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myjoblink.R
import com.example.myjoblink.databinding.ActivityJobDescriptionBinding
import java.net.URI

class JobDescription : AppCompatActivity() {
    private lateinit var binding : ActivityJobDescriptionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityJobDescriptionBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.apply {
            intent
            selectImage.setImageResource(intent.getStringExtra("image")!!.toInt())
             jobTitle.text = intent.getStringExtra("title")
             jobLocation.text = intent.getStringExtra("location")
             jobNature.text = intent.getStringExtra("nature")
             jobPay.text = intent.getStringExtra("pay")
            jobDescription.text = intent.getStringExtra("description")
        }


    }
}