package hamsafar.tj.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;

import hamsafar.tj.R;
import hamsafar.tj.activity.adapters.CardViewAdapter;
import hamsafar.tj.activity.adapters.PostAdapter;
import hamsafar.tj.activity.models.CardViewModel;
import hamsafar.tj.activity.models.Post;

public class MainActivity extends AppCompatActivity {
    private ImageView userProfileBtn;

    private String userID;


    private FirebaseAuth firebaseAuth; // FireBase
    private FirebaseFirestore firebaseFirestore;


    ///   RecyclerView CARD VIEW ON MAIN PAGE
    private RecyclerView recyclerViewCard;
    private RecyclerView.Adapter cardViewAdapter;


    // POST RECYCLVIRE
    private RecyclerView recyclerViewPost;
    PostAdapter postAdapter;
    ArrayList<Post> posts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;

        userProfileBtn = findViewById(R.id.user_ProgileBtn);

        // CARD VIRE
        recyclerViewCard = findViewById(R.id.recyclerViewCard); //CARDVIEW

        // POST VIEW
        recyclerViewPost = findViewById(R.id.recyclerViewPosts);
        recyclerViewPost.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        postAdapter = new PostAdapter(posts, MainActivity.this);
        recyclerViewPost.setAdapter(postAdapter);


        firebaseAuth = FirebaseAuth.getInstance();  // User Table variable
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();


        cardViewRecycler();
        showPostForUsers();


        userProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentUser !=null) {
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.BottomSheetDialogTheme);
                    bottomSheetDialog.setContentView(R.layout.user_profile_sheet);
                    bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    bottomSheetDialog.show();

                    ImageView userImage = bottomSheetDialog.findViewById(R.id.user_Image);
                    TextView userName = bottomSheetDialog.findViewById(R.id.user_Name);
                    TextView carModel = bottomSheetDialog.findViewById(R.id.car_Model);
                    TextView userPhone = bottomSheetDialog.findViewById(R.id.user_Phone);
                    TextView userEmail = bottomSheetDialog.findViewById(R.id.user_Email);

                    userID = firebaseAuth.getCurrentUser().getUid();
                    firebaseFirestore.collection("users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()) {
                                if(task.getResult().exists()) {
                                    String user_name = task.getResult().getString("userName");
                                    String user_email = task.getResult().getString("userEmail");
                                    String user_phone = task.getResult().getString("userPhone");
                                    String car_model = task.getResult().getString("userCarModel");

                                    String firstName = user_name.substring(0, 1);

                                    TextDrawable user_drawble = TextDrawable.builder()
                                            .beginConfig()
                                            .fontSize(26) /* size in px */
                                            .bold()
                                            .toUpperCase()
                                            .endConfig()
                                            .buildRound(firstName, colorGenerator.getRandomColor()); // radius in
                                    userImage.setImageDrawable(user_drawble);

                                    userName.setText(user_name);
                                    userEmail.setText(user_email);
                                    userPhone.setText("+992" + user_phone);
                                    carModel.setText(car_model);
                                }
                            }
                        }
                    });

                } else {
                    Intent authIntent = new Intent(MainActivity.this, AuthActivity.class);
                    startActivity(authIntent);
                    finish();
                }
            }
        });
    }

    private void showPostForUsers() {
        firebaseFirestore.collection("posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {

                } else {
                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            Post post = doc.getDocument().toObject(Post.class);
                            posts.add(post);
                            postAdapter.notifyDataSetChanged();
                        }
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
        recyclerViewCard.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<CardViewModel> cardViewModels = new ArrayList<>();
        cardViewModels.add(new CardViewModel(R.drawable.ic_baseline_control_point_24, "Cоздать поездку", gradient3));
        cardViewModels.add(new CardViewModel(R.drawable.ic_baseline_add_moderator_24, "Безопасное вождение", gradient2));
        cardViewModels.add(new CardViewModel(R.drawable.ic_baseline_add_road_24, "Состояние автодорог", gradient1));
        cardViewModels.add(new CardViewModel(R.drawable.ic_baseline_article_24, "Полезные статьи", gradient4));
        cardViewModels.add(new CardViewModel(R.drawable.ic_baseline_airplane_ticket_24, "Дешевые авиабилеты", gradient5));


        cardViewAdapter = new CardViewAdapter(cardViewModels, this);
        recyclerViewCard.setAdapter(cardViewAdapter);
    }
}