package hamsafar.tj.activity.fragments;

import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import hamsafar.tj.R;
import hamsafar.tj.activity.adapters.PostAdapter;
import hamsafar.tj.activity.models.Post;


public class ProfileFragment extends Fragment {

    private FirebaseAuth firebaseAuth; // FireBase
    private FirebaseFirestore firebaseFirestore,travelPostRef ;
    private String userID;

    private RecyclerView recyclerViewPost;
    PostAdapter postAdapter;
    ArrayList<Post> posts = new ArrayList<>();

    private TextView userNameP, userPhoneP;
    private ImageView userImageP, loadPostImage;
    private TextView textPostNot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);



        firebaseAuth = FirebaseAuth.getInstance();  // User Table variable
        firebaseFirestore = FirebaseFirestore.getInstance();
        travelPostRef = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        userID = firebaseAuth.getCurrentUser().getUid();


        recyclerViewPost = view.findViewById(R.id.recyclerViewMyPosts);
        recyclerViewPost.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        postAdapter = new PostAdapter(posts, getContext());
        recyclerViewPost.setAdapter(postAdapter);

        userNameP = view.findViewById(R.id.userNameProfile);
        userPhoneP = view.findViewById(R.id.userPhoneProfile);
        userImageP = view.findViewById(R.id.userImageProfile);
        loadPostImage = view.findViewById(R.id.imageViewProfilNotPost);
        textPostNot = view.findViewById(R.id.textViewNotPosts);

        showPostForUser();
        showProfileForUser();



        return view;
    }

    private void showProfileForUser() {
        firebaseFirestore.collection("users").document(userID).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
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
    }

    private void showPostForUser() {
        Query query = travelPostRef.collection("posts");

        query.addSnapshotListener((documentSnapshots, e) -> {
            if (e != null) {
            } else {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        Post post = doc.getDocument().toObject(Post.class);
                        if(post.getUserUD().equals(userID))
                        {
                            posts.add(post);
                            postAdapter.notifyDataSetChanged();
                            textPostNot.setVisibility(View.INVISIBLE);
                        } else {
                            loadPostImage.setVisibility(View.VISIBLE);
                            textPostNot.setVisibility(View.VISIBLE);
                            postAdapter.notifyDataSetChanged();
                        }

                    }
                }
            }

        });
    }

}