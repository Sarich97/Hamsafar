package hamsafar.tj.activity.fragments;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import hamsafar.tj.R;
import hamsafar.tj.activity.AuthActivity;
import hamsafar.tj.activity.adapters.CardViewAdapter;
import hamsafar.tj.activity.adapters.PostAdapter;
import hamsafar.tj.activity.models.CardViewModel;
import hamsafar.tj.activity.models.Post;


public class TravelFragment extends Fragment {

    private FirebaseAuth firebaseAuth; // FireBase
    private FirebaseFirestore travelPostRef;
    ///   RecyclerView CARD VIEW ON MAIN PAGE
    private RecyclerView recyclerViewCard;
    private RecyclerView.Adapter cardViewAdapter;

    private RecyclerView recyclerViewPost;
    PostAdapter postAdapter;
    ArrayList<Post> posts = new ArrayList<>();
    private ProgressBar progressBarPostLoad;;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_travel, container, false);


        recyclerViewCard = view.findViewById(R.id.recyclerViewCard); //CARDVIEW

        recyclerViewPost = view.findViewById(R.id.recyclerViewPosts);
        recyclerViewPost.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        postAdapter = new PostAdapter(posts, getContext());
        recyclerViewPost.setAdapter(postAdapter);
        progressBarPostLoad = view.findViewById(R.id.progressBarPost);


        firebaseAuth = FirebaseAuth.getInstance();  // User Table variable
        travelPostRef = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        showPostForUsers();
        cardViewRecycler();

        return view;
    }


    private void showPostForUsers() {
        Query query = travelPostRef.collection("posts").orderBy("timestamp", Query.Direction.DESCENDING);

        query.addSnapshotListener((documentSnapshots, e) -> {
            if (e != null) {
                progressBarPostLoad.setVisibility(View.VISIBLE);
            } else {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        Post post = doc.getDocument().toObject(Post.class);
                        posts.add(post);
                        postAdapter.notifyDataSetChanged();
                        progressBarPostLoad.setVisibility(View.INVISIBLE);
                    }
                }
            }

        });
    }
    private void cardViewRecycler() {
        GradientDrawable gradient2 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xFF105dd0, 0xFF105dd0});
        GradientDrawable gradient1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xFF9937fc, 0xFF9937fc});
        GradientDrawable gradient3 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xfffb7e36, 0xFFfb7e36});
        GradientDrawable gradient4 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xff0fd59e, 0xff0fd59e});
        GradientDrawable gradient5 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffb8cee0, 0xffb8cee0});

        recyclerViewCard.setHasFixedSize(true);
        recyclerViewCard.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        ArrayList<CardViewModel> cardViewModels = new ArrayList<>();
        cardViewModels.add(new CardViewModel(R.drawable.ic_baseline_control_point_24, "Как создать поездку?", gradient3));
        cardViewModels.add(new CardViewModel(R.drawable.ic_baseline_add_moderator_24, "Безопасное вождение", gradient2));
        cardViewModels.add(new CardViewModel(R.drawable.ic_baseline_add_road_24, "Состояние автодорог", gradient1));
        cardViewModels.add(new CardViewModel(R.drawable.ic_baseline_article_24, "Полезные статьи", gradient4));
        cardViewModels.add(new CardViewModel(R.drawable.ic_baseline_airplane_ticket_24, "Дешевые авиабилеты", gradient5));

        cardViewAdapter = new CardViewAdapter(cardViewModels, getContext());
        recyclerViewCard.setAdapter(cardViewAdapter);
    }
}