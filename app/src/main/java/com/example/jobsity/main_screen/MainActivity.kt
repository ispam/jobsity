package com.example.jobsity.main_screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jobsity.databinding.ActivityMainBinding
import com.example.jobsity.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}
