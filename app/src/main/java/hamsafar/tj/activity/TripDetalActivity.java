package hamsafar.tj.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import hamsafar.tj.R;

public class TripDetalActivity extends AppCompatActivity {

    TextView tripNameD, tripStatusD, tripPriceD, tripStartD,tripEndD, tripDateD, tripCarModelD, tripSeatD;
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

    }
}