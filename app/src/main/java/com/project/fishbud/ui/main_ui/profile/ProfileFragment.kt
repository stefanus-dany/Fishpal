package com.project.fishbud.ui.main_ui.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.project.fishbud.Constants
import com.project.fishbud.R
import com.project.fishbud.databinding.FragmentProfileBinding
import com.project.fishbud.ui.main_ui.MainActivity
import com.project.fishbud.ui.viewPager.AkunTokoFragment
import com.project.fishbud.ui.viewPager.SectionsPagerAdapter


class ProfileFragment : Fragment(){

    private lateinit var binding: FragmentProfileBinding
    private lateinit var adapter : SectionsPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = SectionsPagerAdapter(childFragmentManager, lifecycle)

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

//    override fun onBackPressed(): Boolean {
//        return false
//    }

    override fun onResume() {
        val sp = activity?.getSharedPreferences("sharedPref2", Context.MODE_PRIVATE)
        val tmp = sp?.getInt(Constants.SAVE_CURRENT_TABLAYOUT, 0)
        Log.i("cekLife", "onResume: $tmp")
        sp?.edit()?.clear()?.apply()
        Log.i("cekLife", "onResumeSetelah: $tmp")
        if (tmp==1){
//            binding.viewPager2.post {
//                binding.viewPager2.currentItem = 1
//            }
        }
        super.onResume()
    }

//    private fun makeCurrentFragment(fragment : Fragment){
//        fragmentManager?.beginTransaction()?.apply {
//            replace(R.id.fl_profile, fragment)
//            commit()
//        }
//
//    }
}