package com.project.fishbud.ui.main_ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.project.fishbud.R
import com.project.fishbud.databinding.ActivityMainBinding
import com.project.fishbud.ui.authentication.AuthenticationActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivityMainBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(2).isEnabled = false
        auth = FirebaseAuth.getInstance()

        val home = HomeFragment()
        val marketplace = MarketplaceFragment()
        val community = CommunityFragment()
        val profile = ProfileFragment()

        makeCurrentFragment(home)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.bnv_home -> {
                    makeCurrentFragment(home)
                }

                R.id.bnv_marketplace -> {
                    makeCurrentFragment(marketplace)
                }

                R.id.bnv_community -> {
                    makeCurrentFragment(community)
                }

                R.id.bnv_profile -> {
                    makeCurrentFragment(profile)
                }
            }
            true
        }

        binding.btnLogout.setOnClickListener(this)
        binding.detection.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.btnLogout -> {
                auth.signOut()
                startActivity(Intent(this, AuthenticationActivity::class.java))
                finish()
            }

            R.id.detection -> {

            }
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_main_ui, fragment)
            commit()
        }
    }
}