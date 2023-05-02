package hamsafar.tj.activity.fragments;

import static hamsafar.tj.R.string.field_emailInvalid;
import static hamsafar.tj.R.string.field_passMessage;
import static hamsafar.tj.R.string.notFoundUserMessage;
import static hamsafar.tj.R.string.signing_in;
import static hamsafar.tj.activity.utility.Utility.showSnakbarTypeOne;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import com.google.firebase.auth.FirebaseAuth;

import hamsafar.tj.R;
import hamsafar.tj.activity.MainActivity;


public class LoginFragment extends Fragment {

    private EditText editTextEmail, editTextPass;
    private Button buttonLogin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Загрузить макет фрагмента "fragment_login"
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Получить экземпляр FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        // Найти элементы интерфейса в макете и сохранить ссылки на них
        buttonLogin = view.findViewById(R.id.singInButton);
        editTextEmail = view.findViewById(R.id.userEmailSingIn);
        editTextPass = view.findViewById(R.id.userPassSingIn);

        // Обработчик щелчков кнопки buttonLogin
        buttonLogin.setOnClickListener(view1 -> {
            String email = editTextEmail.getText().toString();
            String password = editTextPass.getText().toString();

            // Проверяем, есть ли значение в поле Email с помощью TextUtils
            if (TextUtils.isEmpty(email)) {
                editTextEmail.setError(getString(field_emailInvalid));
                return;
            }

            // Проверяем, есть ли значение в поле Password с помощью TextUtils
            if (TextUtils.isEmpty(password)) {
                editTextPass.setError(getString(field_passMessage));
                return;
            }

            // Отображаем диалоговое окно для индикации процесса выполнения задачи
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage(getString(signing_in));
            progressDialog.show();

            // Выполняем попытку входа пользователя с указанным email и паролем
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {

                // Скрываем диалоговое окно
                progressDialog.dismiss();

                // Если вход выполнен успешно, открыть MainActivity
                if (task.isSuccessful()) {
                    Intent mainIntent = new Intent(getContext(), MainActivity.class);
                    startActivity(mainIntent);
                    getActivity().finish();
                }
                // Если произошла ошибка, показываем сообщение об ошибке при помощи Toast
                else {
                    showSnakbarTypeOne(getView(),getString(notFoundUserMessage));
                }
            });
        });

        return view;
    }
}

