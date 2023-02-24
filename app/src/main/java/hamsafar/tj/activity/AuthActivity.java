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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import hamsafar.tj.R;

public class AuthActivity extends AppCompatActivity {

    private EditText userEmail, userName, userPass, userPhoneNum; // Поля ввод юзера
    private Button userRegisterBtn; // Кнопка регистрации
    private ProgressBar registerProgress; // Прогрессбар страница(фрагмент) регистрации
    private TextView singInUser; // Кнопка входа
    private String userID;


    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // Full Screen Page

        firebaseAuth = FirebaseAuth.getInstance();  // User Table variable
        firebaseFirestore = FirebaseFirestore.getInstance();  // DataBase variable

        userEmail = findViewById(R.id.user_Email);
        userName = findViewById(R.id.user_Name);
        userPass = findViewById(R.id.user_Pass);
        userPhoneNum = findViewById(R.id.user_Phone);
        userRegisterBtn = findViewById(R.id.registerNextBtn);
        registerProgress = findViewById(R.id.progressBarAuth);
        singInUser = findViewById(R.id.singInText);


        // КНОПКА ПОКАЗ ОКОШКИ ВХОДА
        singInUser.setOnClickListener(view -> {

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(AuthActivity.this, R.style.BottomSheetDialogTheme);
            bottomSheetDialog.setContentView(R.layout.sing_in_sheet);
            bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


            TextView userEmilSingIn = bottomSheetDialog.findViewById(R.id.userEmailSingIn);
            TextView userPassSingIn = bottomSheetDialog.findViewById(R.id.userPassSingIn);
            Button singInBtnLogin = bottomSheetDialog.findViewById(R.id.singInButton);
            bottomSheetDialog.show();

            singInBtnLogin.setOnClickListener(view1 -> {
                String email = userEmilSingIn.getText().toString();
                String pass = userPassSingIn.getText().toString();

                if(TextUtils.isEmpty(email)) {
                    userEmilSingIn.setError("Обязательное поле");
                } else  if(TextUtils.isEmpty(pass)) {
                    userPassSingIn.setError("Обязательное поле");
                } else  {
                    singInBtnLogin.setVisibility(View.VISIBLE);
                    loginUser(email, pass);
                }
            });
        });

        // Нажатие на кнопки регистрации
        userRegisterBtn.setOnClickListener(view -> {
            registerProgress.setVisibility(View.VISIBLE);
            userRegisterBtn.setVisibility(View.INVISIBLE);
            String email = userEmail.getText().toString();
            String name = userName.getText().toString();
            String password = userPass.getText().toString();
            String phone = userPhoneNum.getText().toString();

            if(email.length() < 5)
            {
                userEmail.setError("Обязательное поле и не менее 6 символов");
                registerProgress.setVisibility(View.INVISIBLE);
                userRegisterBtn.setVisibility(View.VISIBLE);
            }
            else if(name.length() < 3)
            {
                userName.setError("Обязательное поле и не менее 3 символов");
                registerProgress.setVisibility(View.INVISIBLE);
                userRegisterBtn.setVisibility(View.VISIBLE);

            } else if(password.length() < 6)
            {
                userPass.setError("Обязательное поле и не менее 6 символов");
                registerProgress.setVisibility(View.INVISIBLE);
                userRegisterBtn.setVisibility(View.VISIBLE);

            } else if(phone.length() < 7) {
                userPhoneNum.setError("Обязательное поле и не менее 7 символов");
                registerProgress.setVisibility(View.INVISIBLE);
                userRegisterBtn.setVisibility(View.VISIBLE);
            } else
            {
                createNewUser(email, name, phone, password);
            }
        });

    }

    private void createNewUser(String email, String name, String phone, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
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
                user.put("ipAdress", ipAddress);
                user.put("regDate", FieldValue.serverTimestamp());

                documentReference.set(user).addOnCompleteListener(task1 -> {
                    if(task.isSuccessful()) {
                        Intent mainIntent = new Intent(AuthActivity.this, NextActivity.class);
                        startActivity(mainIntent);
                        finish();
                    } else  {
                        showToast(this, "Ошибка регистрации нового пользователя, повторите попытку позже");
                    }
                });
            } else  {
                showToast(this, "Ошибка регистрации нового пользователя, повторите попытку позже");
            }
        });

    }

    private void loginUser(String email, String pass) {
        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Intent mainIntent = new Intent(AuthActivity.this, NextActivity.class);
                startActivity(mainIntent);
                finish();
            } else {
                showToast(this, "Ошибка входа в аккаунт, повторите попытку позже");
            }
        });
    }
}