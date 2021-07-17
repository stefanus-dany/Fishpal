package com.project.fishbud.ui.main_ui.marketplace.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.fishbud.Constants
import com.project.fishbud.databinding.FragmentCartBinding
import com.project.fishbud.ui.main_ui.marketplace.IkanEntity
import java.text.NumberFormat
import java.util.*


class CartFragment : Fragment(), CartAdapter.CartHarga {

    private lateinit var binding: FragmentCartBinding
    private lateinit var cartAdapter: CartAdapter
    private var ikanEntity: IkanEntity? = null
    private var accumulation = mutableMapOf<Int, Long>()

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
        Log.i("ceek", "onViewCreated: $ikanEntity")
        with(cartAdapter) {
            val bundle: Bundle? = arguments
            if (bundle != null) {
                val data = bundle.getParcelableArrayList<IkanEntity>(Constants.DATA_TO_CART_VALUE)
                Log.i("cik", "onClick222: $data")
                if (data != null) {
                    if (data.isNotEmpty()) {
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
        }
    }

    override fun totalHarga(value: Long, position: Int) {
        accumulation[position] = value
        var tmp: Long = 0
        for (i in 0 until (accumulation.size)) {
            Log.i("testt", "i: $i")
            Log.i("testt", "accumulation: ${accumulation[i]}")
            tmp += accumulation[i]!!
        }
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
}