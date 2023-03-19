package hamsafar.tj.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import hamsafar.tj.R;
import hamsafar.tj.activity.adapters.PostAdapter;
import hamsafar.tj.activity.models.Post;

public class MyPostActivity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth; // FireBase
    private FirebaseFirestore travelPostRef;
    ///   RecyclerView CARD VIEW ON MAIN PAGE
    private RecyclerView recyclerViewPost;
    PostAdapter postAdapter;
    ArrayList<Post> posts = new ArrayList<>();
    private String userKey;
    private TextView textViewBackPageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post);

        travelPostRef = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userKey = firebaseAuth.getCurrentUser().getUid();

        textViewBackPageBtn = findViewById(R.id.toolbarText);

        recyclerViewPost = findViewById(R.id.recyclerViewMyPosts);
        recyclerViewPost.setLayoutManager(new LinearLayoutManager(MyPostActivity.this, LinearLayoutManager.VERTICAL, false));
        postAdapter = new PostAdapter(posts, MyPostActivity.this);
        recyclerViewPost.setAdapter(postAdapter);


        textViewBackPageBtn.setOnClickListener(view -> {
            onBackPressed();
            finish();
        });

        showPostForUsers();
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
                        } else  {

                        }
                    } else {
                    }
                }
            }

        });
    }
}