package com.karyaprestasi.fishpal.ui.recognition

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.karyaprestasi.fishpal.Constants
import com.karyaprestasi.fishpal.databinding.ActivityInfoScanBinding
import com.karyaprestasi.fishpal.ui.main_ui.MainActivity

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
//            Log.i("check", "cekSplit: ${split?.get(1)}")
            val move = Intent(this, MainActivity::class.java)
            move.putExtra(Constants.DATA_SEARCH_FROM_INFOSCAN_TO_MAIN, resultPredictionText)
            move.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(move)
            finish()
        }
    }

    private fun observeData() {
        resultPredictionText?.let {
            Log.i("testObserveData", "before")
            viewModel.getDataInfoScan(it).observe(this) {
                Log.i("testObserveData", "mid")
                with(binding) {
                    Log.i("testObserveData", "data: $it")
                    progressBar.visibility = View.GONE
                    Glide.with(this@InfoScanActivity)
                        .load(uriImage)
                        .into(ivInfoscan)
                    sizeMax.text = it[0].Size
                    age.text = it[0].MaxAge
                    tvKandunganNutrisi.text = it[0].Nutrition
                    titleInfoscan.text = resultPredictionText
                }
            }
            Log.i("testObserveData", "after")
        }
    }
}