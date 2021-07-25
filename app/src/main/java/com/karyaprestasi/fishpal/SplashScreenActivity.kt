package com.karyaprestasi.fishpal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.karyaprestasi.fishpal.databinding.ActivitySplashScreenBinding
import com.karyaprestasi.fishpal.ui.authentication.AuthenticationActivity

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ll.alpha = 0f
        binding.logo.alpha = 0f
        binding.ll.animate().setDuration(1500).alpha(1f).withEndAction{
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        binding.logo.animate().setDuration(1500).alpha(1f).withEndAction{
            startActivity(Intent(this, AuthenticationActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}