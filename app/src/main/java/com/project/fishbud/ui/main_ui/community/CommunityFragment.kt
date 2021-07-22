package com.project.fishbud.ui.main_ui.community

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.fishbud.R
import com.project.fishbud.databinding.FragmentCommunityBinding
import com.project.fishbud.ui.main_ui.community.create_thread.CreateThreadFragment

class CommunityFragment : Fragment(), View.OnClickListener {

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
        binding.fabAddThread.setOnClickListener(this)
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

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.fab_addThread -> {
                val createThreadFragment = CreateThreadFragment()
                makeCurrentFragment(createThreadFragment)
            }
        }
    }

    private fun makeCurrentFragment(fragment : Fragment){
        fragmentManager?.beginTransaction()?.apply {
            replace(R.id.fl_main_ui, fragment)
            addToBackStack(null)
            commit()
        }
    }

}