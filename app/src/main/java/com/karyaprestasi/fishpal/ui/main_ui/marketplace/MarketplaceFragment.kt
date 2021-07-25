package com.karyaprestasi.fishpal.ui.main_ui.marketplace

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.karyaprestasi.fishpal.Constants
import com.karyaprestasi.fishpal.OnItemClick
import com.karyaprestasi.fishpal.R
import com.karyaprestasi.fishpal.databinding.FragmentMarketplaceNewBinding
import com.karyaprestasi.fishpal.ui.authentication.AuthenticationActivity
import com.karyaprestasi.fishpal.ui.main_ui.marketplace.cart.CartFragment
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList


class MarketplaceFragment : Fragment(), OnItemClick {

    private lateinit var binding: FragmentMarketplaceNewBinding

    //    private lateinit var viewModel: IkanViewModel
    private lateinit var ikanAdapter: IkanAdapter
    private var ikanEntity: MutableList<IkanEntity> = mutableListOf()
    private lateinit var data: MutableList<IkanEntity>
    private lateinit var displayData: MutableList<IkanEntity>
    private lateinit var searchQueryFromInfoScan: String
    private var user = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMarketplaceNewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun getDataSearchFromInfoScan() {
        val bundle = arguments
        if (bundle != null) {
            searchQueryFromInfoScan =
                bundle.getString(Constants.DATA_SEARCH_FROM_MAIN_TO_MARKETPLACE).toString()
            if (searchQueryFromInfoScan != "null") {
                val executor = Executors.newSingleThreadExecutor()
                val handler = Handler(Looper.getMainLooper())
                executor.execute {
                    // Simulate process in background thread
                    try {
                        // Simulating something timeconsuming
                        Thread.sleep(500) // in milisecond
                        binding.searchMarketplace.post {
                            binding.searchMarketplace.setQuery(searchQueryFromInfoScan, false)
                        }
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    handler.post {
                        val bgthread = Thread()
                        bgthread.start()
                    }
                }
            }
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) {
        fragmentManager?.beginTransaction()?.apply {
            replace(R.id.fl_main_ui, fragment)
            addToBackStack(null)
            commit()
            val navBar: BottomAppBar? = activity?.findViewById(R.id.bottomAppBar)
            navBar?.visibility = View.GONE
            val scan: FloatingActionButton? = activity?.findViewById(R.id.detection)
            scan?.visibility = View.GONE
        }
    }

//    private fun observeData() {
//        viewModel.getDataIkanFromFirebase().observe(viewLifecycleOwner) {
//            data.addAll(it)
//        }
//    }

    private fun read() {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            // Simulate process in background thread
            try {
                val reference = FirebaseDatabase.getInstance().reference.child("ListProduk")
                reference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        data.clear()
                        displayData.clear()
                        for (dataSnapshot: DataSnapshot in snapshot.children) {
                            val value = dataSnapshot.getValue(IkanEntity::class.java)
                            if (value != null) {
                                data.add(value)
                                ikanAdapter.notifyDataSetChanged()
                            }
                        }
                        displayData.addAll(data)
                        ikanAdapter.notifyDataSetChanged()
                        Log.i("lowk", "diplayData: $displayData")
                        with(binding.shimmerViewContainer) {
                            stopShimmer()
                            visibility = View.GONE
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            handler.post {
                // Update ui in main thread
                val bgthread = Thread()
                bgthread.start()
            }
        }

    }

    private fun search() {
        binding.searchMarketplace.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                //hide keyboard
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotEmpty()) {
                    displayData.clear()
                    val search = newText.lowercase(Locale.getDefault())

                    data.forEach {
                        if (it.namaIkan.lowercase(Locale.getDefault()).contains(search) ||
                            it.tokoIkan.lowercase(Locale.getDefault()).contains(search)
                        ) {
                            displayData.add(it)
                        }
                    }
                    ikanAdapter.notifyDataSetChanged()
                } else {
                    displayData.clear()
                    displayData.addAll(data)
                    ikanAdapter.notifyDataSetChanged()
                }
                return true
            }


        })
    }

    override fun onStop() {
        super.onStop()
        data.clear()
        displayData.clear()
        binding.searchMarketplace.setQuery("", false)
        val bundle = arguments
        bundle?.clear()
    }

    override fun onClick(value: IkanEntity) {
        Log.i("cik", "onClick: $value")
        ikanEntity.add(value)
    }

    override fun onResume() {
        val navBar: BottomAppBar? = activity?.findViewById(R.id.bottomAppBar)
        navBar?.visibility = View.VISIBLE
        val scan: FloatingActionButton? = activity?.findViewById(R.id.detection)
        scan?.visibility = View.VISIBLE

        data = mutableListOf()
        displayData = mutableListOf()
        getDataSearchFromInfoScan()
        read()
//        viewModel = ViewModelProvider(
//            requireActivity(),
//            ViewModelProvider.NewInstanceFactory()
//        )[IkanViewModel::class.java]
        ikanAdapter = IkanAdapter(requireContext(), this, displayData)
//        observeData()
//        ikanAdapter.setdataIkan(data)
        with(binding.rvMarketplace) {
            layoutManager = LinearLayoutManager(context)
            adapter = ikanAdapter
        }
        search()

        binding.cart.setOnClickListener {
            if (user != null) {
                val cartFragment = CartFragment()
                //store data ikan to cart
                val bundle = Bundle()
                val distinct = ikanEntity.distinct()
                val arrayList: ArrayList<IkanEntity> = ArrayList(distinct)
                bundle.putParcelableArrayList(Constants.DATA_TO_CART_VALUE, arrayList)
                Log.i("cio", "cek dataIkan di marketplace : $arrayList")
                cartFragment.arguments = bundle
                makeCurrentFragment(cartFragment)
            } else {
                context?.startActivity(Intent(context, AuthenticationActivity::class.java))
            }
        }
        super.onResume()
    }

//    override fun onBackPressed(): Boolean {
//        return false
//    }

}