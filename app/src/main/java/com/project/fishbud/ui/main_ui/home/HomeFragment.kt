package com.project.fishbud.ui.main_ui.home

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.fishbud.R
import com.project.fishbud.databinding.FragmentHomeBinding
import com.project.fishbud.ui.main_ui.community.CommunityViewModel
import com.project.fishbud.ui.main_ui.marketplace.IkanViewModel

class HomeFragment(onClickHome: onClickHome) : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModelIkan: IkanViewModel
    private lateinit var ikanAdapter: IkanHomeAdapter
    private lateinit var viewModelCommunity: CommunityViewModel
    private lateinit var communityAdapter: CommunityHomeAdapter
    private val mCallback = onClickHome
    private var cekGetData = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //ikan
        viewModelIkan = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.NewInstanceFactory()
        )[IkanViewModel::class.java]
        ikanAdapter = IkanHomeAdapter(requireContext())
        with(binding.rvTokoHome) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = ikanAdapter
        }
        observeDataIkan()

        //community
        viewModelCommunity = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.NewInstanceFactory()
        )[CommunityViewModel::class.java]
        communityAdapter = CommunityHomeAdapter(requireContext())
        with(binding.rvKomunitasHome) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = communityAdapter
        }
        observeDataCommunity()

        binding.arrowToko.setOnClickListener(this)
        binding.arrowCommunity.setOnClickListener(this)

    }

    private fun observeDataIkan() {
        viewModelIkan.getDataIkanFromFirebase().observe(viewLifecycleOwner) {
            with(ikanAdapter) {
                setdataIkan(it)
                notifyDataSetChanged()
            }
        }
    }

    private fun observeDataCommunity() {
        viewModelCommunity.getDataThread().observe(viewLifecycleOwner) {
            with(communityAdapter) {
                if (it.isNotEmpty()) {
                    setdataThread(it)
                    with(binding){
                        tvToko.visibility = View.VISIBLE
                        arrowToko.visibility = View.VISIBLE
                        rvTokoHome.visibility = View.VISIBLE
                        tvKomunitas.visibility = View.VISIBLE
                        arrowCommunity.visibility = View.VISIBLE
                        rvKomunitasHome.visibility = View.VISIBLE
                        imageView3.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE

                    }
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.arrow_toko -> {
                mCallback.onClickHome(true)
            }

            R.id.arrow_community -> {
                mCallback.onClickHome(false)
            }
        }
    }

    interface onClickHome {
        fun onClickHome(value: Boolean)
    }

}