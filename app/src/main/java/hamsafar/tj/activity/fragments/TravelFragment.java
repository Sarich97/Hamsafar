package hamsafar.tj.activity.fragments;

import static hamsafar.tj.R.color.colorRed;
import static hamsafar.tj.R.drawable.baseline_add_box_24;
import static hamsafar.tj.activity.utility.Utility.showSnakbarTypeOne;
import static hamsafar.tj.activity.utility.Utility.showToast;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import hamsafar.tj.R;
import hamsafar.tj.activity.AuthActivity;
import hamsafar.tj.activity.TripDetalActivity;
import hamsafar.tj.activity.adapters.CardViewAdapter;
import hamsafar.tj.activity.adapters.PostAdapter;
import hamsafar.tj.activity.models.CardViewModel;
import hamsafar.tj.activity.models.Post;
import hamsafar.tj.activity.models.books;


public class TravelFragment extends Fragment {

    private FirebaseAuth firebaseAuth; // FireBase
    private FirebaseFirestore travelPostRef, bookRef, UserRef;
    private CollectionReference notificatRef;
    private FirebaseFirestore userRef;
    ///   RecyclerView CARD VIEW ON MAIN PAGE
    private RecyclerView recyclerViewCard;
    private RecyclerView.Adapter cardViewAdapter;
    private ImageView imageViewMyCytyPosts;
    private String userKey;
    private String user_city;
    private int currentPageIdPost = 2;
    private TextView textViewSearch;
    private Dialog dialogRating; //


    private RecyclerView recyclerViewPost;
    PostAdapter postAdapter;
    ArrayList<Post> posts = new ArrayList<>();

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        posts.removeAll(posts);
        postAdapter.notifyDataSetChanged();
        super.onDestroy();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_travel, container, false);

        bookRef = FirebaseFirestore.getInstance();
        UserRef = FirebaseFirestore.getInstance();
        notificatRef = bookRef.collection("posts");

        dialogRating= new Dialog(getContext());

        imageViewMyCytyPosts = view.findViewById(R.id.imageViewPostSetting); // КНОПКА ПОКАЗАТЬ ПОСТЫ ИЗ МОЕГО ГОРОДА
        textViewSearch = view.findViewById(R.id.textViewStatusSearch); // Текст поиска постов если указан мой город

        recyclerViewCard = view.findViewById(R.id.recyclerViewCard); //CARDVIEW

        recyclerViewPost = view.findViewById(R.id.recyclerViewPosts);
        recyclerViewPost.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        postAdapter = new PostAdapter(posts, getContext());
        recyclerViewPost.setAdapter(postAdapter);


        imageViewMyCytyPosts.setOnClickListener(view1 -> {
            if(currentPageIdPost == 2) {
                posts.removeAll(posts);
                posts.clear();
                showPostForUsers();
                currentPageIdPost = 1;
                textViewSearch.setText("Поездки из Вашего города");
                imageViewMyCytyPosts.setColorFilter(getContext().getResources().getColor(R.color.colorPrimary));
            } else if(currentPageIdPost == 1) {
                posts.removeAll(posts);
                posts.clear();
                showPostForUsers();
                currentPageIdPost = 2;
                textViewSearch.setText("Актуальные поездки");
                imageViewMyCytyPosts.setColorFilter(getContext().getResources().getColor(R.color.colorIcons));
            }

        });

        firebaseAuth = FirebaseAuth.getInstance();  // User Table variable
        userKey = firebaseAuth.getCurrentUser().getUid();
        travelPostRef = FirebaseFirestore.getInstance();
        userRef = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        showPostForUsers();
        showRatingDialog();
        cardViewRecycler();

        return view;
    }

    private void showRatingDialog() {
        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
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
                                if (books.getUserID().equals(userKey) && post.getIsDriverUser().equals("Поездка завершена") && books.getRating().equals("no")) {
                                    dialogRating.setContentView(R.layout.show_rating_dialog_sheet);
                                    dialogRating.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                    String user_name = post.getUserName().substring(0,1);
                                    ImageView userImage = dialogRating.findViewById(R.id.userImageBooks);
                                    TextDrawable user_drawbleSheet = TextDrawable.builder()
                                            .beginConfig()
                                            .fontSize(26) /* size in px */
                                            .bold()
                                            .toUpperCase()
                                            .endConfig()
                                            .buildRoundRect(user_name, colorGenerator.getRandomColor(),4); // radius in
                                    userImage.setImageDrawable(user_drawbleSheet);

                                    TextView userName = dialogRating.findViewById(R.id.userNameBook);
                                    TextView startTrip = dialogRating.findViewById(R.id.start_of_route);
                                    TextView endTrip = dialogRating.findViewById(R.id.end_of_route);
                                    TextView tripDate = dialogRating.findViewById(R.id.tripDate);
                                    ImageView setPluse = dialogRating.findViewById(R.id.imageViewSetRatingPluse);
                                    ImageView setMinuse = dialogRating.findViewById(R.id.imageViewSetRatingMinuse);


                                    setPluse.setOnClickListener(view -> {
                                        UserRef.collection("users").document(post.getUserUD()).get().addOnCompleteListener(task1 -> {
                                            showToast(getContext(),post.getUserUD());
                                            if(task1.isSuccessful()) {
                                                int user_rating = Integer.parseInt(String.valueOf(task1.getResult().get("userRating")));
                                                user_rating ++;
                                                UserRef.collection("users").document(post.getUserUD()).update("userRating", user_rating);
                                                bookRef.collection("posts/" + post.getPostId() + "/books").document(books.getUserID()).update("rating", "set");
                                                dialogRating.cancel();
                                            }

                                        });
                                    });

                                    setMinuse.setOnClickListener(view -> {
                                        showToast(getContext(),post.getUserUD());
                                        UserRef.collection("users").document(post.getUserUD()).get().addOnCompleteListener(task1 -> {
                                            if(task1.isSuccessful()) {
                                                int user_rating = Integer.parseInt(String.valueOf(task1.getResult().get("userRating")));
                                                user_rating--;
                                                UserRef.collection("users").document(post.getUserUD()).update("userRating", user_rating);
                                                bookRef.collection("posts/" + post.getPostId() + "/books").document(books.getUserID()).update("rating", "set");
                                                dialogRating.cancel();
                                            }

                                        });

                                    });

                                    userName.setText(post.getUserName());
                                    startTrip.setText(books.getLocationFrom());
                                    endTrip.setText(books.getLocationTo());
                                    tripDate.setText(books.getDate());
                                    dialogRating.show();
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

    private void showPostForUsers() {
        Query query = travelPostRef.collection("posts").orderBy("timestamp", Query.Direction.DESCENDING);

        query.addSnapshotListener((documentSnapshots, e) -> {
            if (e != null) {
            } else {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                            Post post = doc.getDocument().toObject(Post.class);

                            if(currentPageIdPost == 2) {
                                if(post.getStatusTrip().equals("show")) {
                                    posts.add(post);
                                    postAdapter.notifyDataSetChanged();
                                } else {

                                }
                            } else if(currentPageIdPost == 1) {
                                userRef.collection("users").document(userKey).get().addOnCompleteListener(task -> {
                                    if(task.isSuccessful()) {
                                        user_city = task.getResult().getString("userCity");
                                        if(post.getStartTrip().equals(user_city) && post.getStatusTrip().equals("show")) {
                                            if(postAdapter.getItemCount() > 0) {
                                                posts.add(post);
                                                postAdapter.notifyDataSetChanged();
                                            } else {
                                                posts.add(post);
                                                postAdapter.notifyDataSetChanged();
                                            }

                                        } else {

                                        }
                                    }
                                });
                            }

                    }
                    else {
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
        cardViewModels.add(new CardViewModel(R.drawable.ic_baseline_add_road_24, "Состояние автодорог", gradient1));
        cardViewModels.add(new CardViewModel(R.drawable.ic_baseline_control_point_24, "Как создать поездку?", gradient3));
        cardViewModels.add(new CardViewModel(R.drawable.ic_baseline_add_moderator_24, "Безопасное вождение", gradient2));
        cardViewModels.add(new CardViewModel(R.drawable.ic_baseline_article_24, "Полезные статьи", gradient4));
        cardViewModels.add(new CardViewModel(R.drawable.ic_baseline_airplane_ticket_24, "Дешевые авиабилеты", gradient5));

        cardViewAdapter = new CardViewAdapter(cardViewModels, getContext());
        recyclerViewCard.setAdapter(cardViewAdapter);
    }
}