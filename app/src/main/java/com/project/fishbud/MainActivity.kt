package com.project.fishbud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.project.fishbud.databinding.ActivityMainBinding
import com.project.fishbud.ui.authentication.AuthenticationActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivityMainBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.btnLogout.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.btnLogout -> {
                auth.signOut()
                startActivity(Intent(this, AuthenticationActivity::class.java))
                finish()
            }
        }
    }

}