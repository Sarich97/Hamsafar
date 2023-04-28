package hamsafar.tj.activity.fragments;

import static hamsafar.tj.activity.utility.Utility.isOnline;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import hamsafar.tj.R;
import hamsafar.tj.activity.adapters.PostAdapter;
import hamsafar.tj.activity.models.Post;


public class MyPostFragment extends Fragment {

    private FirebaseAuth firebaseAuth; // FireBase
    private FirebaseFirestore travelPostRef;
    ///   RecyclerView CARD VIEW ON MAIN PAGE
    private RecyclerView recyclerViewPost;
    PostAdapter postAdapter;
    ArrayList<Post> posts = new ArrayList<>();
    private String userKey;
    private ImageView imageViewNotPost;
    private Dialog dialogInternetCon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_post, container, false);

        dialogInternetCon = new Dialog(getContext());

        travelPostRef = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userKey = firebaseAuth.getCurrentUser().getUid();

        imageViewNotPost = view.findViewById(R.id.imageViewNotifiivat);

        recyclerViewPost = view.findViewById(R.id.recyclerViewMyPosts);
        recyclerViewPost.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        postAdapter = new PostAdapter(posts, getContext());
        recyclerViewPost.setAdapter(postAdapter);


        showPostForUsers();


        return view;
    }

    private void showPostForUsers() {
        Query query = travelPostRef.collection("posts").orderBy("timestamp", Query.Direction.DESCENDING);

        query.addSnapshotListener((documentSnapshots, e) -> {
            if (e != null) {
            } else {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        Post post = doc.getDocument().toObject(Post.class);
                        if(post.getUserUD().equals(userKey)) {
                            posts.add(post);
                            postAdapter.notifyDataSetChanged();
                            imageViewNotPost.setVisibility(View.INVISIBLE);
                        } else  {

                        }
                    } else {

                    }
                }
            }

        });
    }
}