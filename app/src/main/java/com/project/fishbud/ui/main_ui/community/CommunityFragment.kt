package com.project.fishbud.ui.main_ui.community

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.project.fishbud.R
import com.project.fishbud.databinding.FragmentCommunityBinding
import com.project.fishbud.ui.authentication.AuthenticationActivity
import com.project.fishbud.ui.main_ui.community.create_thread.CreateThreadFragment
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CommunityFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentCommunityBinding
    private lateinit var viewModel: CommunityViewModel
    private lateinit var adapter: CommunityAdapter
    private var user = FirebaseAuth.getInstance().currentUser

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
                Log.i("cekDateTime", "cekDateTime: $it")
//                val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm:ss", Locale.US)
//                val dates = listOf("30-03-2012. 10:20:21", "31-03-2012. 10:20:23", "31-03-2012. 18:20:20", "31-04-2012. 09:10:10")
                val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                val result = it.sortedBy {
                    LocalDate.parse(it.dateTime, dateTimeFormatter)
//                val result : MutableList<CommunityEntity> = it.sortBy { LocalDate.parse(it.dateTime, dateTimeFormatter) }
//                Log.i("cekDateTime", "cekItDate: ${it.dateTime}")
                }
                Log.i("cekDateTime", "cekDateTime: $result")
                setdataThread(result)
                Log.i("cekIkan", "it : $it")
                binding.progressBar.visibility = View.GONE
//                setdataThread(it)
                notifyDataSetChanged()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_addThread -> {
                if (user != null) {
                    val createThreadFragment = CreateThreadFragment()
                    makeCurrentFragment(createThreadFragment)
                } else {
                    context?.startActivity(Intent(context, AuthenticationActivity::class.java))
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

    override fun onResume() {
        val navBar: BottomAppBar? = activity?.findViewById(R.id.bottomAppBar)
        navBar?.visibility = View.VISIBLE
        val scan: FloatingActionButton? = activity?.findViewById(R.id.detection)
        scan?.visibility = View.VISIBLE
        super.onResume()
    }

//    override fun onBackPressed(): Boolean {
//        return false
//    }
}