package hamsafar.tj.activity.fragments;

import static hamsafar.tj.activity.utility.Utility.dayMonthText;
import static hamsafar.tj.activity.utility.Utility.getMonthText;
import static hamsafar.tj.activity.utility.Utility.minuteText;
import static hamsafar.tj.activity.utility.Utility.showToast;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.ImageView;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import hamsafar.tj.R;
import hamsafar.tj.activity.MainActivity;


public class CreatDFragment extends Fragment {

    private Spinner spinnerStartTrip, spinnerEndTrip;
    private EditText editTextPrice, editTextSeat, editTextCarModel, editTextComment;
    private TextView textViewDateTrip, textViewTimeTrip;
    private Button buttonCreatTrip;
    private ProgressBar progressBarPost;
    private Dialog dialogCreatPost;


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




        spinnerStartTrip = view.findViewById(R.id.spinnerStartTripD);
        spinnerEndTrip = view.findViewById(R.id.spinnerEndTripD);
        textViewDateTrip = view.findViewById(R.id.editTextDateD);
        textViewTimeTrip = view.findViewById(R.id.editTextTimeD);
        editTextPrice = view.findViewById(R.id.editTextPriceD);
        editTextSeat = view.findViewById(R.id.editTextSeatD);
        editTextCarModel = view.findViewById(R.id.editCarModelD);
        buttonCreatTrip = view.findViewById(R.id.creatTripD);
        editTextComment = view.findViewById(R.id.editTextCommentD);
        progressBarPost = view.findViewById(R.id.progressBarPostD);


        textViewDateTrip.setOnClickListener(clickDateTime -> shwoDatePickerDialog());

        textViewTimeTrip.setOnClickListener(clickTime -> showTimePickerDialog());

        buttonCreatTrip.setOnClickListener(view13 -> {
            buttonCreatTrip.setVisibility(View.INVISIBLE);
            progressBarPost.setVisibility(View.VISIBLE);

            String start_Trip = spinnerStartTrip.getSelectedItem().toString();
            String end_Trip = spinnerEndTrip.getSelectedItem().toString();
            String data_Trip = textViewDateTrip.getText().toString();
            String time_Trip = textViewTimeTrip.getText().toString();
            String price_Trip = editTextPrice.getText().toString();
            String seat_Trip = editTextSeat.getText().toString();
            String car_model = editTextCarModel.getText().toString();
            String comments = editTextComment.getText().toString();


            if(start_Trip.equals("Откуда")) {
                showToast(getContext(),"Укажите начальную точку маршрута");
                buttonCreatTrip.setVisibility(View.VISIBLE);
                progressBarPost.setVisibility(View.INVISIBLE);
            }  else if(end_Trip.equals("Куда")) {
                showToast(getContext(), "Укажите конечную точку маршрута");
                buttonCreatTrip.setVisibility(View.VISIBLE);
                progressBarPost.setVisibility(View.INVISIBLE);
            } else if(TextUtils.isEmpty(data_Trip)) {
                textViewDateTrip.setError("Укажите дату");
                buttonCreatTrip.setVisibility(View.VISIBLE);
                progressBarPost.setVisibility(View.INVISIBLE);
            } else if(TextUtils.isEmpty(time_Trip)) {
                textViewTimeTrip.setError("Укажите время");
                buttonCreatTrip.setVisibility(View.VISIBLE);
                progressBarPost.setVisibility(View.INVISIBLE);
            } else if(TextUtils.isEmpty(price_Trip)) {
                editTextSeat.setError("Укажите цену");
                buttonCreatTrip.setVisibility(View.VISIBLE);
                progressBarPost.setVisibility(View.INVISIBLE);
            } else if(TextUtils.isEmpty(seat_Trip)) {
                editTextSeat.setError("Укажите количество мест");
                buttonCreatTrip.setVisibility(View.VISIBLE);
                progressBarPost.setVisibility(View.INVISIBLE);
            } else if(TextUtils.isEmpty(car_model)) {
                editTextCarModel.setError("Укажите марку вашего автомобиля");
                buttonCreatTrip.setVisibility(View.VISIBLE);
                progressBarPost.setVisibility(View.INVISIBLE);
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
                        buttonCreatTrip.setVisibility(View.VISIBLE);
                        progressBarPost.setVisibility(View.INVISIBLE);
                        showToast(getContext(), "Ошибка публикации поста. Повторите попытку через несколько минут");
                    }
                });
            }
        });
    }

    private void shwoDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    textViewDateTrip.setText(String.format("%s-%s-%d", dayMonthText(dayOfMonth), getMonthText(monthOfYear), year));
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