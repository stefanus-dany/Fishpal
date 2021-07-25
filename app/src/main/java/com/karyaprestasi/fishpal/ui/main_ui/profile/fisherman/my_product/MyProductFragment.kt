package com.karyaprestasi.fishpal.ui.main_ui.profile.fisherman.my_product

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.karyaprestasi.fishpal.R
import com.karyaprestasi.fishpal.databinding.FragmentMyProductBinding
import com.karyaprestasi.fishpal.ui.main_ui.profile.fisherman.tambahProduk.TambahProdukFragment


class MyProductFragment : Fragment(), View.OnClickListener {

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
        binding.btnBack.setOnClickListener {
            fragmentManager?.popBackStack()
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) {
        fragmentManager?.beginTransaction()?.apply {
            add(R.id.fl_main_ui, fragment)
            addToBackStack(null)
            commit()
        }
    }

    private fun observeData() {
        viewModel.getMyProduct().observe(viewLifecycleOwner) {
            with(adapter) {
                binding.progressBar.visibility = View.GONE
                if (it.isNotEmpty()) {
                    binding.halamanKosong.visibility = View.GONE
                    Log.i("cekIkan", "it : $it")
                    setdataIkan(it)
                    notifyDataSetChanged()
                } else {
                    binding.halamanKosong.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_add_product -> {
                val tambahProdukFragment = TambahProdukFragment()
                makeCurrentFragment(tambahProdukFragment)
            }
        }
    }

    override fun onDestroy() {
        val navBar : BottomAppBar? = activity?.findViewById(R.id.bottomAppBar)
        navBar?.visibility = View.VISIBLE
        val scan : FloatingActionButton? = activity?.findViewById(R.id.detection)
        scan?.visibility = View.VISIBLE
        super.onDestroy()
    }

}