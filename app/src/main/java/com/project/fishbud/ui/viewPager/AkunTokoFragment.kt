package com.project.fishbud.ui.viewPager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.project.fishbud.Constants
import com.project.fishbud.R
import com.project.fishbud.databinding.FragmentAkunTokoBinding
import com.project.fishbud.ui.authentication.AuthenticationActivity
import com.project.fishbud.ui.main_ui.profile.fisherman.my_product.MyProductFragment
import com.project.fishbud.ui.main_ui.profile.fisherman.new_order.NewOrderFragment
import com.project.fishbud.ui.main_ui.profile.fisherman.open_store.OpenStoreFragment
import java.util.concurrent.Executors

class AkunTokoFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentAkunTokoBinding
    private val auth = FirebaseAuth.getInstance()
    private val user = auth.currentUser as FirebaseUser
    private var fisherman: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAkunTokoBinding.inflate(layoutInflater, container, false)
        getDataFishermanOrNot()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.waitingPayment.setOnClickListener(this)
        binding.newOrder.setOnClickListener(this)
        binding.logoutToko.setOnClickListener(this)
        binding.btnBukaTokoSekarang.setOnClickListener(this)
        val sharedPreferences = activity?.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        if (sharedPreferences != null) {
            binding.fishermanName.text = sharedPreferences.getString(Constants.USER_NAME, "")
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) {
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.fl_main_ui, fragment)
            addToBackStack(null)
            commit()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.waiting_payment -> {
                val myProdukFragment = MyProductFragment()
                makeCurrentFragment(myProdukFragment)
            }

            R.id.new_order -> {
                val newOrderFragment = NewOrderFragment()
                makeCurrentFragment(newOrderFragment)
            }

            R.id.logout_toko -> {
                auth.signOut()
                context?.startActivity(Intent(context, AuthenticationActivity::class.java))
                activity?.finish()
            }

            R.id.btn_bukaTokoSekarang -> {
                val openStoreFragment = OpenStoreFragment()
                makeCurrentFragment(openStoreFragment)
            }
        }
    }

    private fun getDataFishermanOrNot() {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            // Simulate process in background thread
            try {
                FirebaseDatabase.getInstance().reference.child("Users")
                    .child(user.uid).child("fisherman").get().addOnSuccessListener { er ->
                        if (er.exists()) {
                            fisherman = er.value.toString()
                            fisherman?.let { checkFisherman(it) }
                        }
                    }

            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            handler.post {

            }
        }
    }

    private fun checkFisherman(fisherman: String) {
        Log.i("cekfisherman", "checkFisherman: $fisherman")
        when (fisherman) {
            "true" -> {
                binding.ll1.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }
            "false" -> {
                with(binding) {
                    tvKosong.visibility = View.VISIBLE
                    ivKosong.visibility = View.VISIBLE
                    btnBukaTokoSekarang.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }
            }
            else -> {
                Toast.makeText(
                    context,
                    "Error detect fisherman status from database",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}