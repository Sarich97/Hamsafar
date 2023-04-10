package hamsafar.tj.activity.fragments;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;


import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;

import hamsafar.tj.R;
import hamsafar.tj.activity.AuthActivity;
import hamsafar.tj.activity.adapters.CardViewAdapter;
import hamsafar.tj.activity.adapters.ProfileFragmentAdapter;
import hamsafar.tj.activity.models.CardViewModel;


public class ProfileFragment extends Fragment {

    private FirebaseAuth firebaseAuth; // FireBase
    private FirebaseFirestore firebaseFirestore,travelPostRef ;
    private String userID;

    private TextView textViewUserName, textViewUserEmail, textViewUserRating, textViewUserTripCount;
    private ImageView userImageP, imageViewLogOurBtn;


    private RecyclerView recyclerViewCard;
    private RecyclerView.Adapter cardViewAdapter;





    private TabLayout tabLayoutAuth;
    private ViewPager2 viewPager2Auth;
    private ProfileFragmentAdapter profileFragmentAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);



        firebaseAuth = FirebaseAuth.getInstance();  // User Table variable
        firebaseFirestore = FirebaseFirestore.getInstance();
        travelPostRef = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        userID = firebaseAuth.getCurrentUser().getUid();

       // recyclerViewCard = view.findViewById(R.id.recyclerViewCard); //CARDVIEW


        textViewUserName = view.findViewById(R.id.userNameProfile);
        textViewUserEmail = view.findViewById(R.id.userEmail);
        textViewUserRating = view.findViewById(R.id.textViewRating);
        textViewUserTripCount = view.findViewById(R.id.textViewTripCount);
        userImageP = view.findViewById(R.id.userImageProfile);
        imageViewLogOurBtn = view.findViewById(R.id.imageViewLogout);


        showProfileForUser();
               //cardViewRecycler();

        imageViewLogOurBtn.setOnClickListener(view1 -> { // КНОПКА ВЫЙТИ ИЗ ПРОФИЛЯ!
            showSingOutDialog();
        });


        tabLayoutAuth = view.findViewById(R.id.tabLayoutFragment);
        viewPager2Auth = view.findViewById(R.id.viewPagerFragment);

        tabLayoutAuth.addTab(tabLayoutAuth.newTab().setIcon(R.drawable.baseline_note_add_24));
        tabLayoutAuth.addTab(tabLayoutAuth.newTab().setIcon(R.drawable.baseline_directions_car_24));

        FragmentManager fragmentManager = getChildFragmentManager();
        profileFragmentAdapter = new ProfileFragmentAdapter(fragmentManager , getLifecycle());
        viewPager2Auth.setAdapter(profileFragmentAdapter);

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


        return view;
    }

    private void showSingOutDialog() {
        AlertDialog.Builder logOutDialog = new AlertDialog.Builder(getContext());
        // Указываем текст сообщение
        logOutDialog.setMessage("Вы уверены, что хотите выйти из аккаунта?");

        logOutDialog.setPositiveButton("Да", (dialog, which) -> {
            firebaseAuth.signOut();
            Intent authIntent = new Intent(getContext(), AuthActivity.class);
            startActivity(authIntent);
            getActivity().finish();
        });
        // Обработчик на нажатие НЕТ
        logOutDialog.setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());

        // показываем Alert
        logOutDialog.show();
    }


    private void cardViewRecycler() {
        GradientDrawable gradient2 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xFF105dd0, 0xFF105dd0});
        GradientDrawable gradient1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xFF9937fc, 0xFF9937fc});
        GradientDrawable gradient3 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xfffb7e36, 0xFFfb7e36});
        GradientDrawable gradient4 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xff0fd59e, 0xff0fd59e});
        GradientDrawable gradient5 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffb8cee0, 0xffb8cee0});

        recyclerViewCard.setHasFixedSize(true);
        recyclerViewCard.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        ArrayList<CardViewModel> cardViewModels = new ArrayList<>();
        cardViewModels.add(new CardViewModel(R.drawable.ic_baseline_add_road_24, "Состояние автодорог", gradient1));
        cardViewModels.add(new CardViewModel(R.drawable.ic_baseline_add_moderator_24, "Безопасное вождение", gradient2));
        cardViewModels.add(new CardViewModel(R.drawable.ic_baseline_airplane_ticket_24, "Дешевые авиабилеты", gradient5));

        cardViewAdapter = new CardViewAdapter(cardViewModels, getContext());
        recyclerViewCard.setAdapter(cardViewAdapter);
    }


    private void showProfileForUser() {
        firebaseFirestore.collection("users").document(userID).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
                String user_name = task.getResult().getString("userName");
                String user_phone = task.getResult().getString("userPhone");
                String user_rating = task.getResult().get("userRating").toString();
                String user_trip_count = task.getResult().get("userTrip").toString();

                textViewUserName.setText(user_name);
                textViewUserEmail.setText(user_phone);
                textViewUserRating.setText(user_rating);
                textViewUserTripCount.setText(user_trip_count);

                String userNameName = user_name.substring(0,1);

                TextDrawable user_drawble = TextDrawable.builder()
                        .beginConfig()
                        .fontSize(32) /* size in px */
                        .bold()
                        .toUpperCase()
                        .endConfig()
                        .buildRoundRect(userNameName, colorGenerator.getRandomColor(),12); // radius in
                userImageP.setImageDrawable(user_drawble);

            } else  {

            }
        });
    }

//    private void showPostForUser() {
//        Query query = travelPostRef.collection("posts");
//
//        query.addSnapshotListener((documentSnapshots, e) -> {
//            if (e != null) {
//            } else {
//                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
//                    if (doc.getType() == DocumentChange.Type.ADDED) {
//                        Post post = doc.getDocument().toObject(Post.class);
//                        if(post.getUserUD().equals(userID))
//                        {
//                            posts.add(post);
//                            postAdapter.notifyDataSetChanged();
//                        } else {
//
//                        }
//
//                    }
//                }
//            }
//
//        });
//    }

}