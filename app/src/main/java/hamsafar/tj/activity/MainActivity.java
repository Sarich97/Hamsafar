package hamsafar.tj.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import hamsafar.tj.R;

public class MainActivity extends AppCompatActivity {
    private  Button button;
    private ImageView userProfileBtn;

    private String userID;


    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        button = findViewById(R.id.button);
        userProfileBtn = findViewById(R.id.user_ProgileBtn);

        firebaseAuth = FirebaseAuth.getInstance();  // User Table variable
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AuthActivity.class);
                startActivity(intent);
            }
        });

        userProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentUser !=null) {
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.BottomSheetDialogTheme);
                    bottomSheetDialog.setContentView(R.layout.user_profile_sheet);
                    bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    bottomSheetDialog.show();

                    TextView userName = bottomSheetDialog.findViewById(R.id.user_Name);
                    TextView carModel = bottomSheetDialog.findViewById(R.id.car_Model);
                    TextView userPhone = bottomSheetDialog.findViewById(R.id.user_Phone);
                    TextView userEmail = bottomSheetDialog.findViewById(R.id.user_Email);

                    userID = firebaseAuth.getCurrentUser().getUid();
                    firebaseFirestore.collection("users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()) {
                                if(task.getResult().exists()) {
                                    String user_name = task.getResult().getString("userName");
                                    String user_email = task.getResult().getString("userEmail");
                                    String user_phone = task.getResult().getString("userPhone");
                                    String car_model = task.getResult().getString("userCarModel");

                                    String firstName = user_name.substring(0, 1);

                                    userName.setText(user_name);
                                    userEmail.setText(user_email);
                                    userPhone.setText("+992" + user_phone);
                                    carModel.setText(car_model);
                                }
                            }
                        }
                    });

                } else {
                    Intent authIntent = new Intent(MainActivity.this, AuthActivity.class);
                    startActivity(authIntent);
                    finish();
                }
            }
        });
    }
}