package com.project.fishbud.ui.main_ui.profile.buyer.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.fishbud.databinding.FragmentTransactionBinding

class TransactionFragment : Fragment() {

    private lateinit var binding: FragmentTransactionBinding
    private lateinit var viewModel: TransactionViewModel
    private lateinit var adapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[TransactionViewModel::class.java]
        viewModel.mContext = requireContext()
        adapter = TransactionAdapter(requireContext())
        with(binding.rvTransaction) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@TransactionFragment.adapter
        }

        binding.btnBack.setOnClickListener {
            fragmentManager?.popBackStack()
        }

        observeData()
    }

    private fun observeData() {
        viewModel.getIdShipping().observe(viewLifecycleOwner) {
            viewModel.getItemShipping(it).observe(viewLifecycleOwner) {
                with(adapter) {
                    if (it != null) {
                        setdataPembayaran(it)
                        notifyDataSetChanged()
                    } else {
                        with(binding) {
                            rvTransaction.visibility = View.GONE
                            halamanKosong.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

}