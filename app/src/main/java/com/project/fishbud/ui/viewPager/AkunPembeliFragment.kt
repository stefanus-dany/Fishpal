package com.project.fishbud.ui.viewPager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.FirebaseAuth
import com.project.fishbud.R
import com.project.fishbud.databinding.FragmentAkunPembeliBinding
import com.project.fishbud.ui.authentication.AuthenticationActivity
import com.project.fishbud.ui.main_ui.profile.buyer.transaction.TransactionFragment
import com.project.fishbud.ui.main_ui.profile.buyer.waiting_payment.WaitingPaymentFragment

class AkunPembeliFragment : Fragment(), View.OnClickListener {

    private lateinit var binding : FragmentAkunPembeliBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAkunPembeliBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        binding.waitingPayment.setOnClickListener(this)
        binding.transaction.setOnClickListener(this)
        binding.logout.setOnClickListener(this)
    }

    private fun makeCurrentFragment(fragment: Fragment) {
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.fl_main_ui, fragment)
            addToBackStack(null)
            commit()
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){

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
        }
    }


}