package hamsafar.tj.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import hamsafar.tj.R;

public class EditProfileActivity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth; // FireBase
    private FirebaseFirestore firebaseFirestore,travelPostRef ;
    private String userID;

    private EditText editTextUserName, editTextUserEmail, editTextUserPhone;
    private Button buttonEditProfileBtn;
    private TextView textViewOnBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        editTextUserName = findViewById(R.id.userEmaiEdit);
        textViewOnBackBtn = findViewById(R.id.toolbarTextBackBtn);


        textViewOnBackBtn.setOnClickListener(view -> {
            onBackPressed();
            finish();
        });
    }
}