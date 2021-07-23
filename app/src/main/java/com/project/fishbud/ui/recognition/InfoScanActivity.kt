package com.project.fishbud.ui.recognition

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.project.fishbud.Constants
import com.project.fishbud.databinding.ActivityInfoScanBinding
import com.project.fishbud.ui.main_ui.MainActivity

class InfoScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoScanBinding
    private var resultPredictionText: String? = null
    private lateinit var viewModel: InfoScanViewModel
    private var uriImage: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoScanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[InfoScanViewModel::class.java]
        viewModel.mContext = this
        resultPredictionText = intent.getStringExtra(Constants.RESULT_PREDICTION_TEXT)
        uriImage = intent.getParcelableExtra(Constants.URI_RESULT_SCAN)
        observeData()

        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnCariDiToko.setOnClickListener {
            val split = resultPredictionText?.split("Ikan ")?.toTypedArray()
            Log.i("check", "cekSplit: ${split?.get(1)}")
            val move = Intent(this, MainActivity::class.java)
            move.putExtra(Constants.DATA_SEARCH_FROM_INFOSCAN_TO_MAIN, split?.get(1))
            startActivity(move)
        }
    }

    private fun observeData() {
        resultPredictionText?.let {
            viewModel.getDataInfoScan(it).observe(this) {
                with(binding) {
                    Glide.with(this@InfoScanActivity)
                        .load(uriImage)
                        .into(ivInfoscan)
                    sizeMax.text = it[0].Size
                    age.text = it[0].MaxAge
                    tvKandunganNutrisi.text = it[0].Nutrition
                    titleInfoscan.text = resultPredictionText
                }
            }
        }
    }
}