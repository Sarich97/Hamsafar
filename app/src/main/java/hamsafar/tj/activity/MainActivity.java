package hamsafar.tj.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.badge.BadgeDrawable;

import hamsafar.tj.R;
import hamsafar.tj.activity.fragments.CreatFragment;
import hamsafar.tj.activity.fragments.ProfileFragment;
import hamsafar.tj.activity.fragments.TravelFragment;
import hamsafar.tj.databinding.ActivityMainBinding;

public class MainActivity extends  AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaseFragment(new TravelFragment());



        BadgeDrawable badgeDrawable = binding.bottomNavigationView.getOrCreateBadge(R.id.nav_creat);
        badgeDrawable.setNumber(10);
        badgeDrawable.setVisible(true);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.nav_posts:
                    replaseFragment(new TravelFragment());
                    break;
                case R.id.nav_creat:
                    replaseFragment(new CreatFragment());
                    break;
                case R.id.nav_profile:
                    replaseFragment(new ProfileFragment());
                    break;
            }

            return true;
        });

    }
    private void replaseFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}