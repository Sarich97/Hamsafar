package hamsafar.tj.activity.fragments;

import static hamsafar.tj.activity.utility.Utility.showToast;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import hamsafar.tj.R;
import hamsafar.tj.activity.MainActivity;


public class CreatDFragment extends Fragment {

    private Spinner startTripSpinnerD, endTripSpinnerD;
    private EditText priceTripD, seatTripD, carModelD, commentsEdit;
    private TextView dateTripD, timeTripD;
    private Button creatTripBtnD;
    private ProgressBar progressBarCreatPostD;


    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser currentUser;
    private String userID;

    private int mYear, mMonth, mDay, mHour, mMinute;

    private TimePickerDialog timePickerDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_creat_d, container, false);


        firebaseFirestore = FirebaseFirestore.getInstance(); // DateBase settings
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();


        startTripSpinnerD = view.findViewById(R.id.spinnerStartTripD);
        endTripSpinnerD = view.findViewById(R.id.spinnerEndTripD);
        dateTripD = view.findViewById(R.id.editTextDateD);
        timeTripD = view.findViewById(R.id.editTextTimeD);
        priceTripD = view.findViewById(R.id.editTextPriceD);
        seatTripD = view.findViewById(R.id.editTextSeatD);
        carModelD = view.findViewById(R.id.editCarModelD);
        creatTripBtnD = view.findViewById(R.id.creatTripD);
        commentsEdit = view.findViewById(R.id.editTextCommentD);
        progressBarCreatPostD = view.findViewById(R.id.progressBarPostD);


        dateTripD.setOnClickListener(clickDateTime -> shwoDatePickerDialog());

        timeTripD.setOnClickListener(clickTime -> showTimePickerDialog());

        creatTripBtnD.setOnClickListener(view13 -> {
            creatTripBtnD.setVisibility(View.INVISIBLE);
            progressBarCreatPostD.setVisibility(View.VISIBLE);

            String start_Trip = startTripSpinnerD.getSelectedItem().toString();
            String end_Trip = endTripSpinnerD.getSelectedItem().toString();
            String data_Trip = dateTripD.getText().toString();
            String time_Trip = timeTripD.getText().toString();
            String price_Trip = priceTripD.getText().toString();
            String seat_Trip = seatTripD.getText().toString();
            String car_model = carModelD.getText().toString();
            String comments = commentsEdit.getText().toString();


            if(start_Trip.equals("Откуда")) {
                showToast(getContext(),"Укажите начальную точку маршрута");
                creatTripBtnD.setVisibility(View.VISIBLE);
                progressBarCreatPostD.setVisibility(View.INVISIBLE);
            }  else if(end_Trip.equals("Куда")) {
                showToast(getContext(), "Укажите конечную точку маршрута");
                creatTripBtnD.setVisibility(View.VISIBLE);
                progressBarCreatPostD.setVisibility(View.INVISIBLE);
            } else if(TextUtils.isEmpty(data_Trip)) {
                dateTripD.setError("Укажите дату");
                creatTripBtnD.setVisibility(View.VISIBLE);
                progressBarCreatPostD.setVisibility(View.INVISIBLE);
            } else if(TextUtils.isEmpty(time_Trip)) {
                timeTripD.setError("Укажите время");
                creatTripBtnD.setVisibility(View.VISIBLE);
                progressBarCreatPostD.setVisibility(View.INVISIBLE);
            } else if(TextUtils.isEmpty(price_Trip)) {
                priceTripD.setError("Укажите цену");
                creatTripBtnD.setVisibility(View.VISIBLE);
                progressBarCreatPostD.setVisibility(View.INVISIBLE);
            } else if(TextUtils.isEmpty(seat_Trip)) {
                seatTripD.setError("Укажите количество мест");
                creatTripBtnD.setVisibility(View.VISIBLE);
                progressBarCreatPostD.setVisibility(View.INVISIBLE);
            } else if(TextUtils.isEmpty(car_model)) {
                carModelD.setError("Укажите марку вашего автомобиля");
                creatTripBtnD.setVisibility(View.VISIBLE);
                progressBarCreatPostD.setVisibility(View.INVISIBLE);
            } else  {
                creatPost(start_Trip, end_Trip, data_Trip, time_Trip, price_Trip, seat_Trip, car_model, comments);
            }
        });

        return view;
    }

    private void creatPost(String start_trip, String end_trip, String data_trip, String time_trip, String price_trip, String seat_trip, String car_model, String comments) {
        userID = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore.collection("users").document(userID).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                String user_name = task.getResult().getString("userName");
                String user_phone = task.getResult().getString("userPhone");

                DocumentReference documentReference = firebaseFirestore.collection("posts").document();
                Map<String, Object> post = new HashMap<>();
                post.put("userUD", userID);
                post.put("userName", user_name);
                post.put("userPhone", user_phone);
                post.put("carModel", car_model);
                post.put("startTrip", start_trip);
                post.put("endTrip", end_trip);
                post.put("dataTrip", data_trip);
                post.put("timeTrip", time_trip);
                post.put("priceTrip", price_trip);
                post.put("seatTrip", seat_trip);
                post.put("commentTrip", comments);
                post.put("isDriverUser", "Ищу пассажиров");
                post.put("postId", documentReference.getId());
                post.put("timestamp", FieldValue.serverTimestamp());

                documentReference.set(post).addOnCompleteListener(task1 -> {
                    if(task1.isSuccessful()) {
                        Intent mainIntent = new Intent(getContext(), MainActivity.class);
                        startActivity(mainIntent);
                        getActivity().finish();
                    } else {
                        creatTripBtnD.setVisibility(View.VISIBLE);
                        progressBarCreatPostD.setVisibility(View.INVISIBLE);
                        showToast(getContext(), "Ошибка публикации поста. Повторите попытку через несколько минут");
                    }
                });
            }
        });
    }

    private void shwoDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    String monthName;
                    switch (monthOfYear) {
                        case 0:
                            monthName = "Янв";
                            break;
                        case 1:
                            monthName = "Фев";
                            break;
                        case 2:
                            monthName = "Март";
                            break;
                        case 3:
                            monthName = "Апр";
                            break;
                        case 4:
                            monthName = "Мая";
                            break;
                        case 5:
                            monthName = "Июня";
                            break;
                        case 6:
                            monthName = "Июля";
                            break;
                        case 7:
                            monthName = "Авг";
                            break;
                        case 8:
                            monthName = "Сен";
                            break;
                        case 9:
                            monthName = "Окт";
                            break;
                        case 10:
                            monthName = "Ноя";
                            break;
                        case 11:
                            monthName = "Дек";
                            break;
                        default:
                            monthName = "Invalid month";
                            break;

                    }

                    dateTripD.setText(dayOfMonth + "." + (monthName) + "." + year);

                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        boolean is24HourView= true;
        timePickerDialog = new TimePickerDialog(getContext(), (timePicker, i, i1) -> timeTripD.setText(i + ":" + i1),mHour,mMinute,is24HourView);
        timePickerDialog.show();
    }
}