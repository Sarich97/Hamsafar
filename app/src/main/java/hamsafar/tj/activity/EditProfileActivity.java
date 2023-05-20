package hamsafar.tj.activity;

import static hamsafar.tj.activity.utility.Utility.USERS_COLLECTION;
import static hamsafar.tj.activity.utility.Utility.showSnakbarTypeOne;
import static hamsafar.tj.activity.utility.Utility.showToast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import hamsafar.tj.R;

public class EditProfileActivity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth; // FireBase
    private FirebaseFirestore firebaseFirestore ;
    private String userID;

    private EditText editTextUserPhone, editTextUserCarModel;
    private TextView textViewUserName, textViewUserEmail;
    private Button buttonEditProfileBtn;
    private TextView textViewOnBackBtn;
    private ProgressBar progressBarEdit;
    private ImageView userImageProfile;
    private View viewSnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;


        viewSnackbar = findViewById(android.R.id.content);

        firebaseAuth = FirebaseAuth.getInstance();  // User Table variable
        firebaseFirestore = FirebaseFirestore.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        textViewUserName = findViewById(R.id.userNameProfile);
        textViewUserEmail = findViewById(R.id.userEmail);
        editTextUserPhone = findViewById(R.id.userPhoneEdit);
        editTextUserCarModel = findViewById(R.id.userCarModelD);
        textViewOnBackBtn = findViewById(R.id.toolbarTextBackBtn);
        progressBarEdit = findViewById(R.id.progressBar);
        userImageProfile = findViewById(R.id.userImageProfile);

        buttonEditProfileBtn = findViewById(R.id.editInfoButton);

        buttonEditProfileBtn.setOnClickListener(view -> {
            String user_phone = editTextUserPhone.getText().toString();
            String user_car = editTextUserCarModel.getText().toString();

            if (TextUtils.isEmpty(user_phone)) {
                editTextUserPhone.setError("Укажите номер телефона");
            } else if (TextUtils.isEmpty(user_car)) {
                editTextUserCarModel.setError("Укажите марку автомобиля");
            } else {
                progressBarEdit.setVisibility(View.VISIBLE);
                buttonEditProfileBtn.setVisibility(View.INVISIBLE);
                editUserProfile(user_phone, user_car);
            }
        });

        textViewOnBackBtn.setOnClickListener(view -> {
            onBackPressed();
            finish();
        });

        String user_name = getIntent().getStringExtra("userName");
        String user_email = getIntent().getStringExtra("userEmail");
        String user_car = getIntent().getStringExtra("userCar");
        String user_phone = getIntent().getStringExtra("userPhone");


        textViewUserName.setText(user_name);
        textViewUserEmail.setText(user_email);
        editTextUserPhone.setHint(user_phone);
        editTextUserCarModel.setHint(user_car);


        String userNameName = user_name.substring(0,1);

        TextDrawable user_drawble = TextDrawable.builder()
                .beginConfig()
                .fontSize(48) /* size in px */
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRoundRect(userNameName, colorGenerator.getRandomColor(),12); // radius in
        userImageProfile.setImageDrawable(user_drawble);
    }

    private void editUserProfile(String user_phone, String user_car) {
        DocumentReference documentReference = firebaseFirestore.collection(USERS_COLLECTION).document(userID);

        // Создаем объект Map с новыми данными пользователя
        Map<String, Object> user = new HashMap<>();
        user.put("userPhone", user_phone);
        user.put("userCarModel", user_car);

        // Обновляем документ в Firestore с новыми данными
        documentReference.update(user)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // При успешном обновлении данных переходим на главный экран
                    Intent mainIntent = new Intent(EditProfileActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                    showSnakbarTypeOne(viewSnackbar, "Вы успешно изменили данные");
                } else {
                    // В случае ошибки выводим сообщение об ошибке и возвращаемся к редактированию профиля
                    progressBarEdit.setVisibility(View.INVISIBLE);
                    buttonEditProfileBtn.setVisibility(View.VISIBLE);
                    showToast(this, "Ошибка при редактировании профиля");
                }
            });
    }
}