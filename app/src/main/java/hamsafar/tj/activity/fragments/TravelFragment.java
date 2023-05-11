package hamsafar.tj.activity.fragments;


import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hamsafar.tj.R;
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
    private String userKey;
    private String user_city;
    private TextView textViewSearch;
    private Dialog dialogRating, dialogInternetCon; //
    private CardView cardViewConTest;


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
        dialogInternetCon = new Dialog(getContext());

        textViewSearch = view.findViewById(R.id.textViewStatusSearch); // Текст поиска постов если указан мой город

        recyclerViewCard = view.findViewById(R.id.recyclerViewCard); //CARDVIEW
        cardViewConTest = view.findViewById(R.id.cardColorConTest);

        recyclerViewPost = view.findViewById(R.id.recyclerViewPosts);
        recyclerViewPost.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        postAdapter = new PostAdapter(posts, getContext());
        recyclerViewPost.setAdapter(postAdapter);


        firebaseAuth = FirebaseAuth.getInstance();  // User Table variable
        userKey = firebaseAuth.getCurrentUser().getUid();
        travelPostRef = FirebaseFirestore.getInstance();
        userRef = FirebaseFirestore.getInstance();

        cardViewConTest.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
            bottomSheetDialog.setContentView(R.layout.card_con_sheets);
            bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            TextView textViewCode = bottomSheetDialog.findViewById(R.id.textCodeUserID);
            textViewCode.setText(userKey);

            textViewCode.setOnClickListener(v1 -> {
                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", textViewCode.getText().toString());
                Toast.makeText(getContext(), "Код скопирован, теперь можете отправить друзьям", Toast.LENGTH_SHORT).show();
                clipboard.setPrimaryClip(clip);
            });
            bottomSheetDialog.show();
        });

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
                                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
                                    bottomSheetDialog.setContentView(R.layout.show_rating_dialog_sheet);
                                    bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


                                    String user_name = post.getUserName().substring(0,1);
                                    ImageView userImage = bottomSheetDialog.findViewById(R.id.userImageBooks);
                                    TextDrawable user_drawbleSheet = TextDrawable.builder()
                                            .beginConfig()
                                            .fontSize(26) /* size in px */
                                            .bold()
                                            .toUpperCase()
                                            .endConfig()
                                            .buildRoundRect(user_name, colorGenerator.getRandomColor(),4); // radius in
                                    userImage.setImageDrawable(user_drawbleSheet);

                                    TextView userName = bottomSheetDialog.findViewById(R.id.userNameBook);
                                    TextView startTrip = bottomSheetDialog.findViewById(R.id.start_of_route);
                                    TextView endTrip = bottomSheetDialog.findViewById(R.id.end_of_route);
                                    TextView tripDate = bottomSheetDialog.findViewById(R.id.tripDate);
                                    ImageView setPluse = bottomSheetDialog.findViewById(R.id.imageViewSetRatingPluse);
                                    ImageView setMinuse = bottomSheetDialog.findViewById(R.id.imageViewSetRatingMinuse);


                                    setPluse.setOnClickListener(view -> {
                                        UserRef.collection("users").document(post.getUserUD()).get().addOnCompleteListener(task1 -> {
                                            if(task1.isSuccessful()) {
                                                int user_rating = Integer.parseInt(String.valueOf(task1.getResult().get("userRating")));
                                                user_rating ++;
                                                UserRef.collection("users").document(post.getUserUD()).update("userRating", user_rating);
                                                bookRef.collection("posts/" + post.getPostId() + "/books").document(books.getUserID()).update("rating", "set");
                                                bottomSheetDialog.cancel();
                                            }

                                        });
                                    });

                                    setMinuse.setOnClickListener(view -> {
                                        UserRef.collection("users").document(post.getUserUD()).get().addOnCompleteListener(task1 -> {
                                            if(task1.isSuccessful()) {
                                                int user_rating = Integer.parseInt(String.valueOf(task1.getResult().get("userRating")));
                                                user_rating--;
                                                UserRef.collection("users").document(post.getUserUD()).update("userRating", user_rating);
                                                bookRef.collection("posts/" + post.getPostId() + "/books").document(books.getUserID()).update("rating", "set");
                                                bottomSheetDialog.cancel();
                                            }

                                        });

                                    });
                                    userName.setText(post.getUserName());
                                    startTrip.setText(books.getLocationFrom());
                                    endTrip.setText(books.getLocationTo());
                                    tripDate.setText(books.getDate());
                                    bottomSheetDialog.show();
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
        Query query = travelPostRef.collection("posts")
                .whereEqualTo("statusTrip", "show")
                .orderBy("rating", Query.Direction.DESCENDING)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String currentDate = formatter.format(new Date());
        List<Post> newPosts = new ArrayList<>();

        query.addSnapshotListener((documentSnapshots, e) -> {
            if (e != null) {
                // Обработка ошибки
                return;
            } else {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        Post post = doc.getDocument().toObject(Post.class);

                        try {
                            Calendar cal1 = Calendar.getInstance();
                            Calendar cal2 = Calendar.getInstance();

                            cal1.setTime(formatter.parse(currentDate));
                            cal2.setTime(formatter.parse(post.getDataTrip()));

                            if (cal1.after(cal2)) {
                                continue;
                            }
                        } catch (ParseException err) {
                            err.printStackTrace();
                        }

                        newPosts.add(post);
                    }
                }

                posts.clear();
                posts.addAll(newPosts);
                newPosts.clear();

                postAdapter.notifyDataSetChanged();
            }
        });
    }

    private void cardViewRecycler() {
        GradientDrawable gradient2 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xFF105dd0, 0xFF105dd0});
        GradientDrawable gradient1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xFF9937fc, 0xFF9937fc});
        GradientDrawable gradient3 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xfffb7e36, 0xFFfb7e36});
        GradientDrawable gradient4 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xff0fd59e, 0xff0fd59e});
        GradientDrawable gradient5 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffb8cee0, 0xffb8cee0});

       // recyclerViewCard.setHasFixedSize(true);
        recyclerViewCard.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        ArrayList<CardViewModel> cardViewModels = new ArrayList<>();
        cardViewModels.add(new CardViewModel(R.drawable.ic_baseline_add_road_24, "Состояние автодорог", gradient1));
        cardViewModels.add(new CardViewModel(R.drawable.ic_baseline_add_moderator_24, "Безопасное вождение", gradient2));
        cardViewModels.add(new CardViewModel(R.drawable.ic_baseline_airplane_ticket_24, "Дешевые авиабилеты", gradient4));


        cardViewAdapter = new CardViewAdapter(cardViewModels, getContext());
        recyclerViewCard.setAdapter(cardViewAdapter);
    }
}