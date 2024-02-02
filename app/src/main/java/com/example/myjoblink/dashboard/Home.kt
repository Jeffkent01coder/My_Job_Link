package com.example.myjoblink.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.myjoblink.R
import com.example.myjoblink.dashboard.homefragments.DashboardFragment
import com.example.myjoblink.dashboard.homefragments.ProfileFragment
import com.example.myjoblink.databinding.ActivityHomeBinding

class Home : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        replaceFragment(DashboardFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.icShop -> replaceFragment(DashboardFragment())
                R.id.icCategories -> replaceFragment(ProfileFragment())

                else -> {
                }
            }
            true
        }
        if (resources.getColor(R.color.background_tint_dark) == resources.getColor(R.color.background_tint_dark)) {
            binding.bottomNavigationView.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.background_tint_dark
                )
            )
        } else {
            binding.bottomNavigationView.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.background_tint_light
                )
            )


        }
    }
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }
}