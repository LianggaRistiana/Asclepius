package com.dicoding.asclepius.view

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.adapter.HistoryAdaper
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import com.dicoding.asclepius.helper.ViewModelFactory

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding

    //    private lateinit var viewModel: HistoryViewModel
    private val viewModel: HistoryViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
//        val factory = ViewModelFactory.getInstance(this)
//        viewModel = factory.create(HistoryViewModel::class.java)

        setContentView(binding.root)

        val layoutManager =
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                GridLayoutManager(this, 2)
            } else {
                LinearLayoutManager(this)
            }

        binding.rvHistory.apply {
            setHasFixedSize(true)
            setLayoutManager(layoutManager)
        }

        viewModel.getAllResult().observe(this) {
            val adapter = HistoryAdaper {
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra("imageUri", it.imageUri)
                intent.putExtra("result", it.result)
                intent.putExtra("confidence", it.confidence)
                startActivity(intent)
            }
            adapter.submitList(it.reversed())
            binding.rvHistory.adapter = adapter
        }
    }
}