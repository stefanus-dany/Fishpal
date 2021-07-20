package com.project.fishbud.ui.viewPager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.fishbud.R
import com.project.fishbud.databinding.FragmentAkunPembeliBinding
import com.project.fishbud.ui.main_ui.profile.buyer.waitingPayment.WaitingPaymentFragment
import com.project.fishbud.ui.main_ui.profile.fisherman.tambahProduk.TambahProdukFragment

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
        binding.btn.setOnClickListener(this)
        binding.btnWaitingPayment.setOnClickListener(this)
    }

    private fun makeCurrentFragment(fragment: Fragment) {
        fragmentManager?.beginTransaction()?.apply {
            replace(R.id.fl_main_ui, fragment)
            addToBackStack(null)
            commit()
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn -> {
                val tambahProdukFragment = TambahProdukFragment()
                makeCurrentFragment(tambahProdukFragment)
            }

            R.id.btn_waiting_payment -> {
                val waitingPaymentFragment = WaitingPaymentFragment()
                makeCurrentFragment(waitingPaymentFragment)
            }
        }
    }


}