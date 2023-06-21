package com.assignment.products.activities

import android.os.Bundle
import androidx.navigation.Navigation
import com.assignment.products.R
import com.assignment.products.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {
    var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
    }
}