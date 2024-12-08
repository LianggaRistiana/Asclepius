package com.dicoding.asclepius.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.helper.ViewModelFactory
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File

class MainActivity : AppCompatActivity(), ImageClassifierHelper.ClassifierListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    //    private lateinit var viewModel: MainViewModel
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        imageClassifierHelper = ImageClassifierHelper(context = this, classifierListener = this)

        currentImageUri = viewModel.getURI()
        viewModel.imageUri.observe(this) { currentImageUri ->
            this.currentImageUri = currentImageUri
        }
        showImage()
        setContentView(binding.root)

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener { analyzeImage() }

        currentImageUri?.let { binding.previewImageView.setOnClickListener { startCropping(currentImageUri!!) } }
        binding.ivHistory.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
//            currentImageUri = UCrop.getOutput(data!!)
            viewModel.setImageUri(UCrop.getOutput(data!!))
            showImage()

        } else if (resultCode == RESULT_CANCELED && requestCode == UCrop.REQUEST_CROP) {
            showToast("Crop canceled")
            showImage()
        } else if (resultCode == UCrop.RESULT_ERROR) {
//            val cropError = UCrop.getError(data!!)
            showToast("Crop error")
        }
    }


    private fun startCropping(uri: Uri) {
        val destinationUri = Uri.fromFile(File(cacheDir, "${System.currentTimeMillis()}"))
        UCrop.of(uri, destinationUri)
            .withAspectRatio(16f, 9f)
            .withMaxResultSize(800, 600)
            .start(this)
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
//            currentImageUri = it
            val chaceImage = Uri.fromFile(File(cacheDir, "${System.currentTimeMillis()}"))
            viewModel.setImageUri(chaceImage)
            startCropping(it)
//            viewModel.imageUri.observe(this) { currentImageUri ->
//                binding.previewImageView.setImageURI(currentImageUri)
//            }
//            showToast(currentImageUri.toString())
            binding.previewImageView.setOnClickListener { startCropping(currentImageUri!!) }
//            showImage()
        } ?: showToast("Image Not Found")
    }

    private fun showImage() {
//        viewModel.imageUri.observe(this) { currentImageUri ->
//            currentImageUri?.let {
//                binding.previewImageView.setImageURI(it)
//            }
//        }
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage() {
//        viewModel.imageUri.observe(this) { currentImageUri ->
//            currentImageUri?.let {
//                imageClassifierHelper.classifyStaticImage(it)
//            } ?: showToast("Image Not Found")
//        }
        currentImageUri?.let {
            imageClassifierHelper.classifyStaticImage(it)
        } ?: showToast("Image Not Found")
    }

    private fun moveToResult(result: String, confidence: Float) {
        val intent = Intent(this, ResultActivity::class.java)
//        viewModel.imageUri.observe(this) { currentImageUri ->
//            intent.putExtra("imageUri", currentImageUri.toString())
//        }
        intent.putExtra("imageUri", currentImageUri.toString())
        intent.putExtra("result", result)
        intent.putExtra("confidence", confidence)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onError(error: String) {
        showToast(error)
    }

    override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
        runOnUiThread {
            results?.let {
                val result = it[0].categories[0]
//                viewModel.imageUri.observe(this) { currentImageUri ->
//                    viewModel.insertResult(currentImageUri.toString(), result.label, result.score)
//                }
                viewModel.insertResult(currentImageUri.toString(), result.label, result.score)
                moveToResult(result.label, result.score)
            }
        }
    }
}