package hamsafar.tj.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import hamsafar.tj.R;

public class AuthActivity extends AppCompatActivity {

    private EditText userEmail, userName, userPass; // Поля ввод юзера
    private Button userRegisterBtn; // Кнопка регистрации
    private ProgressBar registerProgress; // Прогрессбар страница(фрагмент) регистрации
    private TextView singInUser; // Пользовательское соглашение

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // Full Screen Page


        userEmail = findViewById(R.id.user_Email);
        userName = findViewById(R.id.user_Name);
        userPass = findViewById(R.id.user_Pass);
        userRegisterBtn = findViewById(R.id.registerNextBtn);
        registerProgress = findViewById(R.id.progressBarAuth);
        singInUser = findViewById(R.id.singIN);


        singInUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(AuthActivity.this, R.style.BottomSheetDialogTheme);
                bottomSheetDialog.setContentView(R.layout.sing_in_sheet);
                bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                bottomSheetDialog.show();
            }
        });

        userRegisterBtn.setOnClickListener(new View.OnClickListener() {  // Нажатие на кнопки регистрации
            @Override
            public void onClick(View view) {
                registerProgress.setVisibility(View.VISIBLE);
                userRegisterBtn.setVisibility(View.INVISIBLE);
                String email = userEmail.getText().toString();
                String name = userName.getText().toString();
                String password = userPass.getText().toString();

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

                } else
                {
                    Intent registerIntent = new Intent(AuthActivity.this, RegisterActivity.class);
                    registerIntent.putExtra("userEmail", email);
                    registerIntent.putExtra("userName", name);
                    registerIntent.putExtra("userPass", password);
                    startActivity(registerIntent);
                }
            }
        });

    }
}