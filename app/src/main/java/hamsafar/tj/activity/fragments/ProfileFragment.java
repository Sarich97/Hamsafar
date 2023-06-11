package hamsafar.tj.activity.fragments;

import static android.content.ContentValues.TAG;
import static hamsafar.tj.activity.utility.Utility.USERS_COLLECTION;

import android.app.Dialog;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;


import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import hamsafar.tj.R;
import hamsafar.tj.activity.AuthActivity;
import hamsafar.tj.activity.EditProfileActivity;
import hamsafar.tj.activity.HelpActivity;
import hamsafar.tj.activity.adapters.ProfileFragmentAdapter;


public class ProfileFragment extends Fragment {

    // Объявление переменных класса
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore, travelPostRef ;
    private String userID;
    private boolean mDataLoaded = false;
    private String mUserName, mUserPhone, mUserEmail, mUserCarModel, mUserRating, mUserTripCount;

    private TextView textViewUserName, textViewUserEmail, textViewUserRating, textViewUserTripCount;
    private ImageView userImageP, imageViewLogOurBtn;

    // Массив для хранения списка настроек
    private String setting_list[] = {"Редактировать профиль", "Помощь", "Написать нам", "Выйти"};

    private Dialog dialogInternetCon;

    private TabLayout tabLayoutAuth;
    private ViewPager2 viewPager2Auth;
    private ProfileFragmentAdapter profileFragmentAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Заполнение макета фрагмента
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Инициализация диалогового окна
        dialogInternetCon = new Dialog(getContext());

        // Инициализация переменных FireBase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        travelPostRef = FirebaseFirestore.getInstance();

        // Получение ID текущего пользователя
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        userID = firebaseAuth.getCurrentUser().getUid();

        // Инициализация текстовых полей и изображения профиля
        textViewUserName = view.findViewById(R.id.userNameProfile);
        textViewUserEmail = view.findViewById(R.id.userEmail);
        textViewUserRating = view.findViewById(R.id.textViewRating);
        textViewUserTripCount = view.findViewById(R.id.textViewTripCount);
        userImageP = view.findViewById(R.id.userImageProfile);
        imageViewLogOurBtn = view.findViewById(R.id.imageViewLogout);


        // Обработчик нажатия на текстовое поле textViewUserRating
        textViewUserRating.setOnClickListener(view12 -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
            bottomSheetDialog.setContentView(R.layout.description_sheet);

            TextView textViewSheetReting = bottomSheetDialog.findViewById(R.id.textSheet);

            textViewSheetReting.setText(R.string.reating_descrip);
            bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            bottomSheetDialog.show();
        });

        // Обработчик нажатия на текстовое поле textViewUserTripCount
        textViewUserTripCount.setOnClickListener(view13 -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
            bottomSheetDialog.setContentView(R.layout.description_sheet);

            TextView textViewSheetReting = bottomSheetDialog.findViewById(R.id.textSheet);

            textViewSheetReting.setText(R.string.trip_count_descrip);
            bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            bottomSheetDialog.show();
        });

        // Отображение профиля пользователя
        showProfileForUser();

        // Обработчик нажатия на кнопку выхода из аккаунта
        imageViewLogOurBtn.setOnClickListener(view14 -> {
            // Создание диалогового окна
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
            bottomSheetDialog.setContentView(R.layout.listview_settings_menu);

            // Инициализация ListView и установка адаптера
            ListView listViewMenu = bottomSheetDialog.findViewById(R.id.listMenu);
            listViewMenu.setAdapter(new CustomAdapter());

            // Обработчик нажатия на пункт списка
            listViewMenu.setOnItemClickListener((adapterView, view15, i, l) -> {
                switch (i) {
                    case 0:
                        // Открытие активности для редактирования профиля
                        Intent editIntent = new Intent(getContext(), EditProfileActivity.class);
                        editIntent.putExtra("userName", mUserName);
                        editIntent.putExtra("userPhone", mUserPhone);
                        editIntent.putExtra("userEmail", mUserEmail);
                        editIntent.putExtra("userCar", mUserCarModel);
                        startActivity(editIntent);
                        break;

                    case 1:
                        Intent helpIntent = new Intent(getContext(), HelpActivity.class);
                        startActivity(helpIntent);
                        break;

                    case 2:
                        // Открыти
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/sarichh"));
                        startActivity(intent);
                        break;

                    case 3:
                        showSignOutDialog();
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

    private void showSignOutDialog() {
        AlertDialog.Builder signOutDialog = new AlertDialog.Builder(getContext());
        signOutDialog.setMessage("Вы уверены, что хотите выйти из аккаунта?")
        .setPositiveButton("Да", (dialog, which) -> {
            firebaseAuth.signOut();
            Intent authIntent = new Intent(getContext(), AuthActivity.class);
            startActivity(authIntent);
            getActivity().finish();
        })
        .setNegativeButton("Отмена", (dialog, which) -> dialog.cancel())
        .setCancelable(true)
        .show();
    }


    private void showProfileForUser() {
        if (mDataLoaded) {
            updateUI(mUserName, mUserPhone, mUserEmail, mUserCarModel, mUserRating, mUserTripCount);
        } else {
            loadUserData();
        }
    }

    private void loadUserData() {
        firebaseFirestore.collection(USERS_COLLECTION).document(userID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot userDocument = task.getResult();
                        if (userDocument.exists()) {
                            mUserName = userDocument.getString("userName");
                            mUserPhone = userDocument.getString("userPhone");
                            mUserEmail = userDocument.getString("userEmail");
                            mUserCarModel = userDocument.getString("userCarModel");
                            mUserRating = userDocument.get("userRating").toString();
                            mUserTripCount = userDocument.get("userTrip").toString();

                            mDataLoaded = true;

                            updateUI(mUserName, mUserPhone, mUserEmail, mUserCarModel, mUserRating, mUserTripCount);
                        }
                    } else {
                        // Обработка ошибок при неудачном запросе к Firestore
                        Log.e(TAG, "Error loading user data", task.getException());
                    }
                });
    }

    private void updateUI(String userName, String userPhone, String userEmail, String userCarModel,
                          String userRating, String userTripCount) {
        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
        String userNameInitial = userName.substring(0, 1);
        TextDrawable userDrawable = createTextDrawable(userNameInitial, colorGenerator.getRandomColor());

        userImageP.setImageDrawable(userDrawable);
        textViewUserName.setText(userName);
        textViewUserEmail.setText("тел: " + userPhone);
        textViewUserRating.setText(userRating);
        textViewUserTripCount.setText(userTripCount);
    }

    private TextDrawable createTextDrawable(String text, int color) {
        return TextDrawable.builder()
                .beginConfig()
                .fontSize(42)
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRoundRect(text, color, 8);
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

}