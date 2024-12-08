package com.dicoding.asclepius.view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.adapter.NewsAdapter
import com.dicoding.asclepius.data.Result
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.helper.ViewModelFactory
import java.io.File
import java.text.NumberFormat

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    //    private lateinit var viewModel: ResultViewModel
    private val viewModel: ResultViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
//        val factory = ViewModelFactory.getInstance(this)
//        viewModel = factory.create(ResultViewModel::class.java)

        setContentView(binding.root)

        val imageUri = intent.getStringExtra("imageUri")
        val result = intent.getStringExtra("result")
        val confidence = intent.getFloatExtra("confidence", 0.0f)

        val uri = Uri.parse(imageUri)
        val file = File(uri.path ?: "")
        if (file.exists()) {
            binding.resultImage.setImageURI(uri) // Menampilkan gambar jika file ada
        }

//        val uri = Uri.parse(imageUri)
//        try {

//        }finally {
//
//        }

        binding.resultImage.setImageURI(Uri.parse(imageUri))
        binding.resultText.text = "$result ${
            NumberFormat.getPercentInstance()
                .format(confidence).trim()
        }"

        val layoutManager =
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                GridLayoutManager(this, 2)
            } else {
                LinearLayoutManager(this)
            }

        binding.rvNews.apply {
            setHasFixedSize(true)
            setLayoutManager(layoutManager)
        }

        viewModel.getNews().observe(this) { result ->
            result?.let {
                when (it) {
                    is Result.Error -> {
                        showLoading(false)
                        Log.d("TAG", "onViewCreated: ${it.error}")
                        showToast(it.error)
                    }

                    Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        val newsAdapter = NewsAdapter {
                            val intent = Intent(this, DetailNewsActivity::class.java)
                            intent.putExtra("title", it.title)
                            intent.putExtra("author", it.author.toString())
                            intent.putExtra("content", it.content)
                            intent.putExtra("urlToImage", it.urlToImage)
                            intent.putExtra("url", it.url)

                            startActivity(intent)
//
//                            val intent = Intent(Intent.ACTION_VIEW)
//                            intent.data = Uri.parse(it.url)
//                            startActivity(intent)
                        }
                        newsAdapter.submitList(it.data)
                        binding.rvNews.adapter = newsAdapter
                    }
                }
            }

        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}