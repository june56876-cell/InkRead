
package com.inkread.app

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class MainActivity : Activity() {

    private lateinit var result: TextView
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 32, 24, 24)
            setBackgroundColor(Color.rgb(18, 18, 24))
        }

        val title = TextView(this).apply {
            text = "InkRead"
            textSize = 32f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
        }

        val subtitle = TextView(this).apply {
            text = "Read your handwritten notes"
            textSize = 16f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 12, 0, 24)
        }

        val pickButton = Button(this).apply {
            text = "Select Notes Photo"
            setOnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                    type = "image/*"
                }
                startActivityForResult(intent, 100)
            }
        }

        status = TextView(this).apply {
            text = "Choose a photo to begin."
            textSize = 16f
            setTextColor(Color.LTGRAY)
            setPadding(0, 20, 0, 12)
        }

        result = TextView(this).apply {
            text = "Your extracted text will appear here."
            textSize = 18f
            setTextColor(Color.WHITE)
            setTextIsSelectable(true)
        }

        val scroll = ScrollView(this).apply {
            addView(result)
        }

        layout.addView(title)
        layout.addView(subtitle)
        layout.addView(pickButton)
        layout.addView(status)
        layout.addView(
            scroll,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        setContentView(layout)
    }

    @Deprecated("Use Activity Result APIs in a future update")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 &&
            resultCode == RESULT_OK &&
            data?.data != null
        ) {
            recognizeText(data.data!!)
        }
    }

    private fun recognizeText(uri: Uri) {
        status.text = "Reading your notes..."
        result.text = ""

        try {
            val image = InputImage.fromFilePath(this, uri)
            val recognizer = TextRecognition.getClient(
                TextRecognizerOptions.DEFAULT_OPTIONS
            )

            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    result.text = visionText.text.ifBlank {
                        "No text detected. Try a clearer photo."
                    }
                    status.text = "Text recognition complete."
                    recognizer.close()
                }
                .addOnFailureListener { error ->
                    status.text = "Could not read this image."
                    result.text = error.localizedMessage ?: "Try another photo."
                    recognizer.close()
                }
        } catch (error: Exception) {
            status.text = "Could not open this image."
            result.text = error.localizedMessage ?: "Try another photo."
        }
    }
}
