package hamsafar.tj.activity.fragments;

import static hamsafar.tj.activity.utility.Utility.showToast;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import hamsafar.tj.R;
import hamsafar.tj.activity.MainActivity;


public class LoginFragment extends Fragment {

    private EditText userEmail, userPass; // Поля ввод юзера
    private Button userLoginBtn; // Кнопка регистрации
    private ProgressBar loginProgress; // Прогрессбар страница(фрагмент) регистрации

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view = inflater.inflate(R.layout.fragment_login, container, false);

        firebaseAuth = FirebaseAuth.getInstance();  // User Table variable
        firebaseFirestore = FirebaseFirestore.getInstance();  // DataBase variable


        userEmail = view.findViewById(R.id.userEmailSingIn);
        userPass = view.findViewById(R.id.userPassSingIn);
        userLoginBtn = view.findViewById(R.id.singInButton);
        loginProgress = view.findViewById(R.id.progressBarAuthLogin);


        userLoginBtn.setOnClickListener(view1 -> {

            loginProgress.setVisibility(View.VISIBLE);
            userLoginBtn.setVisibility(View.INVISIBLE);
            String email = userEmail.getText().toString();
            String password = userPass.getText().toString();
            if(email.length() < 5)
            {
                userEmail.setError("Обязательное поле и не менее 5 символов");
                loginProgress.setVisibility(View.INVISIBLE);
                userLoginBtn.setVisibility(View.VISIBLE);

            } else if(password.length() < 6)
            {
                userPass.setError("Обязательное поле и не менее 6 символов");
                loginProgress.setVisibility(View.INVISIBLE);
                userLoginBtn.setVisibility(View.VISIBLE);

            } else
            {
                loginProgress.setVisibility(View.INVISIBLE);
                userLoginBtn.setVisibility(View.VISIBLE);
                loginUser(email, password);
            }
        });

        return view;
    }

    private void loginUser(String email, String pass) {
        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Intent mainIntent = new Intent(getContext(), MainActivity.class);
                startActivity(mainIntent);
                getActivity().finish();
            } else {
                loginProgress.setVisibility(View.INVISIBLE);
                userLoginBtn.setVisibility(View.VISIBLE);
                showToast(getContext(), "Ошибка входа в аккаунт, пользователь с таким email не найден");
            }
        });
    }
}