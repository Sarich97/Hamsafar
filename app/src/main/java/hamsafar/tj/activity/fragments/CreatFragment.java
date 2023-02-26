package hamsafar.tj.activity.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import hamsafar.tj.R;
import hamsafar.tj.activity.adapters.CreatFragmentAdapter;


public class CreatFragment extends Fragment {



    private TabLayout tabLayoutAuth;
    private ViewPager2 viewPager2Auth;
    private CreatFragmentAdapter creatFragmentAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_creat, container, false);

        tabLayoutAuth = view.findViewById(R.id.tabLayoutFragment);
        viewPager2Auth = view.findViewById(R.id.viewPagerFragment);

        tabLayoutAuth.addTab(tabLayoutAuth.newTab().setText("Я водитель"));
        tabLayoutAuth.addTab(tabLayoutAuth.newTab().setText("Я пассажир"));

        FragmentManager fragmentManager = getChildFragmentManager();
        creatFragmentAdapter = new CreatFragmentAdapter(fragmentManager , getLifecycle());
        viewPager2Auth.setAdapter(creatFragmentAdapter);

        tabLayoutAuth.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2Auth.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2Auth.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayoutAuth.selectTab(tabLayoutAuth.getTabAt(position));
            }
        });
        return  view;
    }




}