package com.example.weatherapptask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.weatherapptask.databinding.ActivityMainBinding
import com.example.weatherapptask.ui.mylocation.MyLocationFragment
import com.example.weatherapptask.ui.search.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var currentFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        val bottomNav: BottomNavigationView = binding.bottomNav
//        val navController = findNavController(R.id.fragment)
//        bottomNav.setupWithNavController(navController)
//        bottomNav.itemIconTintList = null

        supportFragmentManager.beginTransaction().replace(R.id.fragment, MyLocationFragment()).commit()
        val bottomNav: BottomNavigationView = binding.bottomNav
        bottomNav.itemIconTintList = null

        bottomNav.setOnNavigationItemSelectedListener(navListener)
    }

    val navListener = BottomNavigationView.OnNavigationItemSelectedListener {
        when (it.itemId) {
            R.id.location -> {
                currentFragment = MyLocationFragment()
            }
            R.id.search -> {
                currentFragment = SearchFragment()
            }
        }
        supportFragmentManager.beginTransaction().replace(R.id.fragment, currentFragment).commit()
        true
    }
}