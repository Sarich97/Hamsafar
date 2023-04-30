package hamsafar.tj.activity.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


import org.w3c.dom.Text;

import hamsafar.tj.R;
import hamsafar.tj.activity.AuthActivity;
import hamsafar.tj.activity.EditProfileActivity;
import hamsafar.tj.activity.adapters.ProfileFragmentAdapter;


public class ProfileFragment extends Fragment {

    private FirebaseAuth firebaseAuth; // FireBase
    private FirebaseFirestore firebaseFirestore,travelPostRef ;
    private String userID;
    private String user_name, user_phone, user_email, user_car_model, user_rating, user_trip_count;


    private TextView textViewUserName, textViewUserEmail, textViewUserRating, textViewUserTripCount;
    private ImageView userImageP, imageViewLogOurBtn;

    private String setting_list[] = {"Редактировать профиль", "Как это работает", "Написать нам", "Выйти"};


    private Dialog dialogInternetCon;




    private TabLayout tabLayoutAuth;
    private ViewPager2 viewPager2Auth;
    private ProfileFragmentAdapter profileFragmentAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        dialogInternetCon = new Dialog(getContext());

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


        textViewUserRating.setOnClickListener(view12 -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
            bottomSheetDialog.setContentView(R.layout.description_sheet);

            TextView textViewSheetReting = bottomSheetDialog.findViewById(R.id.textSheet);

            textViewSheetReting.setText(R.string.reating_descrip);
            bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            bottomSheetDialog.show();
        });


        textViewUserTripCount.setOnClickListener(view13 -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
            bottomSheetDialog.setContentView(R.layout.description_sheet);

            TextView textViewSheetReting = bottomSheetDialog.findViewById(R.id.textSheet);

            textViewSheetReting.setText(R.string.trip_count_descrip);
            bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            bottomSheetDialog.show();
        });


        showProfileForUser();
               //cardViewRecycler();

//        imageViewLogOurBtn.setOnClickListener(view1 -> { // КНОПКА ВЫЙТИ ИЗ ПРОФИЛЯ!
//
//        });

        imageViewLogOurBtn.setOnClickListener(view14 -> {


            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
            bottomSheetDialog.setContentView(R.layout.listview_settings_menu);

            ListView listViewMenu = bottomSheetDialog.findViewById(R.id.listMenu);
            listViewMenu.setAdapter(new CustomAdapter());

            listViewMenu.setOnItemClickListener((adapterView, view15, i, l) -> {
                switch (i) {
                    case 0:
                        Intent editIntent = new Intent(getContext(), EditProfileActivity.class);
                        editIntent.putExtra("userName", user_name);
                        editIntent.putExtra("userPhone", user_phone);
                        editIntent.putExtra("userEmail", user_email);
                        editIntent.putExtra("userCar", user_car_model);
                        startActivity(editIntent);
                        break;

                    case 1:

                        break;

                    case 2:
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/sarichh"));
                        startActivity(intent);
                        break;

                    case 3:
                        showSingOutDialog();
                        break;

                    default:
                }

            });
            bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            bottomSheetDialog.show();


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





    private void showProfileForUser() {
        firebaseFirestore.collection("users").document(userID).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
                user_name = task.getResult().getString("userName");
                user_phone = task.getResult().getString("userPhone");
                user_email = task.getResult().getString("userEmail");
                user_car_model = task.getResult().getString("userCarModel");
                user_rating = task.getResult().get("userRating").toString();
                user_trip_count = task.getResult().get("userTrip").toString();

                textViewUserName.setText(user_name);
                textViewUserEmail.setText("тел: " + user_phone);
                textViewUserRating.setText(user_rating);
                textViewUserTripCount.setText(user_trip_count);

                String userNameName = user_name.substring(0,1);

                TextDrawable user_drawble = TextDrawable.builder()
                        .beginConfig()
                        .fontSize(42) /* size in px */
                        .bold()
                        .toUpperCase()
                        .endConfig()
                        .buildRoundRect(userNameName, colorGenerator.getRandomColor(),8); // radius in
                userImageP.setImageDrawable(user_drawble);

            } else  {

            }
        });
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return setting_list.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = getLayoutInflater().inflate(R.layout.listmenu_item, null);

            TextView textViewList = v.findViewById(R.id.listViewText);

            textViewList.setText(setting_list[i]);
            return v;
        }
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