package hamsafar.tj.activity.fragments;

import static hamsafar.tj.R.string.field_emailMessage;
import static hamsafar.tj.R.string.field_passMessage;
import static hamsafar.tj.R.string.notFoundUserMessage;
import static hamsafar.tj.activity.utility.Utility.showSnakbarTypeOne;
import static hamsafar.tj.activity.utility.Utility.showToast;

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

import com.google.firebase.auth.FirebaseAuth;

import hamsafar.tj.R;
import hamsafar.tj.activity.MainActivity;


public class LoginFragment extends Fragment {

    private EditText editTextEmail, editTextPass; // Поля ввод юзера
    private Button buttonLogin; // Кнопка регистрации
    private ProgressBar progressBarLogin; // Прогрессбар страница(фрагмент) регистрации

    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        firebaseAuth = FirebaseAuth.getInstance();  // User Table variable

        buttonLogin = view.findViewById(R.id.singInButton);
        progressBarLogin = view.findViewById(R.id.progressLogin);

        editTextEmail = view.findViewById(R.id.userEmailSingIn);
        editTextPass = view.findViewById(R.id.userPassSingIn);



        buttonLogin.setOnClickListener(view1 -> {
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
                loginUser(email, password);
            }

        });


        return view;
    }

    private void loginUser(String email, String pass) {
        buttonLogin.setVisibility(View.INVISIBLE);
        progressBarLogin.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Intent mainIntent = new Intent(getContext(), MainActivity.class);
                startActivity(mainIntent);
                getActivity().finish();
            } else {
                progressBarLogin.setVisibility(View.INVISIBLE);
                buttonLogin.setVisibility(View.VISIBLE);
                showSnakbarTypeOne(getView(),getString(notFoundUserMessage));
            }
        });
    }
}