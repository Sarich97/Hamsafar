package hamsafar.tj.activity.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;

import hamsafar.tj.R;
import hamsafar.tj.activity.AuthActivity;
import hamsafar.tj.activity.adapters.CardViewAdapter;
import hamsafar.tj.activity.adapters.ListAdapter;
import hamsafar.tj.activity.models.CardViewModel;
import hamsafar.tj.activity.models.listModel;


public class ProfileFragment extends Fragment {

    private FirebaseAuth firebaseAuth; // FireBase
    private FirebaseFirestore firebaseFirestore,travelPostRef ;
    private String userID;

    private TextView textViewUserName, textViewUserEmail, textViewSinhOutBtn;
    private ImageView userImageP;


    private RecyclerView listViewMenu;
    private RecyclerView.Adapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        listViewMenu = view.findViewById(R.id.recyclerViewList); //CARDVIEW

        firebaseAuth = FirebaseAuth.getInstance();  // User Table variable
        firebaseFirestore = FirebaseFirestore.getInstance();
        travelPostRef = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        userID = firebaseAuth.getCurrentUser().getUid();


        textViewUserName = view.findViewById(R.id.userNameProfile);
        textViewUserEmail = view.findViewById(R.id.userEmail);
        userImageP = view.findViewById(R.id.userImageProfile);
        textViewSinhOutBtn = view.findViewById(R.id.logoutTextBtn);


        showProfileForUser();
        showMenuForUser();

        textViewSinhOutBtn.setOnClickListener(view1 -> {
            firebaseAuth.signOut();
            Intent authIntent = new Intent(getContext(), AuthActivity.class);
            startActivity(authIntent);
            getActivity().finish();
        });


        return view;
    }

    private void showMenuForUser() {
        listViewMenu.setHasFixedSize(true);
        listViewMenu.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        ArrayList<listModel> listModels = new ArrayList<>();
        listModels.add(new listModel(R.drawable.baseline_note_add_24, "Мои заявки"));
        listModels.add(new listModel(R.drawable.ic_baseline_directions_car_24, "Мои поездки"));
        listModels.add(new listModel(R.drawable.baseline_person_24_green, "Редактировать профиль"));
        listModels.add(new listModel(R.drawable.baseline_privacy_tip_24, "Политика конфиденциальности"));
        listModels.add(new listModel(R.drawable.baseline_help_24, "Помощь"));

        adapter = new ListAdapter(listModels, getContext());
        listViewMenu.setAdapter(adapter);
    }


    private void showProfileForUser() {
        firebaseFirestore.collection("users").document(userID).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
                String user_name = task.getResult().getString("userName");
                String user_phone = task.getResult().getString("userEmail");

                textViewUserName.setText(user_name);
                textViewUserEmail.setText(user_phone);

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
    }

//    private void showPostForUser() {
//        Query query = travelPostRef.collection("posts");
//
//        query.addSnapshotListener((documentSnapshots, e) -> {
//            if (e != null) {
//            } else {
//                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
//                    if (doc.getType() == DocumentChange.Type.ADDED) {
//                        Post post = doc.getDocument().toObject(Post.class);
//                        if(post.getUserUD().equals(userID))
//                        {
//                            posts.add(post);
//                            postAdapter.notifyDataSetChanged();
//                        } else {
//
//                        }
//
//                    }
//                }
//            }
//
//        });
//    }

}