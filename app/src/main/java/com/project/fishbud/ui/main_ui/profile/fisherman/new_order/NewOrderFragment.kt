package com.project.fishbud.ui.main_ui.profile.fisherman.new_order

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.fishbud.databinding.FragmentNewOrderBinding
import com.project.fishbud.ui.main_ui.profile.OrderFishermanEntity

class NewOrderFragment : Fragment(), NewOrderAdapter.dataNewOrder {

    private lateinit var binding: FragmentNewOrderBinding
    private lateinit var viewModel: NewOrderViewModel
    private lateinit var adapter: NewOrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewOrderBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[NewOrderViewModel::class.java]
        viewModel.mContext = requireContext()
        adapter = NewOrderAdapter(requireContext(), this)
        with(binding.rvNewOrder) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@NewOrderFragment.adapter
        }

        binding.btnBack.setOnClickListener {
            fragmentManager?.popBackStack()
        }

        observeData()

    }

    private fun observeData() {
        viewModel.getIdOrderedFromFisherman().observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                viewModel.getItemOrderedFromFisherman(it).observe(viewLifecycleOwner) {
                    with(adapter) {
                        binding.progressBar.visibility = View.GONE
                        if (it.isNotEmpty()) {
                            setdataPembayaran(it)
                            notifyDataSetChanged()
                        } else {
                            with(binding) {
                                rvNewOrder.visibility = View.GONE
                                halamanKosong.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            } else {
                binding.progressBar.visibility = View.GONE
                binding.halamanKosong.visibility = View.VISIBLE
            }
        }

    }

    override fun getDataNewOrder(data: OrderFishermanEntity, position: Int, getItemCount: Int) {
        with(data) {
            Log.i("nof", "getItemCount: $getItemCount")
            val nelayanId: String = nelayanId
            val idProduk: String = data.idPesanan //id produk
            val namaIkan: String = namaIkan
            val harga: Long = harga
            val linkImage: String = linkImage
            val tokoIkan: String = tokoIkan
            val idPembayaran: String = idPembayaran
            val idBuyer: String = idBuyer
            val totalHarga: Long = totalHarga
            Log.i("cekBuyer", "idBuyer di neworder: $idBuyer")

            viewModel.storeToDatabase(
                nelayanId,
                idProduk,
                namaIkan,
                harga,
                totalHarga,
                linkImage,
                tokoIkan,
                idPembayaran,
                idBuyer
            )
            if (getItemCount==1){
                binding.rvNewOrder.visibility = View.GONE
                binding.halamanKosong.visibility = View.GONE
            }
        }
    }
}