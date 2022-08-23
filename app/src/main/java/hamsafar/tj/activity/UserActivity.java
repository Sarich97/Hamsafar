package hamsafar.tj.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import hamsafar.tj.R;

public class UserActivity extends AppCompatActivity {

    private ImageView userProfileBackBtn, userImage; // User settings
    private TextView userDetalInfoBtn, userName;
    private String userID, user_name, user_email, user_phone, car_model;

    private FirebaseAuth firebaseAuth; // FireBase
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ColorGenerator colorGenerator = ColorGenerator.MATERIAL; // UserImage
            // FIREBASE SETTINGS
        firebaseAuth = FirebaseAuth.getInstance();  // User Table variable
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();


        userProfileBackBtn = findViewById(R.id.userProfleBackBtn);
        userDetalInfoBtn = findViewById(R.id.userDetalInfo);
        userName = findViewById(R.id.user_Name);
        userImage = findViewById(R.id.user_Image);

        userProfileBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        userID = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore.collection("users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    if(task.getResult().exists()) {
                        user_name = task.getResult().getString("userName");
                        user_email = task.getResult().getString("userEmail");
                        user_phone = task.getResult().getString("userPhone");
                        car_model = task.getResult().getString("userCarModel");
                        String firstName = user_name.substring(0, 1);

                        TextDrawable user_drawble = TextDrawable.builder()
                                .beginConfig()
                                .fontSize(40) /* size in px */
                                .bold()
                                .toUpperCase()
                                .endConfig()
                                .buildRoundRect(firstName, colorGenerator.getRandomColor(), 6); // radius in
                        userImage.setImageDrawable(user_drawble);
                        userName.setText(user_name);

                    }
                }
            }
        });


        userDetalInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(UserActivity.this, R.style.BottomSheetDialogTheme);
                bottomSheetDialog.setContentView(R.layout.user_profile_sheet);
                bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                bottomSheetDialog.show();

                TextView carModel = bottomSheetDialog.findViewById(R.id.user_CarModel);
                TextView userPhone = bottomSheetDialog.findViewById(R.id.user_Phone);
                TextView userEmail = bottomSheetDialog.findViewById(R.id.user_Email);

                userEmail.setText(user_email);
                userPhone.setText("+992" + user_phone);
                carModel.setText(car_model);
            }
        });


    }
}