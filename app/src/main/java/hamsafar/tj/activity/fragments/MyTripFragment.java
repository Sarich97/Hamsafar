package hamsafar.tj.activity.fragments;

import static hamsafar.tj.activity.utility.Utility.BOOKS_COLLECTION;
import static hamsafar.tj.activity.utility.Utility.POSTS_COLLECTION;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import java.util.ArrayList;

import hamsafar.tj.R;
import hamsafar.tj.activity.adapters.PostAdapter;
import hamsafar.tj.activity.models.Post;
import hamsafar.tj.activity.models.books;


public class MyTripFragment extends Fragment {

    private FirebaseAuth firebaseAuth; // FireBase
    private FirebaseFirestore travelPostRef, bookRef;
    private CollectionReference notificatRef;
    ///   RecyclerView CARD VIEW ON MAIN PAGE
    private RecyclerView recyclerViewPost;
    PostAdapter postAdapter;
    ArrayList<Post> posts = new ArrayList<>();
    private String userKey;
    private ImageView imageViewNotPost;
    private TextView textViewDescpTrip;
    private Dialog dialogInternetCon;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_trip, container, false);

        dialogInternetCon = new Dialog(getContext());

        bookRef = FirebaseFirestore.getInstance();
        notificatRef = bookRef.collection(POSTS_COLLECTION);
        travelPostRef = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userKey = firebaseAuth.getCurrentUser().getUid();

        imageViewNotPost = view.findViewById(R.id.imageViewNotifiivat);
        textViewDescpTrip = view.findViewById(R.id.textViewDescpT);

        recyclerViewPost = view.findViewById(R.id.recyclerViewMyTrips);
        recyclerViewPost.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        postAdapter = new PostAdapter(posts, getContext());
        recyclerViewPost.setAdapter(postAdapter);


        showTripsForUsers();



        return view;
    }

    private void showTripsForUsers() {

        Query query = travelPostRef.collection(POSTS_COLLECTION).orderBy("timestamp", Query.Direction.DESCENDING)
                        .limit(20);


        query.addSnapshotListener((documentSnapshots, e) -> {
            if (e != null) {
            } else {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        Post post = doc.getDocument().toObject(Post.class);
                        notificatRef.document(post.getPostId()).collection(BOOKS_COLLECTION).get().addOnSuccessListener(queryDocumentSnapshots -> {
                            for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                                books books = documentSnapshot.toObject(books.class);
                                if (books.getUserID().equals(userKey)) {
                                    posts.add(post);
                                    postAdapter.notifyDataSetChanged();
                                    imageViewNotPost.setVisibility(View.INVISIBLE);
                                    textViewDescpTrip.setVisibility(View.INVISIBLE);
                                } else {

                                }
                            }
                        });

                    } else {

                    }
                }
            }

        });
    }
}