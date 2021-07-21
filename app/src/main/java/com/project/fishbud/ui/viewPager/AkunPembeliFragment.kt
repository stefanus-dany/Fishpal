package com.project.fishbud.ui.viewPager

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.project.fishbud.R
import com.project.fishbud.databinding.FragmentAkunPembeliBinding
import com.project.fishbud.ui.main_ui.profile.buyer.transaction.TransactionFragment
import com.project.fishbud.ui.main_ui.profile.buyer.waiting_payment.WaitingPaymentFragment

class AkunPembeliFragment : Fragment(), View.OnClickListener {

    private lateinit var binding : FragmentAkunPembeliBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAkunPembeliBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnWaitingPayment.setOnClickListener(this)
        binding.btnTransaction.setOnClickListener(this)
        binding.toast.setOnClickListener(this)
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

            R.id.btn_waiting_payment -> {
                val waitingPaymentFragment = WaitingPaymentFragment()
                makeCurrentFragment(waitingPaymentFragment)
            }

            R.id.btn_transaction -> {
                val transactionFragment = TransactionFragment()
                makeCurrentFragment(transactionFragment)
            }

            R.id.toast -> {
                Toast.makeText(context, "TESTING", Toast.LENGTH_SHORT).show()
                Log.i("cek_toast", "masuk ")
            }
        }
    }


}