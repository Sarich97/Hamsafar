package hamsafar.tj.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import hamsafar.tj.R;
import hamsafar.tj.activity.adapters.AuthFragmentAdapter;

public class AuthActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TabLayout tabLayoutAuth;
    private ViewPager2 viewPager2Auth;
    private AuthFragmentAdapter authFragmentAdapter;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            sendUserToMainActivity();
        }
    }

    @Override
    protected void onDestroy() {
        authFragmentAdapter = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mAuth = FirebaseAuth.getInstance();

        tabLayoutAuth = findViewById(R.id.tabLayout);
        viewPager2Auth = findViewById(R.id.viewPager);

        tabLayoutAuth.addTab(tabLayoutAuth.newTab().setText("Вход"));
        tabLayoutAuth.addTab(tabLayoutAuth.newTab().setText("Регистрация"));

        FragmentManager fragmentManager = getSupportFragmentManager();
        authFragmentAdapter = new AuthFragmentAdapter(fragmentManager , getLifecycle());
        viewPager2Auth.setAdapter(authFragmentAdapter);

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


    }

    private void sendUserToMainActivity() {
        Intent mainIntent = new Intent(AuthActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}