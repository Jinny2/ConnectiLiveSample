package com.kakaovx.practice.connectilivesample.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kakaovx.practice.connectilivesample.R
import com.kakaovx.practice.connectilivesample.databinding.ActivityMainBinding
import com.remotemonster.sdk.Remon
import com.remotemonster.sdk.RemonCast

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}