package com.project.fishbud.ui.viewPager

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.project.fishbud.R

class SectionsPagerAdapter(private val mContext: Context, fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.akun_pembeli, R.string.akun_toko)
    }

    override fun getItem(position: Int): Fragment =
        when (position) {
            0 -> AkunPembeliFragment()
            1 -> AkunToko()
            else -> Fragment()
        }

    override fun getPageTitle(position: Int): CharSequence? = mContext.resources.getString(
        TAB_TITLES[position])

    override fun getCount(): Int = 2

//    private val _fragments = ArrayList<Fragment>()
//
//    fun add(fragment: Fragment) {
//        _fragments.add(fragment)
//    }
//
//    override fun getItem(position: Int): Fragment {
//        return _fragments!![position]
//    }
//
//    override fun getPageTitle(position: Int): CharSequence? {
//        return "Test"
//    }
//
//    override fun getCount(): Int {
//        return 2
//    }

}