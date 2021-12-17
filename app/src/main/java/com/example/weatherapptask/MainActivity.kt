package com.example.weatherapptask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.weatherapptask.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val bottomNav: BottomNavigationView = binding.bottomNav
        val navController = findNavController(R.id.fragment)
        bottomNav.setupWithNavController(navController)
        bottomNav.itemIconTintList = null
    }
}