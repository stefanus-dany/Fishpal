package com.project.fishbud.ui.viewPager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.fishbud.R
import com.project.fishbud.databinding.FragmentAkunTokoBinding
import com.project.fishbud.ui.main_ui.profile.fisherman.new_order.NewOrderFragment
import com.project.fishbud.ui.main_ui.profile.fisherman.tambahProduk.TambahProdukFragment

class AkunTokoFragment : Fragment(), View.OnClickListener {

    private lateinit var binding : FragmentAkunTokoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAkunTokoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnTambahProduk.setOnClickListener(this)
        binding.btnPesananBaru.setOnClickListener(this)
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
            R.id.btn_tambah_produk -> {
                val tambahProdukFragment = TambahProdukFragment()
                makeCurrentFragment(tambahProdukFragment)
            }

            R.id.btn_pesanan_baru -> {
                val newOrderFragment = NewOrderFragment()
                makeCurrentFragment(newOrderFragment)
            }
        }
    }

}