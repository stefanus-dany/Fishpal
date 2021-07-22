package com.project.fishbud.ui.main_ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.project.fishbud.R
import com.project.fishbud.databinding.ActivityMainBinding
import com.project.fishbud.ui.main_ui.community.CommunityFragment
import com.project.fishbud.ui.main_ui.marketplace.MarketplaceFragment
import com.project.fishbud.ui.main_ui.profile.ProfileFragment

class MainActivity : AppCompatActivity(), View.OnClickListener{

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private val TAG = "check"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: ")
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
        binding.detection.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {

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

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause: ")
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart: ")
    }

    override fun onBackPressed() {
        if (binding.bottomNavigationView.selectedItemId == R.id.bnv_home) {
            super.onBackPressed()
        } else {
            val fragment =
                this.supportFragmentManager.findFragmentById(R.id.fl_main_ui)
            (fragment as? IOnBackPressed)?.onBackPressed()?.not()?.let {
                Log.i("cekcek", "onBackPressed: ")
                makeCurrentFragment(HomeFragment())
                binding.bottomNavigationView.selectedItemId = R.id.bnv_home
            }
        }
    }

    interface IOnBackPressed {
        fun onBackPressed(): Boolean
    }

}