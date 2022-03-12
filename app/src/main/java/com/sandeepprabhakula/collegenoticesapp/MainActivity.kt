package com.sandeepprabhakula.collegenoticesapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.sandeepprabhakula.collegenoticesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = findNavController(R.id.fragmentContainerView2)
        binding.bottomNav.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id){
                R.id.loginFragmnet->{
                    binding.bottomNav.visibility = View.GONE
                }
                else->{
                    binding.bottomNav.visibility = View.VISIBLE
                }
            }
        }
    }
}