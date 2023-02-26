package hamsafar.tj.activity;

import static hamsafar.tj.activity.utility.Utility.showToast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import hamsafar.tj.R;

public class TripDetalActivity extends AppCompatActivity {

    private TextView tripNameD, tripStatusD, tripPriceD, tripStartD,tripEndD, tripDateD, tripCarModelD, tripSeatD;
    private ImageView userImage;
    private Button bookTripBtn;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore UsersRef,bookRef;
    private String post_id;
    private String userKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detal);

        tripNameD = findViewById(R.id.driverName);
        tripStatusD = findViewById(R.id.statusTravel);
        tripPriceD = findViewById(R.id.trip_Price);
        tripStartD = findViewById(R.id.start_of_route);
        tripEndD = findViewById(R.id.end_of_route);
        tripDateD = findViewById(R.id.dateTimeTrip);
        tripCarModelD = findViewById(R.id.carModel);
        tripSeatD = findViewById(R.id.seatTrip);
        bookTripBtn = findViewById(R.id.bookButton);

        firebaseAuth = FirebaseAuth.getInstance();
        UsersRef = FirebaseFirestore.getInstance();
        bookRef = FirebaseFirestore.getInstance();
        userKey = firebaseAuth.getCurrentUser().getUid();


        String tripStart = getIntent().getExtras().getString("locationFrom");
        String tripEnd = getIntent().getExtras().getString("locationTo");
        String tripDate = getIntent().getExtras().getString("date");
        String tripPrice = getIntent().getExtras().getString("price");
        String tripSeat = getIntent().getExtras().getString("seatTrip");
        String tripBrandCar = getIntent().getExtras().getString("brandCar");
        String tripNameUser = getIntent().getExtras().getString("driverName");
        String tripUserId = getIntent().getExtras().getString("driverID");
        String tripUserPhone = getIntent().getExtras().getString("phone");
        String isUserDriver = getIntent().getExtras().getString("isUserDriver");
        String postID = getIntent().getExtras().getString("postID");


        tripNameD.setText(tripNameUser);
        tripStatusD.setText(isUserDriver);
        tripPriceD.setText(tripPrice + " cомони");
        tripStartD.setText(tripStart);
        tripEndD.setText(tripEnd);
        tripDateD.setText(tripDate);
        tripCarModelD.setText("Марка автомобиля\n" + tripBrandCar);
        tripSeatD.setText("Кол-во мест\n" + tripSeat);


        bookTripBtn.setOnClickListener(view -> {
            UsersRef.collection("users").document(userKey).get().addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    String user_name = task.getResult().getString("userName");
                    String user_phone = task.getResult().getString("userPhone");
                    bookTrip(user_name, user_phone, postID);
                }
            });

        });

    }

    private void bookTrip(String user_name, String user_phone, String postID) {
        Map<String, Object> book = new HashMap<>();
        book.put("userID", userKey);
        book.put("userName", user_name);
        book.put("userPhone", user_phone);
        book.put("postID", postID);
        book.put("timestamp", FieldValue.serverTimestamp());

        bookRef.collection("posts/" + postID + "/books").add(book).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                showToast(this,"Вы успешно забронировали поездку");
            } else {
                showToast(this,"Ошибка бронирования . Повторите попытку через несколько минут");
            }
        });

    }
}