package com.project.fishbud.ui.main_ui.marketplace

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.project.fishbud.databinding.FragmentMarketplaceNewBinding

class MarketplaceFragment : Fragment() {

    private lateinit var binding : FragmentMarketplaceNewBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var user : FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMarketplaceNewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val value = UserModel(
//            binding.etEmail.text.toString().trim(),
//            binding.etName.text.toString().trim(),
//            user.uid
//        )
//
//    }
//}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser as FirebaseUser

//        val viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[IkanViewModel::class.java]
////        val courses = viewModel.getCourses()
//        val kategoriAdapter = IkanAdapter()
//        kategoriAdapter.setCourses(courses)
//
//        with(binding.rvKategori) {
//            layoutManager = LinearLayoutManager(context)
//            setHasFixedSize(true)
//            adapter = kategoriAdapter
//        }

//        data = mutableListOf()
//        adapter = NotificationAdapter(data)
//        adapter.mContext = requireContext()
//
//        binding.recyclerNotification.layoutManager = LinearLayoutManager(context)
//        binding.recyclerNotification.adapter = adapter
    }
}