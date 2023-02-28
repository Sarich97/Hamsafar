package hamsafar.tj.activity;

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

    private EditText userName, userPhoneNum; // Поля ввод юзера
    private Button creatNewUserBtn; // Кнопка регистрации
    private ProgressBar progressBarRegister; // Прогрессбар страница(фрагмент) регистрации
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

        userName = findViewById(R.id.user_Name);
        userPhoneNum = findViewById(R.id.user_Phone);
        creatNewUserBtn = findViewById(R.id.registerUserBtn);
        progressBarRegister = findViewById(R.id.progressRegisterActivity);



        // Нажатие на кнопки регистрации
        creatNewUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creatNewUserBtn.setVisibility(View.INVISIBLE);
                progressBarRegister.setVisibility(View.VISIBLE);


                String email = getIntent().getExtras().getString("userEmail");
                String name = userName.getText().toString();
                String phone = userPhoneNum.getText().toString();
                String password = getIntent().getExtras().getString("userPass");

                if(name.length() < 3)
                {
                    userName.setError("Обязательное поле и не менее 3 символов");
                    progressBarRegister.setVisibility(View.INVISIBLE);
                    creatNewUserBtn.setVisibility(View.VISIBLE);

                } else if(phone.length() < 7) {
                    userPhoneNum.setError("Обязательное поле и не менее 7 символов");
                    progressBarRegister.setVisibility(View.INVISIBLE);
                    creatNewUserBtn.setVisibility(View.VISIBLE);
                } else
                {
                    progressBarRegister.setVisibility(View.INVISIBLE);
                    creatNewUserBtn.setVisibility(View.VISIBLE);
                    createNewUser(email, password,name, phone);
                }
            }
        });
    }

    private void createNewUser(String email, String password, String name, String phone) {
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
                user.put("ipAdress", ipAddress);
                user.put("regDate", FieldValue.serverTimestamp());

                documentReference.set(user).addOnCompleteListener(task1 -> {
                    if(task.isSuccessful()) {
                        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(mainIntent);
                        finish();
                    } else  {
                        progressBarRegister.setVisibility(View.INVISIBLE);
                        creatNewUserBtn.setVisibility(View.VISIBLE);
                        showToast(this, "Ошибка регистрации нового пользователя, повторите попытку позже");
                    }
                });
            } else  {
                progressBarRegister.setVisibility(View.INVISIBLE);
                creatNewUserBtn.setVisibility(View.VISIBLE);
                showToast(this, "Ошибка регистрации нового пользователя, повторите попытку позже");
            }
        });

    }
}