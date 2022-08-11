package hamsafar.tj.fragments;

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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import hamsafar.tj.R;
import hamsafar.tj.activity.MainActivity;
import hamsafar.tj.activity.RegisterActivity;


public class LoginFragment extends Fragment {

    private EditText userEmail, userPass;
    private Button userSingInBtn;
    private ProgressBar loginProgressBar;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);

        firebaseAuth = FirebaseAuth.getInstance();  // User Table variable
        firebaseFirestore = FirebaseFirestore.getInstance();  // DataBase variable

        userEmail = view.findViewById(R.id.userEmail);
        userPass = view.findViewById(R.id.userPass);
        userSingInBtn = view.findViewById(R.id.loginBtn);
        loginProgressBar = view.findViewById(R.id.progressBarLogin);


        userSingInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = userEmail.getText().toString();
                String pass = userPass.getText().toString();
                userSingInBtn.setVisibility(View.INVISIBLE);
                loginProgressBar.setVisibility(View.VISIBLE);

                if(TextUtils.isEmpty(email)) {
                    userEmail.setError("Обязательное поле");
                    userSingInBtn.setVisibility(View.VISIBLE);
                    loginProgressBar.setVisibility(View.INVISIBLE);
                } else  if(TextUtils.isEmpty(pass)) {
                    userPass.setError("Обязательное поле");
                    userSingInBtn.setVisibility(View.VISIBLE);
                    loginProgressBar.setVisibility(View.INVISIBLE);
                } else  {
                    userSingInBtn.setVisibility(View.VISIBLE);
                    loginProgressBar.setVisibility(View.INVISIBLE);
                    loginUser(email, pass);
                }
            }
        });
        return  view;
    }

    private void loginUser(String email, String pass) {
        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Intent mainIntent = new Intent(getContext(), MainActivity.class);
                    startActivity(mainIntent);
                    getActivity().finish();
                } else {
                    Toast.makeText(getContext(), "Ошибка входа в аккаунт, повторите попытку позже #23", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}