package com.project.fishbud.ui.main_ui.marketplace

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.fishbud.Constants
import com.project.fishbud.OnItemClick
import com.project.fishbud.R
import com.project.fishbud.databinding.FragmentMarketplaceNewBinding
import com.project.fishbud.ui.main_ui.MainActivity
import com.project.fishbud.ui.main_ui.marketplace.cart.CartFragment


class MarketplaceFragment : Fragment(), OnItemClick, MainActivity.IOnBackPressed {

    private lateinit var binding: FragmentMarketplaceNewBinding
    private lateinit var viewModel: IkanViewModel
    private lateinit var ikanAdapter: IkanAdapter
    private var ikanEntity: MutableList<IkanEntity> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMarketplaceNewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.NewInstanceFactory()
        )[IkanViewModel::class.java]
        ikanAdapter = IkanAdapter(requireContext(), this)
        with(binding.rvMarketplace) {
            layoutManager = LinearLayoutManager(context)
            adapter = ikanAdapter
        }
        observeData()

        binding.cart.setOnClickListener {
            val cartFragment = CartFragment()
            //store data ikan to cart
            val bundle = Bundle()
            val arrayList : ArrayList<IkanEntity> = ArrayList(ikanEntity)
            bundle.putParcelableArrayList(Constants.DATA_TO_CART_VALUE, arrayList)
            Log.i("cio", "cek dataIkan di marketplace : $arrayList")
            cartFragment.arguments = bundle
            makeCurrentFragment(cartFragment)
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) {
        fragmentManager?.beginTransaction()?.apply {
            replace(R.id.fl_main_ui, fragment)
            addToBackStack(null)
            commit()
        }
    }

    private fun observeData() {
        viewModel.getDataIkanFromFirebase().observe(viewLifecycleOwner) {
            with(ikanAdapter) {
                with(binding.shimmerViewContainer) {
                    stopShimmer()
                    visibility = View.GONE
                }
                setdataIkan(it)
                notifyDataSetChanged()
            }
        }
    }

    override fun onClick(value: IkanEntity) {
        Log.i("cik", "onClick: $value")
        ikanEntity.add(value)
    }

    override fun onBackPressed(): Boolean {
        return false
    }

}