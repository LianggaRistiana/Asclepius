package com.dicoding.asclepius.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.data.local.entity.ClassificationResultEntity
import com.dicoding.asclepius.databinding.HistoryItemBinding
import java.io.File
import java.text.NumberFormat

class HistoryAdaper(private val onItemClicked: (ClassificationResultEntity) -> Unit):
    ListAdapter<ClassificationResultEntity, HistoryAdaper.ViewHolder>(DIFF_CALLBACK) {
    inner class ViewHolder(private val binding: HistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ClassificationResultEntity) {
            itemView.setOnClickListener {
                onItemClicked(item)
            }
            with(binding) {
                tvLabel.text = item.result
                tvConfidence.text = NumberFormat.getPercentInstance().format(item.confidence).trim()
                val uriString = item.imageUri  // String yang berisi URI
                val uri = Uri.parse(uriString)

                val file = File(uri.path ?: "")
                if (file.exists()) {
                    ivHistory.setImageURI(uri)  // Menampilkan gambar jika file ada
                }
                ivHistory.setImageURI(item.imageUri.toUri())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ClassificationResultEntity>() {
            override fun areItemsTheSame(
                oldItem: ClassificationResultEntity,
                newItem: ClassificationResultEntity
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ClassificationResultEntity,
                newItem: ClassificationResultEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}