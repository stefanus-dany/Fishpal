package com.project.fishbud.ui.main_ui.marketplace.checkout

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.project.fishbud.Constants
import com.project.fishbud.R
import com.project.fishbud.databinding.FragmentPaymentBinding
import com.project.fishbud.ui.main_ui.MainActivity
import com.project.fishbud.ui.main_ui.marketplace.IkanEntity
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PaymentFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentPaymentBinding
    private var cartPrice: Long = 0
    private var deliveryPrice: Long = 0
    private var totalPrice: Long = 0
    private var isChecked = false
    private lateinit var viewModel: PaymentViewModel
    private var timeDate = ""
    private var date = ""
    private var jenisPengiriman = ""
    private var count = 0
    private var dataIkan : ArrayList<IkanEntity>? = null

    //implementasi alfabet acak untuk id
    private var alphabet: List<Char> = emptyList()
    private lateinit var idPembayaran: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[PaymentViewModel::class.java]
//        viewModel = ViewModelProvider(this)[PaymentViewModel::class.java]
        viewModel.mContext = requireContext()

        //get current date and time
        val cal = Calendar.getInstance()
        val dt = cal.time
        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.US)
        timeDate = sdf.format(dt)

        //get date only
        val sdf0 = SimpleDateFormat("dd MMM yyyy", Locale.US)
        date = sdf0.format(dt)

        val bundle: Bundle? = arguments
        if (bundle != null) {
            cartPrice = bundle.getLong(Constants.CART_PRICE_TO_PAYMENT, 0L)
            dataIkan = bundle.getParcelableArrayList(Constants.DATA_TO_PAYMENT)
            Log.i("cio", "cek dataIkan  di payment: $dataIkan")
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
                jenisPengiriman = "ABC Regular"
            }

            if (checkedId == R.id.rb_2) {
                deliveryPrice = 35000
                totalPrice = deliveryPrice + cartPrice
                binding.deliveryPrice.text = formatRupiah(deliveryPrice)
                binding.hargaTotal.text = formatRupiah((totalPrice))
                isChecked = true
                jenisPengiriman = "One Day Service"
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

                var buyerName: String
                var buyerId: String
                //generate idPembayaran
                alphabet = ('a'..'z') + ('A'..'Z') + ('0'..'9')
                idPembayaran = List(20) { alphabet.random() }.joinToString("")

                viewModel.getDataUser().observe(viewLifecycleOwner, {
                    count++
                    Log.i("cek_count", "cek count: $count")
                    buyerName = it[0].name
                    buyerId = it[0].id
                    //store data to database
                    viewModel.storeToDatabase(
                        idPembayaran,
                        buyerName,
                        buyerId,
                        binding.etAddress.text.toString().trim(),
                        binding.etPayment.text.toString().trim(),
                        jenisPengiriman,
                        totalPrice,
                        timeDate,
                        date,
                        dataIkan
                    )


                    startActivity(Intent(activity, MainActivity::class.java))
                    activity?.finish()
                    binding.progressBar.visibility = View.GONE

                })

            }
        }
    }
}