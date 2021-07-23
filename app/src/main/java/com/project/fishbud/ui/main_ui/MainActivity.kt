package com.project.fishbud.ui.main_ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.project.fishbud.Constants
import com.project.fishbud.R
import com.project.fishbud.databinding.ActivityMainBinding
import com.project.fishbud.ui.main_ui.community.CommunityFragment
import com.project.fishbud.ui.main_ui.home.HomeFragment
import com.project.fishbud.ui.main_ui.marketplace.MarketplaceFragment
import com.project.fishbud.ui.main_ui.profile.ProfileFragment
import com.project.fishbud.ui.main_ui.profile.buyer.DataProfileEntity
import com.project.fishbud.ui.recognition.RecognitionActivity
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity(), View.OnClickListener, HomeFragment.onClickHome {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private val TAG = "check"
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: ")
        sharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        binding = ActivityMainBinding.inflate(layoutInflater)
        getFromDatabase()
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser as FirebaseUser
        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(2).isEnabled = false
        auth = FirebaseAuth.getInstance()

        val home = HomeFragment(this)
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
                startActivity(Intent(this, RecognitionActivity::class.java))
            }
        }
    }

    fun makeCurrentFragment(fragment: Fragment) {
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
                makeCurrentFragment(HomeFragment(this))
                binding.bottomNavigationView.selectedItemId = R.id.bnv_home
            }
        }
    }

    interface IOnBackPressed {
        fun onBackPressed(): Boolean
    }

    private fun getFromDatabase() {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            // Simulate process in background thread
            try {
                auth = FirebaseAuth.getInstance()
                user = auth.currentUser as FirebaseUser
                val reference = FirebaseDatabase.getInstance().reference.child("Users")
                    .child(user.uid)
                reference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val value = snapshot.getValue(DataProfileEntity::class.java)
                        if (value != null) {
                            with(value) {
                                val sp = sharedPreferences.edit()
                                sp.putString(Constants.USER_NAME, name)
                                sp.putString(Constants.ADDRESS_USER, address)
                                sp.putString(Constants.CITY_USER, city)
                                sp.putString(Constants.BIRTHDAY_USER, birthday)
                                sp.putString(Constants.URL_PROFILE_IMAGE_USER, urlProfileImage)
                                sp.apply()
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(
                            this@MainActivity,
                            "Error get data from database",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                })
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            handler.post {

            }
        }
    }

    override fun onClickHome(value: Boolean) {
        if (value){
            binding.bottomNavigationView.selectedItemId = R.id.bnv_marketplace
            makeCurrentFragment(MarketplaceFragment())
        } else {
            binding.bottomNavigationView.selectedItemId = R.id.bnv_community
            makeCurrentFragment(CommunityFragment())
        }
    }

}