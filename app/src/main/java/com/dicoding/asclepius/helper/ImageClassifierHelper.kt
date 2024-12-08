package com.dicoding.asclepius.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import com.google.android.material.transition.MaterialContainerTransform.ProgressThresholds
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier


class ImageClassifierHelper(
    var thresholds: Float = 0.1f,
    var maxResults: Int = 3,
    val modelName: String = "cancer_classification.tflite",
    val context: Context,
    val classifierListener: ClassifierListener?
) {

    init {
        setupImageClassifier()
    }

    private var imageClassifier: ImageClassifier? = null

    private fun setupImageClassifier() {
        val optionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(thresholds)
            .setMaxResults(maxResults)

        val baseOptionsBuilder = BaseOptions.builder().setNumThreads(4)

        optionsBuilder.setBaseOptions(baseOptionsBuilder.build())

        try {
            imageClassifier = ImageClassifier.createFromFileAndOptions(
                context,
                modelName,
                optionsBuilder.build()
            )
//            classifierListener?.onError("Classifier successfully initialized")
        }catch (e : IllegalStateException) {
            classifierListener?.onError(
                "Image Classifier failed to initialize. See error logs for details"
            )
            Log.e("Image Classifier Helper", "TFLite failed to load model with error: " + e.message)
        }
    }

    fun classifyStaticImage(imageUri: Uri) {
        if (imageClassifier == null){
            setupImageClassifier()
        }

        val imageProcessor = ImageProcessor.Builder()
            .build()

        val tensorImage: TensorImage? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        }.copy(Bitmap.Config.ARGB_8888, true)?.let { bitmap ->
            imageProcessor.process(TensorImage.fromBitmap(bitmap))
        }

        tensorImage?.let { processedImage ->
            var inferenceTime = SystemClock.uptimeMillis()
            val results = imageClassifier?.classify(processedImage)
            inferenceTime = SystemClock.uptimeMillis() - inferenceTime
            classifierListener?.onResults(results, inferenceTime)
        } ?: classifierListener?.onError("Failed to classify image")
    }

    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(
            results: List<Classifications>?,
            inferenceTime: Long
        )

    }

}