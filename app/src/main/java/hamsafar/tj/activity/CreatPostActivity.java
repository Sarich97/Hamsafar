package hamsafar.tj.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import hamsafar.tj.R;

public class CreatPostActivity extends AppCompatActivity {

    private ImageView backBtn;
    private Spinner startTripSpinner, endTripSpinner;
    private EditText dateTrip, timeTrip, priceTrip, seatTrip;
    private Button creatTripBtn;
    private ProgressBar progressBarCreatPost;


    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser currentUser;
    private String userID;

    @Override
    protected void onStart() {
        super.onStart();
        if(currentUser !=null) {

        } else  {
            Intent authIntent = new Intent(CreatPostActivity.this, AuthActivity.class);
            startActivity(authIntent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_post);

        firebaseFirestore = FirebaseFirestore.getInstance(); // DateBase settings
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();



        backBtn = findViewById(R.id.creatPostBackBtn);
        startTripSpinner = findViewById(R.id.spinnerStartTrip);
        endTripSpinner = findViewById(R.id.spinnerEndTrip);
        dateTrip = findViewById(R.id.editTextDate);
        timeTrip = findViewById(R.id.editTextTime);
        priceTrip = findViewById(R.id.editTextPrice);
        seatTrip = findViewById(R.id.editTextSeat);
        creatTripBtn = findViewById(R.id.creatTrip);
        progressBarCreatPost = findViewById(R.id.progressBarPost);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        creatTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creatTripBtn.setVisibility(View.INVISIBLE);
                progressBarCreatPost.setVisibility(View.VISIBLE);

                String start_Trip = startTripSpinner.getSelectedItem().toString();
                String end_Trip = endTripSpinner.getSelectedItem().toString();
                String data_Trip = dateTrip.getText().toString();
                String time_Trip = timeTrip.getText().toString();
                String price_Trip = priceTrip.getText().toString();
                String seat_Trip = seatTrip.getText().toString();

                if(TextUtils.isEmpty(price_Trip)) {
                    priceTrip.setError("Укажите цену");
                    creatTripBtn.setVisibility(View.VISIBLE);
                    progressBarCreatPost.setVisibility(View.INVISIBLE);
                } else if(TextUtils.isEmpty(seat_Trip)) {
                    seatTrip.setError("Укажите количество мест");
                    creatTripBtn.setVisibility(View.VISIBLE);
                    progressBarCreatPost.setVisibility(View.INVISIBLE);
                } else  {
                    creatPost(start_Trip, end_Trip, data_Trip, time_Trip, price_Trip, seat_Trip);
                }
            }
        });

    }

    private void creatPost(String start_trip, String end_trip, String data_trip, String time_trip, String price_trip, String seat_trip) {
        userID = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore.collection("users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    String user_name = task.getResult().getString("userName");
                    String user_phone = task.getResult().getString("userPhone");
                    String user_carModel = task.getResult().getString("userCarModel");

                    DocumentReference documentReference = firebaseFirestore.collection("posts").document();
                    Map<String, Object> post = new HashMap<>();
                    post.put("userUD", userID);
                    post.put("userName", user_name);
                    post.put("userPhone", user_phone);
                    post.put("carModel", user_carModel);
                    post.put("srartTrip", start_trip);
                    post.put("endTrip", end_trip);
                    post.put("dataTrip", data_trip);
                    post.put("timeTrip", time_trip);
                    post.put("priceTrip", price_trip);
                    post.put("seatTrip", seat_trip);
                    post.put("postId", documentReference.getId());
                    post.put("timestamp", FieldValue.serverTimestamp());

                    documentReference.set(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Intent mainIntent = new Intent(CreatPostActivity.this, MainActivity.class);
                                startActivity(mainIntent);
                                finish();
                            } else {
                                creatTripBtn.setVisibility(View.VISIBLE);
                                progressBarCreatPost.setVisibility(View.INVISIBLE);
                                Toast.makeText(CreatPostActivity.this, "Ошибка публикации поста. Повторите попытку через несколько минут", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
}