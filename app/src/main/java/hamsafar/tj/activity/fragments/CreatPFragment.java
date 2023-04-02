package hamsafar.tj.activity.fragments;

import static hamsafar.tj.activity.utility.Utility.dayMonthText;
import static hamsafar.tj.activity.utility.Utility.getMonthText;
import static hamsafar.tj.activity.utility.Utility.minuteText;
import static hamsafar.tj.activity.utility.Utility.showToast;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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


public class CreatPFragment extends Fragment {

    private Spinner spinnerStartTrip, spinnerEndTrip;
    private TextView textViewDate, textViewTime;
    private EditText editTextComment;
    private Button buttonCreatTripPas;
    private ProgressBar progressBarCreat;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser currentUser;
    private String userID;

    private int mYear, mMonth, mDay, mHour, mMinute;
    final Calendar cal = Calendar.getInstance();


    private TimePickerDialog timePickerDialog;

    private MediaPlayer mediaPlayerSound;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view =  inflater.inflate(R.layout.fragment_creat_p, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance(); // DateBase settings
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        mediaPlayerSound = MediaPlayer.create(getActivity(), R.raw.sound);


        spinnerStartTrip = view.findViewById(R.id.spinnerStartTripP);
        spinnerEndTrip = view.findViewById(R.id.spinnerEndTripP);
        textViewDate = view.findViewById(R.id.editTextDateP);
        textViewTime = view.findViewById(R.id.editTextTimeP);
        buttonCreatTripPas = view.findViewById(R.id.creatTripP);
        editTextComment = view.findViewById(R.id.editTextCommentP);
        progressBarCreat = view.findViewById(R.id.progressBarPostP);

        textViewDate.setOnClickListener(view1 -> shwoDatePickerDialog());

        textViewTime.setOnClickListener(view12 -> showTimePickerDialog());


        buttonCreatTripPas.setOnClickListener(clickCreatBtn -> {
            buttonCreatTripPas.setVisibility(View.INVISIBLE);
            progressBarCreat.setVisibility(View.VISIBLE);

            String start_Trip = spinnerStartTrip.getSelectedItem().toString();
            String end_Trip = spinnerEndTrip.getSelectedItem().toString();
            String data_Trip = textViewDate.getText().toString();
            String time_Trip = textViewTime.getText().toString();
            String commets_Trip = editTextComment.getText().toString();

            if(start_Trip.equals("Откуда")) {
                showToast(getContext(),"Укажите начальную точку маршрута");
                buttonCreatTripPas.setVisibility(View.VISIBLE);
                progressBarCreat.setVisibility(View.INVISIBLE);
            }  else if(end_Trip.equals("Куда")) {
                showToast(getContext(), "Укажите конечную точку маршрута");
                buttonCreatTripPas.setVisibility(View.VISIBLE);
                progressBarCreat.setVisibility(View.INVISIBLE);
            } else if(TextUtils.isEmpty(data_Trip)) {
                textViewDate.setError("Укажите дату");
                buttonCreatTripPas.setVisibility(View.VISIBLE);
                progressBarCreat.setVisibility(View.INVISIBLE);
            } else if(TextUtils.isEmpty(time_Trip)) {
                textViewTime.setError("Укажите время");
                buttonCreatTripPas.setVisibility(View.VISIBLE);
                progressBarCreat.setVisibility(View.INVISIBLE);
            } else {
                creatPost(start_Trip, end_Trip, data_Trip, time_Trip, commets_Trip);
            }

        });

        return view;
    }

    private void creatPost(String start_trip, String end_trip, String data_trip, String time_trip, String commets_Trip) {
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
                post.put("carModel", null);
                post.put("startTrip", start_trip);
                post.put("endTrip", end_trip);
                post.put("dataTrip", data_trip);
                post.put("timeTrip", time_trip);
                post.put("priceTrip", null);
                post.put("seatTrip", "1");
                post.put("commentTrip", commets_Trip);
                post.put("isDriverUser", "Ищу водителя");
                post.put("statusTrip", "show");
                post.put("postId", documentReference.getId());
                post.put("timestamp", FieldValue.serverTimestamp());

                documentReference.set(post).addOnCompleteListener(task1 -> {
                    if(task1.isSuccessful()) {
                        mediaPlayerSound.start();
                        Intent mainIntent = new Intent(getContext(), MainActivity.class);
                        startActivity(mainIntent);
                        getActivity().finish();
                    } else {
                        textViewDate.setVisibility(View.VISIBLE);
                        progressBarCreat.setVisibility(View.INVISIBLE);
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
                    textViewDate.setText(String.format("%s.%s", dayMonthText(dayOfMonth), getMonthText(monthOfYear)));

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
            textViewTime.setText(String.format("%s:%s", dayMonthText(i), minuteText(i1)));
        },mHour,mMinute,is24HourView);
        timePickerDialog.show();
    }
}