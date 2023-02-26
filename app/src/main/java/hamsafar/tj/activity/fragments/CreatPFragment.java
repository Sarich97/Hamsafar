package hamsafar.tj.activity.fragments;

import static hamsafar.tj.activity.utility.Utility.showToast;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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

    private Spinner startTripSpinnerP, endTripSpinnerP;
    private TextView dateTripP, timeTripP;
    private EditText commentTripP;
    private Button creatTripBtnP;
    private ProgressBar progressBarCreatPostP;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser currentUser;
    private String userID;

    private int mYear, mMonth, mDay, mHour, mMinute;
    final Calendar cal = Calendar.getInstance();


    private TimePickerDialog timePickerDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view =  inflater.inflate(R.layout.fragment_creat_p, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance(); // DateBase settings
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();


        startTripSpinnerP = view.findViewById(R.id.spinnerStartTripP);
        endTripSpinnerP = view.findViewById(R.id.spinnerEndTripP);
        dateTripP = view.findViewById(R.id.editTextDateP);
        timeTripP = view.findViewById(R.id.editTextTimeP);
        creatTripBtnP = view.findViewById(R.id.creatTripP);
        commentTripP = view.findViewById(R.id.editTextCommentP);
        progressBarCreatPostP = view.findViewById(R.id.progressBarPostP);

        dateTripP.setOnClickListener(view1 -> shwoDatePickerDialog());

        timeTripP.setOnClickListener(view12 -> showTimePickerDialog());


        creatTripBtnP.setOnClickListener(clickCreatBtn -> {
            creatTripBtnP.setVisibility(View.INVISIBLE);
            progressBarCreatPostP.setVisibility(View.VISIBLE);

            String start_Trip = startTripSpinnerP.getSelectedItem().toString();
            String end_Trip = endTripSpinnerP.getSelectedItem().toString();
            String data_Trip = dateTripP.getText().toString();
            String time_Trip = timeTripP.getText().toString();
            String commets_Trip = commentTripP.getText().toString();

            if(start_Trip.equals("Откуда")) {
                showToast(getContext(),"Укажите начальную точку маршрута");
                creatTripBtnP.setVisibility(View.VISIBLE);
                progressBarCreatPostP.setVisibility(View.INVISIBLE);
            }  else if(end_Trip.equals("Куда")) {
                showToast(getContext(), "Укажите конечную точку маршрута");
                creatTripBtnP.setVisibility(View.VISIBLE);
                progressBarCreatPostP.setVisibility(View.INVISIBLE);
            } else if(TextUtils.isEmpty(data_Trip)) {
                dateTripP.setError("Укажите дату");
                creatTripBtnP.setVisibility(View.VISIBLE);
                progressBarCreatPostP.setVisibility(View.INVISIBLE);
            } else if(TextUtils.isEmpty(time_Trip)) {
                timeTripP.setError("Укажите время");
                creatTripBtnP.setVisibility(View.VISIBLE);
                progressBarCreatPostP.setVisibility(View.INVISIBLE);
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
                post.put("postId", documentReference.getId());
                post.put("timestamp", FieldValue.serverTimestamp());

                documentReference.set(post).addOnCompleteListener(task1 -> {
                    if(task1.isSuccessful()) {
                        Intent mainIntent = new Intent(getContext(), MainActivity.class);
                        startActivity(mainIntent);
                        getActivity().finish();
                    } else {
                        dateTripP.setVisibility(View.VISIBLE);
                        progressBarCreatPostP.setVisibility(View.INVISIBLE);
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

                    dateTripP.setText(dayOfMonth + "." + (monthName) + "." + year);

                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        boolean is24HourView= true;
        timePickerDialog = new TimePickerDialog(getContext(), (timePicker, i, i1) -> timeTripP.setText(i + ":" + i1),mHour,mMinute,is24HourView);
        timePickerDialog.show();
    }
}