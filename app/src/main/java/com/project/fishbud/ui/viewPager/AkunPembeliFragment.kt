package com.project.fishbud.ui.viewPager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.project.fishbud.Constants
import com.project.fishbud.R
import com.project.fishbud.databinding.FragmentAkunPembeliBinding
import com.project.fishbud.ui.authentication.AuthenticationActivity
import com.project.fishbud.ui.main_ui.profile.buyer.DataProfileEntity
import com.project.fishbud.ui.main_ui.profile.buyer.DetailProfileFragment
import com.project.fishbud.ui.main_ui.profile.buyer.transaction.TransactionFragment
import com.project.fishbud.ui.main_ui.profile.buyer.waiting_payment.WaitingPaymentFragment

class AkunPembeliFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentAkunPembeliBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAkunPembeliBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser as FirebaseUser
        binding.waitingPayment.setOnClickListener(this)
        binding.transaction.setOnClickListener(this)
        binding.logout.setOnClickListener(this)
        binding.profile.setOnClickListener(this)

        val sharedPreferences = activity?.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        if (sharedPreferences != null) {
            binding.userName.text = sharedPreferences.getString(Constants.USER_NAME, "")
            val url = sharedPreferences.getString(Constants.URL_PROFILE_IMAGE_USER, "")
            if (url != "") {
                context?.let {
                    Glide.with(it)
                        .load(url)
                        .into(binding.profileImageUser)
                }
            }
        }

        val bundle: Bundle? = arguments
        if (bundle != null) {
            val data = bundle.getParcelable<DataProfileEntity>(Constants.DATA_TO_PROFILE)
            if (data != null) {
                with(data) {
                    binding.userName.text = name
                    if (urlProfileImage != "") {
                        context?.let {
                            Glide.with(it)
                                .load(urlProfileImage)
                                .into(binding.profileImageUser)
                        }
                    }
                }
            }

        }
    }

    private fun makeCurrentFragment(fragment: Fragment) {
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.fl_main_ui, fragment)
            addToBackStack(null)
            commit()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.waiting_payment -> {
                val waitingPaymentFragment = WaitingPaymentFragment()
                makeCurrentFragment(waitingPaymentFragment)
            }

            R.id.transaction -> {
                val transactionFragment = TransactionFragment()
                makeCurrentFragment(transactionFragment)
            }

            R.id.logout -> {
                Log.i("ceklogout", "onClick: ")
                auth.signOut()
                context?.startActivity(Intent(context, AuthenticationActivity::class.java))
                activity?.finish()
            }

            R.id.profile -> {
                val detailProfileFragment = DetailProfileFragment()
//                val bundle = Bundle()
//                bundle.putParcelable(Constants.DATA_TO_PROFILE, dataProfile)
//                detailProfileFragment.arguments = bundle
                makeCurrentFragment(detailProfileFragment)
            }
        }
    }


}