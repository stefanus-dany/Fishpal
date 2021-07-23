package com.project.fishbud.ui.main_ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.project.fishbud.databinding.FragmentProfileBinding
import com.project.fishbud.ui.main_ui.MainActivity
import com.project.fishbud.ui.viewPager.SectionsPagerAdapter


class ProfileFragment : Fragment(), MainActivity.IOnBackPressed{

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = childFragmentManager.let { SectionsPagerAdapter(it, lifecycle) }

        binding.viewPager2.adapter = adapter
        binding.viewPager2.isSaveEnabled = false

        TabLayoutMediator(binding.tabs, binding.viewPager2) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Akun Pembeli"
                }
                1 -> {
                    tab.text = "Akun Toko"
                }
            }
        }.attach()
    }

    override fun onBackPressed(): Boolean {
        return false
    }

}