package com.project.fishbud.ui.main_ui.community

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.fishbud.databinding.FragmentCommunityBinding

class CommunityFragment : Fragment() {

    private lateinit var binding: FragmentCommunityBinding
    private lateinit var viewModel: CommunityViewModel
    private lateinit var adapter: CommunityAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommunityBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.NewInstanceFactory()
        )[CommunityViewModel::class.java]
        adapter = CommunityAdapter(requireContext())
        with(binding.rvCommunity) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@CommunityFragment.adapter
        }
        observeData()
    }

    private fun observeData() {
        viewModel.getDataThread().observe(viewLifecycleOwner) {
            with(adapter) {
                Log.i("cekIkan", "it : $it")
                setdataThread(it)
                notifyDataSetChanged()
            }
        }
    }

}