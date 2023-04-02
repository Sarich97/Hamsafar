package hamsafar.tj.activity;

import static hamsafar.tj.R.string.erro_registerMessage;
import static hamsafar.tj.R.string.erro_registerMessageS;
import static hamsafar.tj.R.string.field_nameRegister;
import static hamsafar.tj.R.string.field_phoneRegister;
import static hamsafar.tj.R.string.spinner_CityMessage;
import static hamsafar.tj.activity.utility.Utility.showToast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import hamsafar.tj.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextUserName, editTextUserPhone, editTextUserCarModel; // Поля ввод юзера
    private Button buttonCreatNewUser; // Кнопка регистрации
    private Spinner spinnerUserCity;
    private ProgressBar progressRegister; // Прогрессбар страница(фрагмент) регистрации
    private TextView textViewTeamOfServis;
    private String userID;


    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // Full Screen Page

        firebaseAuth = FirebaseAuth.getInstance();  // User Table variable
        firebaseFirestore = FirebaseFirestore.getInstance();  // DataBase variable

        editTextUserName = findViewById(R.id.user_Name);
        editTextUserPhone = findViewById(R.id.user_Phone);
        spinnerUserCity = findViewById(R.id.spinnerGetCityUser);
        buttonCreatNewUser = findViewById(R.id.registerUserBtn);
        progressRegister = findViewById(R.id.progressRegisterActivity);
        textViewTeamOfServis = findViewById(R.id.team_of_servis);
        editTextUserCarModel = findViewById(R.id.userCarModelD);



        // Нажатие на кнопки регистрации
        buttonCreatNewUser.setOnClickListener(view -> {

            String email = getIntent().getExtras().getString("userEmail");
            String name = editTextUserName.getText().toString();
            String phone = editTextUserPhone.getText().toString();
            String user_city = spinnerUserCity.getSelectedItem().toString();
            String password = getIntent().getExtras().getString("userPass");
            String user_car_model = editTextUserCarModel.getText().toString();

            if(name.length() < 3)
            {
                editTextUserName.setError(getString(field_nameRegister));


            } else if(phone.length() < 7) {
                editTextUserPhone.setError(getString(field_phoneRegister));
            } else if(user_city.equals(getString(spinner_CityMessage))) {
                showToast(this,getString(spinner_CityMessage));
            } else
            {
                createNewUser(email, password,name, phone, user_city, user_car_model);
            }
        });

        textViewTeamOfServis.setOnClickListener(view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(RegisterActivity.this, R.style.BottomSheetDialogTheme);
            bottomSheetDialog.setContentView(R.layout.terms_of_use_sheet);
            bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            bottomSheetDialog.show();
        });
    }

    private void createNewUser(String email, String password, String name, String phone, String user_city, String user_car_model) {
        progressRegister.setVisibility(View.VISIBLE);
        buttonCreatNewUser.setVisibility(View.INVISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                String ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());

                userID = firebaseAuth.getCurrentUser().getUid();
                DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
                Map<String, Object> user = new HashMap<>();
                user.put("userKey", userID);
                user.put("userPass", password);
                user.put("userEmail", email);
                user.put("userName", name);
                user.put("userPhone", phone);
                user.put("userCity", user_city);
                user.put("userCarModel", user_car_model);
                user.put("userRating", 15);
                user.put("userTrip", 0);
                user.put("ipAdress", ipAddress);
                user.put("regDate", FieldValue.serverTimestamp());

                documentReference.set(user).addOnCompleteListener(task1 -> {
                    if(task.isSuccessful()) {
                        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(mainIntent);
                        finish();
                        progressRegister.setVisibility(View.INVISIBLE);
                        buttonCreatNewUser.setVisibility(View.VISIBLE);
                    } else  {
                        progressRegister.setVisibility(View.INVISIBLE);
                        buttonCreatNewUser.setVisibility(View.VISIBLE);
                        showToast(this, getString(erro_registerMessage));
                    }
                });
            } else  {
                progressRegister.setVisibility(View.INVISIBLE);
                buttonCreatNewUser.setVisibility(View.VISIBLE);
                showToast(this, getString(erro_registerMessageS));
            }
        });

    }
}