package com.dicoding.asclepius.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.databinding.ActivityDetailNewsBinding

class DetailNewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("title")
        val author = intent.getStringExtra("author")
        val content = intent.getStringExtra("content")
        val urlToImage = intent.getStringExtra("urlToImage")
        val url = intent.getStringExtra("url")

        with(binding){
            tvTitle.text = title
            tvAuthor.text = author
            tvContent.text = content
            fabWeb.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }
        }

        Glide.with(this)
            .load(urlToImage)
            .placeholder(R.drawable.error_image)
            .error(R.drawable.error_image)
            .into(binding.ivHero)
    }

}
