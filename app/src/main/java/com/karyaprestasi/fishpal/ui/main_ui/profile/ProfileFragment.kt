package com.karyaprestasi.fishpal.ui.main_ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.karyaprestasi.fishpal.databinding.BelumLoginBinding
import com.karyaprestasi.fishpal.databinding.FragmentProfileBinding
import com.karyaprestasi.fishpal.ui.authentication.AuthenticationActivity
import com.karyaprestasi.fishpal.ui.viewPager.SectionsPagerAdapter

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var bindingLogin: BelumLoginBinding
    private lateinit var adapter: SectionsPagerAdapter
    private var user: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            bindingLogin = BelumLoginBinding.inflate(layoutInflater, container, false)
            return bindingLogin.root
        } else {
            binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
            return binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = SectionsPagerAdapter(childFragmentManager, lifecycle)

        if (user != null) {
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
        } else {
            bindingLogin.btnLoginProfile.setOnClickListener {
                val move = Intent(context, AuthenticationActivity::class.java)
                move.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                context?.startActivity(Intent(context, AuthenticationActivity::class.java))
            }
        }
    }

//    override fun onBackPressed(): Boolean {
//        return false
//    }
}