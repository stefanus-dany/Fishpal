package com.project.fishbud.ui.main_ui.profile.buyer.waiting_payment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.fishbud.databinding.FragmentWaitingPaymentBinding

class WaitingPaymentFragment : Fragment(), WaitingPaymentAdapter.dataWaitingPayment {

    private lateinit var binding: FragmentWaitingPaymentBinding
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

    //observe once / observe sekali / observe satu kali
    fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }

    //cara panggil observe one
//    val observe = viewModel.getDataPayment()
//    observe.observeOnce(this, {
//        if (it != null) {
//            with(adapter) {
//                i++
//                setdataPembayaran(it)
//                Log.i("cekObserve", "onChanged: $i")
//                adapter.notifyDataSetChanged()
//            }
//        }
//    })

    private fun observeData() {
        viewModel.getDataPayment().observe(viewLifecycleOwner, {
            binding.progressBar.visibility = View.GONE
            if (it.isNotEmpty()) {
                with(adapter) {
                    setdataPembayaran(it)
                    adapter.notifyDataSetChanged()
                }
            } else {
                binding.halamanKosong.visibility = View.VISIBLE
            }
        })
    }

    override fun getData(data: WaitingPaymentEntity, position: Int, getItemCount: Int) {
        with(data) {
            val idPembayaran: String = idPembayaran
            val buyerName: String = buyerName
            val buyerId: String = buyerId
            val alamat: String = alamat
            val kartuKredit: String = kartuKredit
            val jenisPengiriman: String = jenisPengiriman
            val totalHarga: Long = totalHarga
            val netPrice: Long = netPrice
            Log.i("totharga", "totalharga: $totalHarga")
            val timePurchase: String = date
            val payBefore: String = timeDate
            val dataIkan: String = dataIkan

            viewModel.getItemOrdered(idPembayaran).observe(viewLifecycleOwner) {

                viewModel.storeToDatabase(
                    idPembayaran,
                    buyerName,
                    buyerId,
                    alamat,
                    kartuKredit,
                    jenisPengiriman,
                    totalHarga,
                    netPrice,
                    timePurchase,
                    payBefore,
                    dataIkan,
                    it
                )

                if (getItemCount==1){
                    binding.rvWaitingPayment.visibility = View.GONE
                    binding.halamanKosong.visibility = View.GONE
                }
            }
        }
    }
}