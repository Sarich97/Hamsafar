package hamsafar.tj.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static hamsafar.tj.activity.utility.Utility.BOOKS_COLLECTION;
import static hamsafar.tj.activity.utility.Utility.USERS_COLLECTION;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.badge.BadgeDrawable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import hamsafar.tj.R;
import hamsafar.tj.activity.fragments.CreatFragment;
import hamsafar.tj.activity.fragments.NotificationFragment;
import hamsafar.tj.activity.fragments.ProfileFragment;
import hamsafar.tj.activity.fragments.TravelFragment;
import hamsafar.tj.activity.models.books;
import hamsafar.tj.databinding.ActivityMainBinding;

public class MainActivity extends  AppCompatActivity {

    ActivityMainBinding binding;

    private FirebaseFirestore bookRef;
    private FirebaseAuth firebaseAuth;
    private CollectionReference notificatRef;
    private String userKey;

    private BadgeDrawable badgeDrawable;
    private int currentPageId = -1;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    protected void onStart() {
        showNotificationForUser();
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaseFragment(new TravelFragment());

        firebaseAuth = FirebaseAuth.getInstance();
        bookRef = FirebaseFirestore.getInstance();
        notificatRef = bookRef.collection("notificat");
        userKey = firebaseAuth.getCurrentUser().getUid();

        showNotificationForUser();


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
                case R.id.nav_notification:
                    if (currentPageId == item.getItemId()) {
                        return false;
                    } else {
                        if(badgeDrawable !=null) {
                            badgeDrawable.setVisible(false);
                            notificatRef.document(userKey).collection(BOOKS_COLLECTION).get().addOnSuccessListener(queryDocumentSnapshots -> {
                                for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                                    books books = documentSnapshot.toObject(books.class);
                                    if (books.getPostCreateID().equals(userKey)) {
                                        if(books.getNotifiStatus().equals("show")) {
                                            notificatRef.document(userKey).collection(BOOKS_COLLECTION).document(books.getUserID()+books.getPostID()).update("notifiStatus", "null");
                                        } else {

                                        }

                                    } else {
                                    }
                                }
                            });
                        }
                        replaseFragment(new NotificationFragment());
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

    private void showNotificationForUser() {
        notificatRef.document(userKey).collection(BOOKS_COLLECTION).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                books books = documentSnapshot.toObject(books.class);
                if (books.getPostCreateID().equals(userKey)) {
                    if(books.getNotifiStatus().equals("show")) {
                        badgeDrawable = binding.bottomNavigationView.getOrCreateBadge(R.id.nav_notification);
                        badgeDrawable.setBackgroundColor(Color.RED);
                        badgeDrawable.setVisible(true);
                    } else {

                    }

                } else {
                    badgeDrawable.setVisible(false);
                }
            }
        });
    }

    private void replaseFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}