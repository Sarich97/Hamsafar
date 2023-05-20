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

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
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

    private Dialog dialogInternetCon;


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
        userID = firebaseAuth.getCurrentUser().getUid();

        mediaPlayerSound = MediaPlayer.create(getActivity(), R.raw.sound);
        dialogInternetCon = new Dialog(getContext());


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
            firebaseFirestore.collection(POSTS_COLLECTION)
                .whereEqualTo("userUD", userID)
                .whereEqualTo("statusTrip", "show") // Фильтруем только активные поездки
                .get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        buttonCreatTripPas.setVisibility(View.VISIBLE);
                        progressBarCreat.setVisibility(View.INVISIBLE);
                        Log.e(TAG, "Error getting active posts count:", task.getException());
                        return;
                    }

                    int activePostsCount = task.getResult().size();
                    if (activePostsCount >= 3) {
                        buttonCreatTripPas.setVisibility(View.VISIBLE);
                        progressBarCreat.setVisibility(View.INVISIBLE);
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

                   String startTrip = spinnerStartTrip.getSelectedItem().toString();
                   String endTrip = spinnerEndTrip.getSelectedItem().toString();
                   String dateTrip = textViewDate.getText().toString();
                   String timeTrip = textViewTime.getText().toString();
                   String commentsTrip = editTextComment.getText().toString();

                    if (startTrip.equals("Откуда")) {
                        showSnakbarTypeOne(getView(),"Укажите начальную точку маршрута");
                        buttonCreatTripPas.setVisibility(View.VISIBLE);
                        progressBarCreat.setVisibility(View.INVISIBLE);
                    }  else if (endTrip.equals("Куда")) {
                        showSnakbarTypeOne(getView(),"Укажите конечную точку маршрута");
                        buttonCreatTripPas.setVisibility(View.VISIBLE);
                        progressBarCreat.setVisibility(View.INVISIBLE);
                    } else if (TextUtils.isEmpty(dateTrip)) {
                        textViewDate.setError("Укажите дату");
                        buttonCreatTripPas.setVisibility(View.VISIBLE);
                        progressBarCreat.setVisibility(View.INVISIBLE);
                    } else if (timeTrip.equals("Время")) {
                        textViewTime.setError("Укажите время");
                        buttonCreatTripPas.setVisibility(View.VISIBLE);
                        progressBarCreat.setVisibility(View.INVISIBLE);
                    } else {
                        createPost(startTrip, endTrip, dateTrip, timeTrip, commentsTrip);
                    }
                });

        });

        return view;
    }

    private void createPost(String startTrip, String endTrip, String dateTrip, String timeTrip, String commentsTrip) {
        firebaseFirestore.collection(USERS_COLLECTION).document(userID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Получаем имя и номер телефона пользователя
                String userName = task.getResult().getString("userName");
                String userPhone = task.getResult().getString("userPhone");
                String rating = task.getResult().get("userRating").toString();

                // Создаем новый документ поста с автоматически генерируемым ID
                DocumentReference documentReference = firebaseFirestore.collection(POSTS_COLLECTION).document();
                // Заполняем подробности поста в объект Map
                Map<String, Object> post = new HashMap<>();
                post.put("userUD", userID);
                post.put("userName", userName);
                post.put("userPhone", userPhone);
                post.put("carModel", null);
                post.put("startTrip", startTrip);
                post.put("endTrip", endTrip);
                post.put("dataTrip", dateTrip);
                post.put("timeTrip", timeTrip);
                post.put("priceTrip", null);
                post.put("seatTrip", "1");
                post.put("commentTrip", commentsTrip);
                post.put("isPackage", "no");
                post.put("rating",  Integer.parseInt(rating));
                post.put("isDriverUser", "Ищу водителя");
                post.put("statusTrip", "show");
                post.put("postId", documentReference.getId());
                post.put("timestamp", FieldValue.serverTimestamp());

                // Устанавливаем подробности поста в Firestore
                documentReference.set(post).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        // Если создание поста успешно, проигрываем звуковой сигнал и перенаправляем на главный экран
                        mediaPlayerSound.start();
                        Intent mainIntent = new Intent(getContext(), MainActivity.class);
                        startActivity(mainIntent);
                        getActivity().finish();
                    } else {
                        // Если создание поста не удалось, сообщаем об ошибке и сбрасываем прогресс-бар
                        textViewDate.setVisibility(View.VISIBLE);
                        progressBarCreat.setVisibility(View.INVISIBLE);
                        showSnakbarTypeOne(getView(), "Ошибка публикации поста. Повторите попытку через несколько минут");
                    }
                });
            }
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
                    textViewDate.setText(String.format("%s.%s.%s", dayMonthText(dayOfMonth), getMonthText(monthOfYear), year));

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