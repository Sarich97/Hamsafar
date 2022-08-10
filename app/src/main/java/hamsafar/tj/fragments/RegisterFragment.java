package hamsafar.tj.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import hamsafar.tj.R;
import hamsafar.tj.activity.RegisterActivity;

public class RegisterFragment extends Fragment {

    private EditText userEmail, userName, userPass; // Поля ввод юзера
    private Button userRegisterBtn; // Кнопка регистрации
    private ProgressBar registerProgress; // Прогрессбар страница(фрагмент) регистрации
    private TextView termsOfUseSheet; // Пользовательское соглашение

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        userEmail = view.findViewById(R.id.user_Email);
        userName = view.findViewById(R.id.user_Name);
        userPass = view.findViewById(R.id.user_Pass);
        userRegisterBtn = view.findViewById(R.id.registerNextBtn);
        registerProgress = view.findViewById(R.id.progressBarAuth);
        termsOfUseSheet = view.findViewById(R.id.terms_of_use);

        termsOfUseSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
                bottomSheetDialog.setContentView(R.layout.terms_of_use_sheet);
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

                if(TextUtils.isEmpty(email))
                {
                    userEmail.setError("Обязательное поле");
                    registerProgress.setVisibility(View.INVISIBLE);
                    userRegisterBtn.setVisibility(View.VISIBLE);
                }
                else if(TextUtils.isEmpty(name))
                {
                    userName.setError("Обязательное поле");
                    registerProgress.setVisibility(View.INVISIBLE);
                    userRegisterBtn.setVisibility(View.VISIBLE);

                } else if(TextUtils.isEmpty(password))
                {
                    userPass.setError("Обязательное поле");
                    registerProgress.setVisibility(View.INVISIBLE);
                    userRegisterBtn.setVisibility(View.VISIBLE);
                } else
                {
                    Intent registerIntent = new Intent(getContext(), RegisterActivity.class);
                    registerIntent.putExtra("userEmal", email);
                    registerIntent.putExtra("userName", name);
                    registerIntent.putExtra("userPass", password);
                    startActivity(registerIntent);
                }
            }
        });

        return view;
    }
}