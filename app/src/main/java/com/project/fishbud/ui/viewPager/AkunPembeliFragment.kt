package com.project.fishbud.ui.viewPager

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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.project.fishbud.Constants
import com.project.fishbud.R
import com.project.fishbud.databinding.FragmentAkunPembeliBinding
import com.project.fishbud.ui.authentication.AuthenticationActivity
import com.project.fishbud.ui.main_ui.profile.buyer.DataProfileEntity
import com.project.fishbud.ui.main_ui.profile.buyer.DetailProfileFragment
import com.project.fishbud.ui.main_ui.profile.buyer.transaction.TransactionFragment
import com.project.fishbud.ui.main_ui.profile.buyer.waiting_payment.WaitingPaymentFragment
import java.util.concurrent.Executors

class AkunPembeliFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentAkunPembeliBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private var dataProfile : DataProfileEntity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAkunPembeliBinding.inflate(layoutInflater, container, false)
        getFromDatabase()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser as FirebaseUser
        binding.waitingPayment.setOnClickListener(this)
        binding.transaction.setOnClickListener(this)
        binding.logout.setOnClickListener(this)
        binding.profile.setOnClickListener(this)
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
                val waitingPaymentFragment = WaitingPaymentFragment()
                makeCurrentFragment(waitingPaymentFragment)
            }

            R.id.transaction -> {
                val transactionFragment = TransactionFragment()
                makeCurrentFragment(transactionFragment)
            }

            R.id.logout -> {
                Log.i("ceklogout", "onClick: ")
                auth.signOut()
                context?.startActivity(Intent(context, AuthenticationActivity::class.java))
                activity?.finish()
            }

            R.id.profile -> {
                val detailProfileFragment = DetailProfileFragment()
                val bundle = Bundle()
                bundle.putParcelable(Constants.DATA_TO_PROFILE, dataProfile)
                detailProfileFragment.arguments = bundle
                makeCurrentFragment(detailProfileFragment)
            }
        }
    }

    private fun getFromDatabase(){
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            // Simulate process in background thread
            try {
                val reference = FirebaseDatabase.getInstance().reference.child("Users")
                    .child(user.uid)
                reference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val value = snapshot.getValue(DataProfileEntity::class.java)
                        if (value != null) {
                            with(value) {
                                dataProfile = DataProfileEntity(
                                    name, city, birthday, urlProfileImage, address
                                )
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context, "Error get data from database", Toast.LENGTH_SHORT)
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

}