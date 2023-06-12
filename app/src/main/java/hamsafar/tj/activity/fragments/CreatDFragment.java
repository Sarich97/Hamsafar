package hamsafar.tj.activity.fragments;

import static android.content.ContentValues.TAG;
import static hamsafar.tj.activity.utility.Utility.POSTS_COLLECTION;
import static hamsafar.tj.activity.utility.Utility.USERS_COLLECTION;
import static hamsafar.tj.activity.utility.Utility.dayMonthText;
import static hamsafar.tj.activity.utility.Utility.getMonthText;
import static hamsafar.tj.activity.utility.Utility.isOnline;
import static hamsafar.tj.activity.utility.Utility.minuteText;
import static hamsafar.tj.activity.utility.Utility.showSnakbarTypeOne;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import hamsafar.tj.R;
import hamsafar.tj.activity.MainActivity;
import hamsafar.tj.activity.utility.Utility;


public class CreatDFragment extends Fragment {

    private Spinner spinnerStartTrip, spinnerEndTrip;
    private EditText editTextPrice, editTextSeat, editTextComment;
    private TextView textViewDateTrip, textViewTimeTrip;
    private Button buttonCreatTrip;
    private SwitchCompat aSwitchPackage;
    private ProgressBar progressBarPost;

    private MediaPlayer mediaPlayerSound;


    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseFirestore travelPostRef;
    private FirebaseUser currentUser;
    private String userID;
    private Dialog dialogInternetCon;


    private int mYear, mMonth, mDay, mHour, mMinute;

    private TimePickerDialog timePickerDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_creat_d, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance(); // DateBase settings
        travelPostRef = FirebaseFirestore.getInstance(); // DateBase settings
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        userID = firebaseAuth.getCurrentUser().getUid();

        mediaPlayerSound = MediaPlayer.create(getActivity(), R.raw.sound);

        dialogInternetCon = new Dialog(getContext());


        spinnerStartTrip = view.findViewById(R.id.spinnerStartTripD);
        spinnerEndTrip = view.findViewById(R.id.spinnerEndTripD);
        textViewDateTrip = view.findViewById(R.id.editTextDateD);
        textViewTimeTrip = view.findViewById(R.id.editTextTimeD);
        editTextPrice = view.findViewById(R.id.editTextPriceD);
        editTextSeat = view.findViewById(R.id.editTextSeatD);
        buttonCreatTrip = view.findViewById(R.id.creatTripD);
        editTextComment = view.findViewById(R.id.editTextCommentD);

        progressBarPost = view.findViewById(R.id.progressBarPostD);

        aSwitchPackage = view.findViewById(R.id.switchPackage);
        if (aSwitchPackage != null) {
            aSwitchPackage.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(isChecked == true) {
                    aSwitchPackage.setText("Беру посылку: Да");
                } else {
                    aSwitchPackage.setText("Беру посылку: Нет");
                }

            });
        }

        textViewDateTrip.setOnClickListener(clickDateTime -> shwoDatePickerDialog());

        textViewTimeTrip.setOnClickListener(clickTime -> showTimePickerDialog());

        buttonCreatTrip.setOnClickListener(view13 -> {
            buttonCreatTrip.setVisibility(View.INVISIBLE);
            progressBarPost.setVisibility(View.VISIBLE);
            firebaseFirestore.collection(POSTS_COLLECTION)
                .whereEqualTo("userUD", userID)
                .whereEqualTo("statusTrip", "show") // Фильтруем только активные поездки
                .get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        buttonCreatTrip.setVisibility(View.VISIBLE);
                        progressBarPost.setVisibility(View.INVISIBLE);
                        Log.e(TAG, "Error getting active posts count:", task.getException());
                        return;
                    }

                    int activePostsCount = task.getResult().size();
                    if (activePostsCount >= 3) {
                        buttonCreatTrip.setVisibility(View.VISIBLE);
                        progressBarPost.setVisibility(View.INVISIBLE);
                        // Если у пользователя уже есть 3 активные поездки, выводим сообщение и не даём создавать новую
                        showSnakbarTypeOne(getView(), "Вы не можете создавать больше 3 активных поездок");
                        return;
                    }

                    // Если есть меньше 3 активных поездок, позволяем пользователю создать новую
                    if (!isOnline(getContext())) {
                        dialogInternetCon.setContentView(R.layout.internet_connecting_dialog);
                        dialogInternetCon.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialogInternetCon.show();
                        return;
                    }

                    String start_Trip = spinnerStartTrip.getSelectedItem().toString();
                    String end_Trip = spinnerEndTrip.getSelectedItem().toString();
                    String data_Trip = textViewDateTrip.getText().toString();
                    String time_Trip = textViewTimeTrip.getText().toString();
                    String price_Trip = editTextPrice.getText().toString();
                    String seat_Trip = editTextSeat.getText().toString();
                    String package_trip = aSwitchPackage.getText().toString();
                    String comments = editTextComment.getText().toString();

                    if (start_Trip.equals("Откуда")) {
                        showSnakbarTypeOne(getView(),"Укажите начальную точку маршрута");
                        buttonCreatTrip.setVisibility(View.VISIBLE);
                        progressBarPost.setVisibility(View.INVISIBLE);
                    }  else if (end_Trip.equals("Куда")) {
                        showSnakbarTypeOne(getView(),"Укажите конечную точку маршрута");
                        buttonCreatTrip.setVisibility(View.VISIBLE);
                        progressBarPost.setVisibility(View.INVISIBLE);
                    } else if (TextUtils.isEmpty(data_Trip)) {
                        textViewDateTrip.setError("Укажите дату");
                        buttonCreatTrip.setVisibility(View.VISIBLE);
                        progressBarPost.setVisibility(View.INVISIBLE);
                    } else if (time_Trip.equals("Время")) {
                        textViewTimeTrip.setError("Укажите время");
                        buttonCreatTrip.setVisibility(View.VISIBLE);
                        progressBarPost.setVisibility(View.INVISIBLE);
                    } else if (TextUtils.isEmpty(price_Trip)) {
                        editTextPrice.setError("Укажите цену");
                        buttonCreatTrip.setVisibility(View.VISIBLE);
                        progressBarPost.setVisibility(View.INVISIBLE);
                    } else if (TextUtils.isEmpty(seat_Trip)) {
                        editTextSeat.setError("Укажите количество мест");
                        buttonCreatTrip.setVisibility(View.VISIBLE);
                        progressBarPost.setVisibility(View.INVISIBLE);
                    }
                    else {
                        createPost(start_Trip, end_Trip, data_Trip, time_Trip, price_Trip, seat_Trip, package_trip, comments);
                    }
                });
        });

        return view;
    }



    private void createPost(String start_trip, String end_trip, String data_trip, String time_trip, String price_trip, String seat_trip, String package_trip, String comments) {

        // Получаем требуемые данные из документа пользователя в базе данных
        firebaseFirestore.collection(Utility.USERS_COLLECTION).document(userID).get().addOnSuccessListener(documentSnapshot -> {
            // Получаем имя пользователя, телефон и модель автомобиля из документа пользователя
            String user_name = documentSnapshot.getString("userName");
            String user_phone = documentSnapshot.getString("userPhone");
            String user_car = documentSnapshot.getString("userCarModel");
            String rating = documentSnapshot.get("userRating").toString();

            // Создаем новый документ поста в коллекции "posts"
            DocumentReference postRef = firebaseFirestore.collection(Utility.POSTS_COLLECTION).document();
            // Создаем объект Map, содержащий данные поста
            Map<String, Object> post = new HashMap<>();
            post.put("userUD", userID);
            post.put("userName", user_name);
            post.put("userPhone", user_phone);
            post.put("carModel", user_car);
            post.put("startTrip", start_trip);
            post.put("endTrip", end_trip);
            post.put("dataTrip", data_trip);
            post.put("timeTrip", time_trip);
            post.put("priceTrip", price_trip);
            post.put("seatTrip", seat_trip);
            post.put("commentTrip", comments);
            post.put("rating", Integer.parseInt(rating));
            post.put("isDriverUser", "Ищу пассажиров");
            post.put("statusTrip", "show");
            post.put("isPackage", package_trip);
            post.put("postId", postRef.getId());
            post.put("timestamp", FieldValue.serverTimestamp());

            // Записываем данные поста в базу данных
            postRef.set(post).addOnSuccessListener(aVoid -> {
//                sendMessageToTelegram("Новый пост добавлен: "+start_trip+" - "+end_trip+" ("+data_trip+", "+time_trip+")", "@sarvar_sultan");
                // Если запись прошла успешно, то запускаем звуковой эффект и переходим на главный экран
                mediaPlayerSound.start();
                Intent mainIntent = new Intent(getContext(), MainActivity.class);
                startActivity(mainIntent);
                getActivity().finish();
            }).addOnFailureListener(e -> {
                // Если запись не удалась, то отображаем сообщение об ошибке
                buttonCreatTrip.setVisibility(View.VISIBLE);
                progressBarPost.setVisibility(View.INVISIBLE);
                showSnakbarTypeOne(getView(),"Ошибка публикации поста. Повторите попытку через несколько минут");
            });
        });
    }

    private void shwoDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 0);
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    textViewDateTrip.setText(String.format("%s.%s.%s", dayMonthText(dayOfMonth), getMonthText(monthOfYear), year));
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        boolean is24HourView= true;
        timePickerDialog = new TimePickerDialog(getContext(), (timePicker, i, i1) -> {
                textViewTimeTrip.setText(String.format("%s:%s", dayMonthText(i), minuteText(i1)));
        },mHour,mMinute,is24HourView);
        timePickerDialog.show();
    }

}