package com.project.fishbud.ui.main_ui.profile

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.fishbud.databinding.FragmentProfileBinding
import com.project.fishbud.ui.viewPager.AkunPembeliFragment
import com.project.fishbud.ui.viewPager.SectionsPagerAdapter


class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    private val TAG = "check"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i(TAG, "onCreateView: ")
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated: ")
        val sectionsPagerAdapter = fragmentManager?.let {
            SectionsPagerAdapter(
                requireContext(),
                it
            )
        }
//        val sectionsPagerAdapter = SectionsPagerAdapter(requireContext(), childFragmentManager)
//        sectionsPagerAdapter.add(AkunPembeliFragment())
        binding.viewPager.adapter = sectionsPagerAdapter
        binding.tabs.setupWithViewPager(binding.viewPager)
    }
}