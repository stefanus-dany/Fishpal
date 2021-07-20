package com.project.fishbud.ui.main_ui.profile.fisherman.newOrder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.fishbud.R
import com.project.fishbud.databinding.FragmentNewOrderBinding
import com.project.fishbud.ui.main_ui.profile.buyer.waitingPayment.WaitingPaymentAdapter
import com.project.fishbud.ui.main_ui.profile.buyer.waitingPayment.WaitingPaymentEntity
import com.project.fishbud.ui.main_ui.profile.buyer.waitingPayment.WaitingPaymentViewModel

//class NewOrderFragment : Fragment() {
//
//    private lateinit var binding : FragmentNewOrderBinding
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentNewOrderBinding.inflate(layoutInflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        viewModel = ViewModelProvider(
//            this,
//            ViewModelProvider.NewInstanceFactory()
//        )[WaitingPaymentViewModel::class.java]
//        viewModel.mContext = requireContext()
//        adapter = WaitingPaymentAdapter(requireContext(), this)
//        with(binding.rvWaitingPayment) {
//            layoutManager = LinearLayoutManager(context)
//            adapter = this@WaitingPaymentFragment.adapter
//        }
//
//        observeData()
//    }
//
//    private fun observeData() {
//        viewModel.getDataPayment().observe(viewLifecycleOwner) {
//            with(adapter) {
//                if (it != null) {
//                    setdataPembayaran(it)
//                    notifyDataSetChanged()
//                } else {
//                    with(binding) {
//                        rvWaitingPayment.visibility = android.view.View.GONE
//                        halamanKosong.visibility = android.view.View.VISIBLE
//                    }
//                }
//            }
//        }
//    }
//
//    override fun getData(data: WaitingPaymentEntity, position : Int) {
//        with(data) {
//            val idPembayaran: String = idPembayaran
//            val buyerName: String = buyerName
//            val buyerId: String = buyerId
//            val alamat: String = alamat
//            val kartuKredit: String = kartuKredit
//            val jenisPengiriman: String = jenisPengiriman
//            val totalHarga: Long = totalHarga
//            val timePurchase: String = date
//            val payBefore: String = timeDate
//            val dataIkan : String = dataIkan
//
//            viewModel.getItemOrdered(idPembayaran).observe(viewLifecycleOwner){
//
//                viewModel.storeToDatabase(
//                    idPembayaran,
//                    buyerName,
//                    buyerId,
//                    alamat,
//                    kartuKredit,
//                    jenisPengiriman,
//                    totalHarga,
//                    timePurchase,
//                    payBefore,
//                    dataIkan,
//                    it
//                )
//
//            }
//
//
//
//
//
//            adapter.notifyItemRemoved(position)
//        }
//    }
//}