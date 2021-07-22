package com.project.fishbud.ui.viewPager

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.fishbud.Constants
import com.project.fishbud.R
import com.project.fishbud.databinding.FragmentAkunTokoBinding
import com.project.fishbud.ui.main_ui.profile.fisherman.my_product.MyProductFragment
import com.project.fishbud.ui.main_ui.profile.fisherman.new_order.NewOrderFragment

class AkunTokoFragment : Fragment(), View.OnClickListener {

    private lateinit var binding : FragmentAkunTokoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAkunTokoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.waitingPayment.setOnClickListener(this)
        binding.newOrder.setOnClickListener(this)
        val sharedPreferences = activity?.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        if (sharedPreferences != null) {
            binding.fishermanName.text = sharedPreferences.getString(Constants.USER_NAME, "")
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
        when(v?.id){
            R.id.waiting_payment -> {
                val myProdukFragment = MyProductFragment()
                makeCurrentFragment(myProdukFragment)
            }

            R.id.new_order -> {
                val newOrderFragment = NewOrderFragment()
                makeCurrentFragment(newOrderFragment)
            }
        }
    }
}