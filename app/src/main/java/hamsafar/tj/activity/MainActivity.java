package hamsafar.tj.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.os.Bundle;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


import hamsafar.tj.R;
import hamsafar.tj.activity.fragments.CreatFragment;

import hamsafar.tj.activity.fragments.ProfileFragment;
import hamsafar.tj.activity.fragments.TravelFragment;

import hamsafar.tj.databinding.ActivityMainBinding;

public class MainActivity extends  AppCompatActivity {

    ActivityMainBinding binding;

    private FirebaseAuth firebaseAuth;
    private String userKey;

    private int currentPageId = -1;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaseFragment(new TravelFragment());



        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.nav_posts:
                    if (currentPageId == item.getItemId()) {
                        return false;
                    } else {
                        replaseFragment(new TravelFragment());
                        currentPageId = item.getItemId();
                    }
                    break;
                case R.id.nav_creat:
                    if (currentPageId == item.getItemId()) {
                        return false;
                    } else {
                        replaseFragment(new CreatFragment());
                        currentPageId = item.getItemId();
                    }
                    break;
                case R.id.nav_profile:
                    if (currentPageId == item.getItemId()) {
                        return false;
                    } else {
                        replaseFragment(new ProfileFragment());
                        currentPageId = item.getItemId();
                    }
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