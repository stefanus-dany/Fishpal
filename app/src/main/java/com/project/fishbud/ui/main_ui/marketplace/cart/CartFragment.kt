package com.project.fishbud.ui.main_ui.marketplace.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.fishbud.Constants
import com.project.fishbud.R
import com.project.fishbud.databinding.FragmentCartBinding
import com.project.fishbud.ui.main_ui.marketplace.IkanEntity
import com.project.fishbud.ui.main_ui.marketplace.checkout.PaymentFragment
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class CartFragment : Fragment(), CartAdapter.CartHarga, View.OnClickListener {

    private lateinit var binding: FragmentCartBinding
    private lateinit var cartAdapter: CartAdapter
    private var ikanEntity: ArrayList<IkanEntity>? = null
    private var ikanQuantity = hashMapOf<Int, Long>()
    private var accumulation = mutableMapOf<Int, Long>()
    private var cartPrice : Long = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cartAdapter = CartAdapter(this)
        with(binding.rvCart) {
            layoutManager = LinearLayoutManager(context)
            adapter = cartAdapter
        }
        with(cartAdapter) {
            val bundle: Bundle? = arguments
            if (bundle != null) {
                val data = bundle.getParcelableArrayList<IkanEntity>(Constants.DATA_TO_CART_VALUE)
                ikanEntity = data
                Log.i("cik", "onClick222: $data")
                if (data != null) {
                    if (data.isNotEmpty()) {
                        with(binding) {
                            rvCart.visibility = View.VISIBLE
                            ivHarga.visibility = View.VISIBLE
                            btnBeliSekarang.visibility = View.VISIBLE
                            hargaCart.visibility = View.VISIBLE
                            halamanKosong.visibility = View.GONE
                        }
                        //isi map dengan nilai 0 agar saat di cart ga ada error jika isi quantity dari 1 langsung ke 3/4/5...
                        for (i in 0 until (data.size)){
                            accumulation[i] = 0
                            ikanQuantity[i] = 0
                        }
                        setdataCart(data)
                        notifyDataSetChanged()
                    } else {
                        with(binding) {
                            rvCart.visibility = View.GONE
                            ivHarga.visibility = View.GONE
                            btnBeliSekarang.visibility = View.GONE
                            hargaCart.visibility = View.GONE
                            halamanKosong.visibility = View.VISIBLE
                        }
                    }
                }
            }

            binding.btnBack.setOnClickListener {
                fragmentManager?.popBackStack()
            }
            binding.btnBeliSekarang.setOnClickListener(this@CartFragment)
        }
    }

    override fun totalHarga(value: Long, position: Int) {
        ikanQuantity[position] = value
        accumulation[position] = value
        var tmp = 0L
        for (i in 0 until (accumulation.size)) {
            Log.i("testt", "i: $i")
            Log.i("testt", "accumulation: ${accumulation[i]}")
            tmp += accumulation[i]!!
        }
        cartPrice = tmp
        val post = formatRupiah(tmp)
        binding.hargaCart.text = post
    }

    private fun formatRupiah(number: Long): String {
        val localeID = Locale("IND", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        val formatRupiah = numberFormat.format(number)
        val split = formatRupiah.split(",")
        val length = split[0].length
        return split[0].substring(0, 2) + split[0].substring(2, length)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_beli_sekarang -> {
                if (binding.hargaCart.text != resources.getString(R.string.rp0)){
                    val paymentFragment = PaymentFragment()
                    val bundle = Bundle()
                    bundle.putLong(Constants.CART_PRICE_TO_PAYMENT, cartPrice)
                    val arrayList : ArrayList<IkanEntity> = ArrayList(ikanEntity)
                    bundle.putParcelableArrayList(Constants.DATA_TO_PAYMENT, arrayList)
                    bundle.putSerializable(Constants.DATA_QUANTITY_TO_PAYMENT, ikanQuantity)
                    Log.i("cio", "cek ikanEntity di cart : $ikanEntity")
                    Log.i("cio", "cek dataIkan di cart : $arrayList")
                    paymentFragment.arguments = bundle
                    makeCurrentFragment(paymentFragment)
                } else {
                    Toast.makeText(context, "Masukkan jumlah barang!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) {
        fragmentManager?.beginTransaction()?.apply {
            replace(R.id.fl_main_ui, fragment)
            addToBackStack(null)
            commit()
        }
    }
}