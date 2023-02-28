package hamsafar.tj.activity.fragments;

import android.os.Bundle;


import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import hamsafar.tj.R;


public class ProfileFragment extends Fragment {

    private FirebaseAuth firebaseAuth; // FireBase
    private FirebaseFirestore firebaseFirestore;
    private String userID;

    private TextView userNameP, userPhoneP;
    private ImageView userImageP;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;

        firebaseAuth = FirebaseAuth.getInstance();  // User Table variable
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        userID = firebaseAuth.getCurrentUser().getUid();

        userNameP = view.findViewById(R.id.userNameProfile);
        userPhoneP = view.findViewById(R.id.userPhoneProfile);
        userImageP = view.findViewById(R.id.userImageProfile);

        firebaseFirestore.collection("users").document(userID).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                String user_name = task.getResult().getString("userName");
                String user_phone = task.getResult().getString("userPhone");

                userNameP.setText(user_name);
                userPhoneP.setText(user_phone);

                String userNameName = user_name.substring(0,1);

                TextDrawable user_drawble = TextDrawable.builder()
                        .beginConfig()
                        .fontSize(26) /* size in px */
                        .bold()
                        .toUpperCase()
                        .endConfig()
                        .buildRoundRect(userNameName, colorGenerator.getRandomColor(),4); // radius in
                userImageP.setImageDrawable(user_drawble);

            } else  {

            }
        });

        return view;
    }

}