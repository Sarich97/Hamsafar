package hamsafar.tj.activity.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import hamsafar.tj.R;
import hamsafar.tj.activity.RegisterActivity;


public class RegisterFragment extends Fragment {

    private EditText userEmail, userPass; // Поля ввод юзера
    private Button userRegisterBtn; // Кнопка регистрации
    private ProgressBar registerProgress; // Прогрессбар страница(фрагмент) регистрации


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_register, container, false);


        userEmail = view.findViewById(R.id.user_Email);
        userPass = view.findViewById(R.id.user_Pass);
        userRegisterBtn = view.findViewById(R.id.registerNextBtn);
        registerProgress = view.findViewById(R.id.progressBarAuth);


        userRegisterBtn.setOnClickListener(click -> {
            registerProgress.setVisibility(View.VISIBLE);
            userRegisterBtn.setVisibility(View.INVISIBLE);
            String email = userEmail.getText().toString();
            String password = userPass.getText().toString();

            if(email.length() < 5)
            {
                userEmail.setError("Обязательное поле и не менее 5 символов");
                registerProgress.setVisibility(View.INVISIBLE);
                userRegisterBtn.setVisibility(View.VISIBLE);

            } else if(password.length() < 6)
            {
                userPass.setError("Обязательное поле и не менее 6 символов");
                registerProgress.setVisibility(View.INVISIBLE);
                userRegisterBtn.setVisibility(View.VISIBLE);

            } else
            {
                registerProgress.setVisibility(View.INVISIBLE);
                userRegisterBtn.setVisibility(View.VISIBLE);
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