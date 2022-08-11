package hamsafar.tj.activity;

import androidx.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import hamsafar.tj.R;

public class RegisterActivity extends AppCompatActivity {

    private ImageView regBackBtn;
    private String userName, userEmail, userPass;
    private EditText userPhone, userExperience, userCarModel;
    private ProgressBar regProgressBar;
    private Button creatAccBtn;
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

        userName = getIntent().getExtras().getString("userName");
        userEmail = getIntent().getExtras().getString("userEmail");
        userPass = getIntent().getExtras().getString("userPass");

        userPhone = findViewById(R.id.user_Phone);
        userExperience = findViewById(R.id.user_Experience);
        userCarModel = findViewById(R.id.user_CarModel);
        regProgressBar = findViewById(R.id.progressBarReg);
        creatAccBtn = findViewById(R.id.creatAccauntBtn);

        creatAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creatAccBtn.setVisibility(View.INVISIBLE);
                regProgressBar.setVisibility(View.VISIBLE);

                String phone = userPhone.getText().toString();
                String experience = userExperience.getText().toString();
                String carModel = userCarModel.getText().toString();

                if(TextUtils.isEmpty(phone)){
                    userPhone.setError("Обязательное поле");
                    creatAccBtn.setVisibility(View.VISIBLE);
                    regProgressBar.setVisibility(View.INVISIBLE);
                } else if(TextUtils.isEmpty(experience)) {
                    userExperience.setError("Обязательное поле");
                    creatAccBtn.setVisibility(View.VISIBLE);
                    regProgressBar.setVisibility(View.INVISIBLE);
                } else if(TextUtils.isEmpty(carModel)) {
                    userCarModel.setError("Обязательное поле");
                    creatAccBtn.setVisibility(View.VISIBLE);
                    regProgressBar.setVisibility(View.INVISIBLE);
                } else {
                    creatNewUser(userEmail, userPass, phone, experience, carModel);
                }
            }
        });

        regBackBtn = findViewById(R.id.registerBackBtn);
        regBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });


    }

    private void creatNewUser(String userEmail, String userPass, String phone, String experience, String carModel) {
        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                    String ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());

                    userID = firebaseAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
                    Map<String, Object> user = new HashMap<>();
                    user.put("userID", userID);
                    user.put("userEmail", userEmail);
                    user.put("userName", userName);
                    user.put("userPhone", phone);
                    user.put("userExperience", experience);
                    user.put("userCarModel", carModel);
                    user.put("ipAdress", ipAddress);
                    user.put("regDate", FieldValue.serverTimestamp());

                    documentReference.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(mainIntent);
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Ошибка регистрации, повторите попытку позже #98", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                } else  {
                    Toast.makeText(RegisterActivity.this, "Ошибка регистрации, повторите попытку позже #12", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}