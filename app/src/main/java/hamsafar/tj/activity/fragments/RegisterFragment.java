package hamsafar.tj.activity.fragments;

import static hamsafar.tj.R.string.field_emailMessage;
import static hamsafar.tj.R.string.field_passMessage;

import android.content.Intent;
import android.os.Bundle;


import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import hamsafar.tj.R;
import hamsafar.tj.activity.RegisterActivity;


public class RegisterFragment extends Fragment {

    private EditText editTextEmail, editTextPass; // Поля ввод юзера
    private Button buttonRegister; // Кнопка регистрации

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_register, container, false);


        editTextEmail = view.findViewById(R.id.user_Email);
        editTextPass = view.findViewById(R.id.user_Pass);
        buttonRegister = view.findViewById(R.id.registerNextBtn);


        buttonRegister.setOnClickListener(click -> {

            String email = editTextEmail.getText().toString();
            String password = editTextPass.getText().toString();

            if(TextUtils.isEmpty(email))
            {
                editTextEmail.setError(getString(field_emailMessage));


            } else if(TextUtils.isEmpty(password))
            {
                editTextPass.setError(getString(field_passMessage));

            } else
            {
                createNewUser(email, password);
            }
        });

        return view;
    }

    private void createNewUser(String email, String password) {
        Intent registerIntent = new Intent(getContext(), RegisterActivity.class);
        registerIntent.putExtra("userEmail", email);
        registerIntent.putExtra("userPass", password);
        startActivity(registerIntent);
    }
}