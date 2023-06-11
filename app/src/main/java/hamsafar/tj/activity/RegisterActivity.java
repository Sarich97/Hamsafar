package hamsafar.tj.activity;


import static hamsafar.tj.R.string.signing_in;
import static hamsafar.tj.activity.utility.Utility.USERS_COLLECTION;
import static hamsafar.tj.activity.utility.Utility.isOnline;
import static hamsafar.tj.activity.utility.Utility.showSnakbarTypeOne;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

    private EditText editTextUserName, editTextUserCarModel, editTextRefCode;
    private Button buttonCreateNewUser;
    private Spinner spinnerUserCity;
    private ProgressDialog progressDialog;
    private TextView textViewTeamOfServis;
    private View viewSnackbar;
    private Dialog dialogInternetCon;
    private String userID;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore,userRefCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userRefCode = FirebaseFirestore.getInstance();


        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            userID = currentUser.getUid();
            // Do something with the uid
        } else {
            // User is not logged in
        }

        dialogInternetCon = new Dialog(this);

        initViews();

        buttonCreateNewUser.setOnClickListener(view -> {
            if(isOnline(this)) {
                String email = getIntent().getStringExtra("userPhone");
                String name = editTextUserName.getText().toString();
                String phone = getIntent().getStringExtra("userPhone");
                String userCity = spinnerUserCity.getSelectedItem().toString();
                String password = getIntent().getStringExtra("userPass");
                String userCarModel = editTextUserCarModel.getText().toString();
                String user_ref_code = editTextRefCode.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    editTextUserName.setError(getString(R.string.field_nameRegister));
                } else if (userCity.equals(getString(R.string.spinner_CityMessage))) {
                    showSnakbarTypeOne(viewSnackbar, getString(R.string.spinner_CityMessage));
                } else {
                    createFirebaseAuthUser(email, password, name, phone, userCity, userCarModel, user_ref_code);
                }
            } else  {
                dialogInternetCon.setContentView(R.layout.internet_connecting_dialog);
                dialogInternetCon.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogInternetCon.show();
            }
        });

        textViewTeamOfServis.setOnClickListener(view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(RegisterActivity.this, R.style.BottomSheetDialogTheme);
            bottomSheetDialog.setContentView(R.layout.terms_of_use_sheet);
            bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            bottomSheetDialog.show();
        });
    }

    // Инициализация полей класса
    private void initViews() {
        editTextUserName = findViewById(R.id.user_Name);
        spinnerUserCity = findViewById(R.id.spinnerGetCityUser);
        buttonCreateNewUser = findViewById(R.id.registerUserBtn);
        textViewTeamOfServis = findViewById(R.id.team_of_servis);
        editTextUserCarModel = findViewById(R.id.userCarModelD);
        viewSnackbar = findViewById(android.R.id.content);
        editTextRefCode = findViewById(R.id.userCode);
    }

    // Создание нового пользователя в Firebase Authentication
    private void createFirebaseAuthUser(String email, String password, String name, String phone, String userCity, String userCarModel, String user_ref_code) {
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Регистрация нового пользователя");
        progressDialog.show();
        buttonCreateNewUser.setVisibility(View.INVISIBLE);

        firebaseAuth.createUserWithEmailAndPassword(email + "@gmail.com", password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String userID = firebaseAuth.getCurrentUser().getUid();
                saveUserDataToFirestore(userID, email, password, name, phone, userCity, userCarModel, user_ref_code);
            } else {
                progressDialog.dismiss();
                buttonCreateNewUser.setVisibility(View.VISIBLE);
                showSnakbarTypeOne(viewSnackbar, getString(R.string.erro_registerMessage));
            }
        });
    }

    // Сохранение данных нового пользователя в Firestore
    private void saveUserDataToFirestore(String userID, String email, String password, String name, String phone, String userCity, String userCarModel, String user_ref_code) {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        String ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());

        DocumentReference documentReference = firebaseFirestore.collection(USERS_COLLECTION).document(userID);
        Map<String, Object> user = new HashMap<>();
        user.put("userKey", userID);
        user.put("userPass", password);
        user.put("userEmail", email);
        user.put("userName", name);
        user.put("userPhone", phone);
        user.put("userCity", userCity);
        user.put("userCarModel", userCarModel);
        user.put("userRating", 15);
        user.put("userTrip", 0);
        user.put("ipAddress", ipAddress);
        user.put("regDate", FieldValue.serverTimestamp());

       if(user_ref_code.isEmpty()) {

       } else {
           DocumentReference documentReference1 = userRefCode.collection("refcode/" + user_ref_code + "/refs"). document(userID);

           Map<String, Object> reCode = new HashMap<>();
           reCode.put("userKey", userID);
           reCode.put("user_ref_code", userID);
           reCode.put("ipAddress", ipAddress);
           reCode.put("userName", name);

           documentReference1.set(reCode).addOnCompleteListener(task2 -> {
               if(task2.isSuccessful()) {
                   showSnakbarTypeOne(viewSnackbar, "Реферальный код добавлен");
               } else {
                   showSnakbarTypeOne(viewSnackbar, "Ошибка реферального кода");
               }
           });
       }

        documentReference.set(user).addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {

                Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
                progressDialog.dismiss();
                buttonCreateNewUser.setVisibility(View.VISIBLE);
            } else {
                progressDialog.dismiss();
                buttonCreateNewUser.setVisibility(View.VISIBLE);
                showSnakbarTypeOne(viewSnackbar, getString(R.string.erro_registerMessageS));
            }
        });
    }

    // Показать snackbar
}