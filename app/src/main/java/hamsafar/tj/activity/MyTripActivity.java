package hamsafar.tj.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
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

public class MyTripActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth; // FireBase
    private FirebaseFirestore travelPostRef, bookRef;
    private CollectionReference notificatRef;
    ///   RecyclerView CARD VIEW ON MAIN PAGE
    private RecyclerView recyclerViewPost;
    PostAdapter postAdapter;
    ArrayList<Post> posts = new ArrayList<>();
    private String userKey;
    private TextView textViewBackPageBtn;
    private ImageView imageViewNotPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trip);

        bookRef = FirebaseFirestore.getInstance();
        notificatRef = bookRef.collection("posts");
        travelPostRef = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userKey = firebaseAuth.getCurrentUser().getUid();

        textViewBackPageBtn = findViewById(R.id.toolbarText);
        imageViewNotPost = findViewById(R.id.imageViewNotifiivat);

        recyclerViewPost = findViewById(R.id.recyclerViewMyTrips);
        recyclerViewPost.setLayoutManager(new LinearLayoutManager(MyTripActivity.this, LinearLayoutManager.VERTICAL, false));
        postAdapter = new PostAdapter(posts, MyTripActivity.this);
        recyclerViewPost.setAdapter(postAdapter);



        textViewBackPageBtn.setOnClickListener(view -> {
            onBackPressed();
            finish();
        });

        showTripsForUsers();
    }

    private void showTripsForUsers() {
        Query query = travelPostRef.collection("posts").orderBy("timestamp", Query.Direction.DESCENDING);


        query.addSnapshotListener((documentSnapshots, e) -> {
            if (e != null) {
            } else {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        Post post = doc.getDocument().toObject(Post.class);
                        notificatRef.document(post.getPostId()).collection("books").get().addOnSuccessListener(queryDocumentSnapshots -> {
                            for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                                books books = documentSnapshot.toObject(books.class);
                                if (books.getUserID().equals(userKey)) {
                                    posts.add(post);
                                    postAdapter.notifyDataSetChanged();
                                    imageViewNotPost.setVisibility(View.INVISIBLE);
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