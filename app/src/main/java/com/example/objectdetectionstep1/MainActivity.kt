package com.example.objectdetectionstep1

import android.graphics.*
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.detector.ObjectDetector

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnDetect = findViewById<Button>(R.id.btnDetect)
        val imageView = findViewById<ImageView>(R.id.imageView)

        imageView.setImageResource(R.drawable.test_image)

        btnDetect.setOnClickListener {
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.test_image)
            val options = ObjectDetector.ObjectDetectorOptions.builder()
                .setMaxResults(5)
                .setScoreThreshold(0.3f)
                .build()

            val detector = ObjectDetector.createFromFileAndOptions(
                this,
                "ssd_mobilenet_v1.tflite",
                options
            )

            val image = TensorImage.fromBitmap(bitmap)
            val results = detector.detect(image)

            val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
            val canvas = Canvas(mutableBitmap)
            val paint = Paint().apply {
                color = Color.RED
                style = Paint.Style.STROKE
                strokeWidth = 5f
            }
            val textPaint = Paint().apply {
                color = Color.RED
                textSize = 40f
            }

            results.forEach { detection ->
                canvas.drawRect(detection.boundingBox, paint)
                val label = detection.categories.firstOrNull()?.label ?: "Unknown"
                canvas.drawText(label, detection.boundingBox.left, detection.boundingBox.top, textPaint)
            }

            imageView.setImageBitmap(mutableBitmap)
        }
    }
}