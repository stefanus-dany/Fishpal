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

        observeData()
    }

    private fun observeData() {
        viewModel.getIdOrderedFromFisherman().observe(viewLifecycleOwner){
            adapter.notifyDataSetChanged()
            viewModel.getItemOrderedFromFisherman(it).observe(viewLifecycleOwner){
                with(adapter) {
                    if (it != null) {
                        setdataPembayaran(it)
                        notifyDataSetChanged()
                    } else {
                        with(binding) {
                            rvNewOrder.visibility = android.view.View.GONE
                            halamanKosong.visibility = android.view.View.VISIBLE
                        }
                    }
                }
            }
        }

    }

    override fun getDataNewOrder(data: OrderFishermanEntity, position: Int) {
        with(data) {
            val nelayanId: String = nelayanId
            val idProduk: String = data.idPesanan //id produk
            val namaIkan: String = namaIkan
            val harga: Long = harga
            val linkImage: String = linkImage
            val tokoIkan: String = tokoIkan
            val idPembayaran: String = idPembayaran

            viewModel.storeToDatabase(
                nelayanId,
                idProduk,
                namaIkan,
                harga,
                linkImage,
                tokoIkan,
                idPembayaran
            )
            adapter.notifyItemRemoved(position)
        }
    }
}