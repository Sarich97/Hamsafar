package hamsafar.tj.activity.fragments;



import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import hamsafar.tj.R;
import hamsafar.tj.activity.RegisterActivity;


public class RegisterFragment extends Fragment {

    private EditText editTextEmail, editTextPass;
    private Button buttonRegister;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        initViews(view);
        setListeners();

        return view;
    }

    // Инициализация полей класса
    private void initViews(View view) {
        editTextEmail = view.findViewById(R.id.user_Email);
        editTextPass = view.findViewById(R.id.user_Pass);
        buttonRegister = view.findViewById(R.id.registerNextBtn);
    }

    // Назначение обработчика кликов на кнопку регистрации
    private void setListeners() {
        buttonRegister.setOnClickListener(click -> {
            String email = editTextEmail.getText().toString();
            String password = editTextPass.getText().toString();

            if (TextUtils.isEmpty(email)) {
                editTextEmail.setError(getString(R.string.field_emailInvalid));
            } else if (TextUtils.isEmpty(password)) {
                editTextPass.setError(getString(R.string.field_passMessage));
            } else {
                createNewUser(email, password);
            }
        });
    }

    // Создание нового пользователя и переход на другой экран
    private void createNewUser(String email, String password) {
        if (!isEmailValid(email)) {
            editTextEmail.setError(getString(R.string.field_emailInvalid));
            return;
        }

        Intent registerIntent = new Intent(getContext(), RegisterActivity.class);
        registerIntent.putExtra("userEmail", email);
        registerIntent.putExtra("userPass", password);
        startActivity(registerIntent);
    }

    private boolean isEmailValid(CharSequence email) { // проверки на валидность email-адреса:
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}