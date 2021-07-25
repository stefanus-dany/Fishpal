//package com.project.fishbud.ui.main_ui.profile;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import com.project.fishbud.R;
//import android.view.ViewGroup;
//
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.viewpager.widget.ViewPager;
//
//import com.google.android.material.tabs.TabLayout;
//import com.project.fishbud.ui.viewPager.AkunPembeliFragment;
//import com.project.fishbud.ui.viewPager.AkunTokoFragment;
//import com.project.fishbud.ui.viewPager.SectionPagerAdapter;
//
//import java.lang.reflect.Field;
//
//public class ProfileFragment extends Fragment {
//
//    View myFragment;
//
//    ViewPager viewPager;
//    TabLayout tabLayout;
//
//
//    public ProfileFragment() {
//        // Required empty public constructor
//    }
//
//    public static ProfileFragment getInstance()    {
//        return new ProfileFragment();
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        myFragment = inflater.inflate(R.layout.fragment_profile2, container, false);
//
//        viewPager = myFragment.findViewById(R.id.viewPager);
//        tabLayout = myFragment.findViewById(R.id.tabLayout);
//
//        return myFragment;
//    }
//
//    //Call onActivity Create method
//
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        setUpViewPager(viewPager);
//        tabLayout.setupWithViewPager(viewPager);
//
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//    }
//
//    private void setUpViewPager(ViewPager viewPager) {
//        SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager());
//
//        adapter.addFragment(new AkunPembeliFragment(), "Akun Pembeli");
//        adapter.addFragment(new AkunTokoFragment(), "Akun Toko");
//
//        viewPager.setAdapter(adapter);
//    }
//
////    @Override
////    public void onDetach() {
////        super.onDetach();
////        try {
////            Field childFragmentManager = Fragment.class
////                    .getDeclaredField("mChildFragmentManager");
////            childFragmentManager.setAccessible(true);
////            childFragmentManager.set(this, null);
////        } catch (NoSuchFieldException e) {
////            throw new RuntimeException(e);
////        } catch (IllegalAccessException e) {
////            throw new RuntimeException(e);
////        }
////    }
//}