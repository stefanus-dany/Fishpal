package com.project.fishbud.ui.main_ui.profile.fisherman.my_product

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.fishbud.Constants
import com.project.fishbud.OnItemClick
import com.project.fishbud.R
import com.project.fishbud.databinding.FragmentMarketplaceNewBinding
import com.project.fishbud.databinding.FragmentMyProductBinding
import com.project.fishbud.ui.main_ui.marketplace.IkanEntity
import com.project.fishbud.ui.main_ui.marketplace.cart.CartFragment
import com.project.fishbud.ui.main_ui.profile.fisherman.tambahProduk.TambahProdukFragment


class MyProductFragment : Fragment(), View.OnClickListener{

    private lateinit var binding: FragmentMyProductBinding
    private lateinit var viewModel: MyProductViewModel
    private lateinit var adapter: MyProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyProductBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.NewInstanceFactory()
        )[MyProductViewModel::class.java]
        adapter = MyProductAdapter(requireContext())
        with(binding.rvMyProduct) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@MyProductFragment.adapter
        }
        observeData()
        binding.btnAddProduct.setOnClickListener(this)
    }

    private fun makeCurrentFragment(fragment: Fragment) {
        fragmentManager?.beginTransaction()?.apply {
            replace(R.id.fl_main_ui, fragment)
            addToBackStack(null)
            commit()
        }
    }

    private fun observeData() {
        viewModel.getMyProduct().observe(viewLifecycleOwner) {
            with(adapter) {
                Log.i("cekIkan", "it : $it")
                setdataIkan(it)
                notifyDataSetChanged()
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_add_product -> {
                val tambahProdukFragment = TambahProdukFragment()
                makeCurrentFragment(tambahProdukFragment)
            }
        }
    }

}