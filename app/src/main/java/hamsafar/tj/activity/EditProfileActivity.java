package hamsafar.tj.activity;

import static hamsafar.tj.R.string.erro_registerMessage;
import static hamsafar.tj.activity.utility.Utility.showToast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import hamsafar.tj.R;

public class EditProfileActivity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth; // FireBase
    private FirebaseFirestore firebaseFirestore ;
    private String userID;

    private EditText editTextUserName, editTextUserEmail, editTextUserPhone;
    private Button buttonEditProfileBtn;
    private TextView textViewOnBackBtn;
    private ProgressBar progressBarEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        firebaseAuth = FirebaseAuth.getInstance();  // User Table variable
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        userID = firebaseAuth.getCurrentUser().getUid();

        editTextUserName = findViewById(R.id.userNameEdit);
        editTextUserEmail = findViewById(R.id.userEmaiEdit);
        editTextUserPhone = findViewById(R.id.userPhoneEdit);
        textViewOnBackBtn = findViewById(R.id.toolbarTextBackBtn);
        progressBarEdit = findViewById(R.id.progressBar);

        buttonEditProfileBtn = findViewById(R.id.editInfoButton);

        buttonEditProfileBtn.setOnClickListener(view -> {
            String user_phone = editTextUserPhone.getText().toString();

            if(user_phone.length() < 13) {
                editTextUserPhone.setError("Укажите номер телефона в международном формате");
                showToast(this, "Укажите номер телефона в международном формате");
            } else  {
                progressBarEdit.setVisibility(View.VISIBLE);
                buttonEditProfileBtn.setVisibility(View.INVISIBLE);
                editUserProofile(user_phone);
            }
        });


        textViewOnBackBtn.setOnClickListener(view -> {
            onBackPressed();
            finish();
        });

        firebaseFirestore.collection("users").document(userID).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                String user_name = task.getResult().getString("userName");
                String user_email = task.getResult().getString("userEmail");
                String user_phone = task.getResult().getString("userPhone");

                editTextUserName.setHint(user_name);
                editTextUserEmail.setHint(user_email);
                editTextUserPhone.setHint(user_phone);

            } else  {

            }
        });
    }

    private void editUserProofile(String user_phone) {
        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.update("userPhone", user_phone).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Intent mainIntent = new Intent(EditProfileActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
                showToast(this, "Вы успешно сменили номер телефона");
            } else {
                progressBarEdit.setVisibility(View.INVISIBLE);
                buttonEditProfileBtn.setVisibility(View.VISIBLE);
                showToast(this, "Ошибка при редактировании профиля");
            }
        });
    }
}