package com.project.fishbud.ui.recognition

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.fishbud.Constants
import com.project.fishbud.databinding.ActivityInfoScanBinding

class InfoScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoScanBinding
    private var resultPredictionText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        resultPredictionText = intent.getStringExtra(Constants.RESULT_PREDICTION_TEXT)

        binding.titleInfoscan.text = resultPredictionText
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}