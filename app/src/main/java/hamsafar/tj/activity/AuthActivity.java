package hamsafar.tj.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import hamsafar.tj.R;

public class AuthActivity extends AppCompatActivity {

    private EditText userEmail, userName, userPass; // Поля ввод юзера
    private Button userRegisterBtn; // Кнопка регистрации
    private ProgressBar registerProgress; // Прогрессбар страница(фрагмент) регистрации
    private TextView singInUser; // Пользовательское соглашение


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
        userRegisterBtn = findViewById(R.id.registerNextBtn);
        registerProgress = findViewById(R.id.progressBarAuth);
        singInUser = findViewById(R.id.singInText);


        singInUser.setOnClickListener(new View.OnClickListener() { // КНОПКА ПОКАЗ ОКОШКИ ВХОДА
            @Override
            public void onClick(View view) {

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(AuthActivity.this, R.style.BottomSheetDialogTheme);
                bottomSheetDialog.setContentView(R.layout.sing_in_sheet);
                bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


                TextView userEmilSingIn = bottomSheetDialog.findViewById(R.id.userEmailSingIn);
                TextView userPassSingIn = bottomSheetDialog.findViewById(R.id.userPassSingIn);
                Button singInBtnLogin = bottomSheetDialog.findViewById(R.id.singInButton);
                ProgressBar singInProgressBar = bottomSheetDialog.findViewById(R.id.progressBarLogin);
                bottomSheetDialog.show();

                singInBtnLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = userEmilSingIn.getText().toString();
                        String pass = userPassSingIn.getText().toString();
                        singInBtnLogin.findViewById(R.id.singInButton).setVisibility(View.INVISIBLE);
                        singInProgressBar.setVisibility(View.VISIBLE);

                        if(TextUtils.isEmpty(email)) {
                            userEmilSingIn.setError("Обязательное поле");
                            singInBtnLogin.setVisibility(View.VISIBLE);
                            singInProgressBar.setVisibility(View.INVISIBLE);
                        } else  if(TextUtils.isEmpty(pass)) {
                            userPassSingIn.setError("Обязательное поле");
                            singInBtnLogin.setVisibility(View.VISIBLE);
                            singInProgressBar.setVisibility(View.INVISIBLE);
                        } else  {
                            singInBtnLogin.setVisibility(View.VISIBLE);
                            singInProgressBar.setVisibility(View.INVISIBLE);
                            loginUser(email, pass);
                        }
                    }
                });
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

    private void loginUser(String email, String pass) {
        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Intent mainIntent = new Intent(AuthActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                } else {
                    Toast.makeText(AuthActivity.this, "Ошибка входа в аккаунт, повторите попытку позже #23", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}