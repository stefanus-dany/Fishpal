package com.project.fishbud.ui.main_ui.marketplace.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.project.fishbud.Constants
import com.project.fishbud.R
import com.project.fishbud.databinding.FragmentPaymentBinding
import java.text.NumberFormat
import java.util.*

class PaymentFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentPaymentBinding
    private var cartPrice: Long = 0
    private var deliveryPrice: Long = 0
    private var totalPrice: Long = 0
    private var isChecked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle: Bundle? = arguments
        if (bundle != null) {
            cartPrice = bundle.getLong(Constants.CART_PRICE_TO_PAYMENT, 0L)
            binding.cartPrice.text = formatRupiah(cartPrice)
        }

        binding.btnProses.setOnClickListener(this)

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rb_1) {
                deliveryPrice = 22000
                totalPrice = deliveryPrice + cartPrice
                binding.deliveryPrice.text = formatRupiah(deliveryPrice)
                binding.hargaTotal.text = formatRupiah((totalPrice))
                isChecked = true
            }

            if (checkedId == R.id.rb_2) {
                deliveryPrice = 35000
                totalPrice = deliveryPrice + cartPrice
                binding.deliveryPrice.text = formatRupiah(deliveryPrice)
                binding.hargaTotal.text = formatRupiah((totalPrice))
                isChecked = true
            }
        }

        binding.hargaTotal.text = formatRupiah((deliveryPrice + cartPrice))
        binding.btnBack.setOnClickListener {
            fragmentManager?.popBackStack()
        }
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
        when (v?.id) {
            R.id.btn_proses -> {
                binding.progressBar.visibility = View.VISIBLE

                if (binding.etAddress.text.toString().trim().isEmpty()) {
                    binding.etAddress.error = "Masukkan alamat!"
                    binding.etAddress.requestFocus()
                    binding.progressBar.visibility = View.INVISIBLE
                    return onClick(view)
                }

                if (binding.etPayment.text.toString().trim().isEmpty()) {
                    binding.etPayment.error = "Masukkan no kartu kredit!"
                    binding.etPayment.requestFocus()
                    binding.progressBar.visibility = View.INVISIBLE
                    return onClick(view)
                }

                if (!isChecked) {
                    Toast.makeText(context, "Pilih jenis pembayaran!", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.INVISIBLE
                    return onClick(view)
                }

                Toast.makeText(context, "Dah komplit", Toast.LENGTH_SHORT).show()

                binding.progressBar.visibility = View.GONE
            }
        }
    }
}