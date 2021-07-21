package com.project.fishbud.ui.main_ui.profile.buyer.waiting_payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.fishbud.databinding.FragmentWaitingPaymentBinding

class WaitingPaymentFragment : Fragment(), WaitingPaymentAdapter.dataWaitingPayment {

    private lateinit var binding: FragmentWaitingPaymentBinding
    private var getData: WaitingPaymentEntity? = null
    private lateinit var viewModel: WaitingPaymentViewModel
    private lateinit var adapter: WaitingPaymentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWaitingPaymentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[WaitingPaymentViewModel::class.java]
        viewModel.mContext = requireContext()
        adapter = WaitingPaymentAdapter(requireContext(), this)
        with(binding.rvWaitingPayment) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@WaitingPaymentFragment.adapter
        }

        observeData()
        binding.btnBack.setOnClickListener {
            fragmentManager?.popBackStack()
        }
    }

    private fun observeData() {
        viewModel.getDataPayment().observe(viewLifecycleOwner) {
            with(adapter) {
                if (it != null) {
                    setdataPembayaran(it)
                    notifyDataSetChanged()
                } else {
                    with(binding) {
                        rvWaitingPayment.visibility = View.GONE
                        halamanKosong.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun getData(data: WaitingPaymentEntity, position : Int) {
        with(data) {
            val idPembayaran: String = idPembayaran
            val buyerName: String = buyerName
            val buyerId: String = buyerId
            val alamat: String = alamat
            val kartuKredit: String = kartuKredit
            val jenisPengiriman: String = jenisPengiriman
            val totalHarga: Long = totalHarga
            val timePurchase: String = date
            val payBefore: String = timeDate
            val dataIkan : String = dataIkan

            viewModel.getItemOrdered(idPembayaran).observe(viewLifecycleOwner){

                viewModel.storeToDatabase(
                    idPembayaran,
                    buyerName,
                    buyerId,
                    alamat,
                    kartuKredit,
                    jenisPengiriman,
                    totalHarga,
                    timePurchase,
                    payBefore,
                    dataIkan,
                    it
                )

            }
            adapter.notifyItemRemoved(position)
        }
    }
}