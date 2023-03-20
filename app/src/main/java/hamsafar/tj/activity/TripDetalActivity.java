package hamsafar.tj.activity;

import static hamsafar.tj.activity.utility.Utility.showToast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import hamsafar.tj.R;
import hamsafar.tj.activity.adapters.BooksAdapter;
import hamsafar.tj.activity.models.Post;
import hamsafar.tj.activity.models.books;

public class TripDetalActivity extends AppCompatActivity {

    private TextView textViewDriverName, textViewTripStatus, textViewTripPrice, textViewTripStart,textViewTripEnd;
    private TextView textViewComment, textViewDateTime, textViewCarModel, textViewSeat, textViewBookStatus;
    private ImageView imageViewUserImage, buttonBackActivity;
    private Button buttonBookTrip, buttonBookCancel, buttonBookDelete;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore UsersRef,bookRef, noteRef;
    private String post_id;
    private String userKey;
    private Dialog dialogCreatPost;


    private RecyclerView recyclerViewBook;
    BooksAdapter booksAdapter;
    ArrayList<books> booksArrayList = new ArrayList<>();


    @Override
    protected void onStart() {
        super.onStart();
        booksAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detal);

        imageViewUserImage = findViewById(R.id.driverImage);
        textViewDriverName = findViewById(R.id.driverName);
        textViewTripStatus = findViewById(R.id.statusTravel);
        textViewTripPrice = findViewById(R.id.trip_Price);
        textViewTripStart = findViewById(R.id.start_of_route);
        textViewTripEnd = findViewById(R.id.end_of_route);
        textViewDateTime = findViewById(R.id.dateTimeTrip);
        textViewCarModel = findViewById(R.id.carModel);
        textViewBookStatus = findViewById(R.id.textViewBook);
        textViewSeat = findViewById(R.id.seatTrip);
        buttonBookTrip = findViewById(R.id.bookButton);
        buttonBookCancel = findViewById(R.id.bookDeletButton);
        buttonBookDelete = findViewById(R.id.bookEndButton);
        buttonBackActivity = findViewById(R.id.imageViewBackButton);
        textViewComment = findViewById(R.id.textCommentView);

        firebaseAuth = FirebaseAuth.getInstance();
        UsersRef = FirebaseFirestore.getInstance();
        bookRef = FirebaseFirestore.getInstance();
        noteRef = FirebaseFirestore.getInstance();
        userKey = firebaseAuth.getCurrentUser().getUid();


        dialogCreatPost= new Dialog(TripDetalActivity.this);



        recyclerViewBook = findViewById(R.id.booksRecyclerView);
        recyclerViewBook.setHasFixedSize(true);
        recyclerViewBook.setLayoutManager(new LinearLayoutManager(TripDetalActivity.this, LinearLayoutManager.VERTICAL, false));
        booksAdapter = new BooksAdapter(booksArrayList, TripDetalActivity.this);
        recyclerViewBook.setAdapter(booksAdapter);


        String tripStart = getIntent().getExtras().getString("locationFrom");
        String tripEnd = getIntent().getExtras().getString("locationTo");
        String tripDate = getIntent().getExtras().getString("date");
        String tripPrice = getIntent().getExtras().getString("price");
        String tripSeat = getIntent().getExtras().getString("seatTrip");
        String tripBrandCar = getIntent().getExtras().getString("brandCar");
        String tripNameUser = getIntent().getExtras().getString("driverName");
        String tripUserId = getIntent().getExtras().getString("driverID");
        String tripUserPhone = getIntent().getExtras().getString("phone");
        String comments = getIntent().getExtras().getString("commentTrip");
        String isUserDriver = getIntent().getExtras().getString("isUserDriver");
        String postID = getIntent().getExtras().getString("postID");



        textViewDriverName.setText(tripNameUser);
        textViewTripStatus.setText(isUserDriver);

        textViewTripStart.setText(tripStart);
        textViewTripEnd.setText(tripEnd);
        textViewDateTime.setText(tripDate);
        textViewSeat.setText(tripSeat + " чел.");
        textViewComment.setText("Коментарии: " + comments);

        if(tripBrandCar == null) {
            textViewCarModel.setText("Марко автомобиля\n");
        } else {
            textViewCarModel.setText(tripBrandCar);
        }

        if(tripPrice == null) {
            textViewTripPrice.setText("договорная");
        } else {
            textViewTripPrice.setText(tripPrice + " cомони");
        }

        if(isUserDriver.equals("Ищу водителя"))
        {
            textViewBookStatus.setText("Водитель");
        } else {
            textViewBookStatus.setText("Пассажиры");
        }


        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
        String firstName = tripNameUser.substring(0,1);
        TextDrawable user_drawble = TextDrawable.builder()
                .beginConfig()
                .fontSize(38) /* size in px */
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRoundRect(firstName, colorGenerator.getRandomColor(),4); // radius in
        imageViewUserImage.setImageDrawable(user_drawble);


        showBookBtnStatus();
        showBooksList();


        buttonBookTrip.setOnClickListener(view -> {
            if(booksArrayList.size() + 1 > Integer.valueOf(tripSeat))
            {
                showToast(this, "Количество мест ограничено");
            } else {
                UsersRef.collection("users").document(userKey).get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        String user_name = task.getResult().getString("userName");
                        String user_phone = task.getResult().getString("userPhone");
                        bookTrip(user_name, user_phone, postID);

                    }
                });
            }
        });

        buttonBookCancel.setOnClickListener(view -> {
            bookRef.collection("posts/" + postID + "/books").document(userKey).delete();
            bookRef.collection("notificat/" + tripUserId + "/books").document(userKey+postID).delete();
            buttonBookCancel.setVisibility(View.GONE);
            booksAdapter.notifyDataSetChanged();
            gotoMainIntent();
            buttonBookTrip.setVisibility(View.VISIBLE);
            showToast(this,"Ваше бронирование отменено");
        });

        buttonBookDelete.setOnClickListener(view -> {
            if(booksArrayList.size() > 0) {
                showToast(this,"Нельзя удалить поезду пока есть активные заявки");
            } else  {
                bookRef.collection("posts").document(postID).delete();
                showToast(this,"Вы удалили свою заявку!");
                onBackPressed();
                finish();
            }

        });


        buttonBackActivity.setOnClickListener(view -> {
            onBackPressed();
            finish();
        });

    }

    private void gotoMainIntent() {
        Intent mainIntent = new Intent(TripDetalActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void showBooksList() { // Show passenger list
        String postID = getIntent().getExtras().getString("postID");
        Query query = bookRef.collection("posts/" + postID + "/books").orderBy("timestamp", Query.Direction.ASCENDING);

        query.addSnapshotListener(TripDetalActivity.this, (value, error) -> {
            for (DocumentChange doc: value.getDocumentChanges()) {
                books books = doc.getDocument().toObject(books.class);
                if(doc.getType() == DocumentChange.Type.ADDED) {
                    booksArrayList.add(books);
                    booksAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    private void showBookBtnStatus() {
        String postID = getIntent().getExtras().getString("postID");
        String tripUserId = getIntent().getExtras().getString("driverID");
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            bookRef.collection("posts/" + postID + "/books").document(userKey).addSnapshotListener((documentSnapshot, e) -> {

                String passengerID = documentSnapshot.getString("userID");
                try {
                    if (documentSnapshot.exists()) {
                        if(userKey.equals(tripUserId)) {
                            buttonBookDelete.setVisibility(View.VISIBLE);
                            buttonBookTrip.setVisibility(View.GONE);
                        } else {
                            if(passengerID.equals(userKey)){
                                buttonGetForPassengerRequest();
                                buttonBookTrip.setVisibility(View.GONE);
                                buttonBookCancel.setVisibility(View.VISIBLE);
                            } else {
                                bookRef.collection("posts/" + postID +  "/books").document(userKey).addSnapshotListener((value, error) -> {
                                    if(value.exists()) {
                                        buttonBookTrip.setVisibility(View.GONE);
                                        buttonBookCancel.setVisibility(View.GONE);
                                    } else {
                                        buttonGetForPassengerRequest();
                                        buttonBookTrip.setVisibility(View.VISIBLE);
                                        buttonBookCancel.setVisibility(View.GONE);
                                    }
                                });

                            }
                        }
                    } else {
                        if(userKey.equals(tripUserId)) {
                            buttonBookDelete.setVisibility(View.VISIBLE);
                            buttonBookTrip.setVisibility(View.GONE);
                        } else {

                            bookRef.collection("posts/" + postID +  "/books").document(userKey).addSnapshotListener((value, error) -> {
                                if(value.exists()) {
                                    buttonBookTrip.setVisibility(View.GONE);
                                    buttonBookCancel.setVisibility(View.GONE);
                                } else {
                                    buttonSetForPassengerRequest();
                                    buttonBookTrip.setVisibility(View.VISIBLE);
                                    buttonBookCancel.setVisibility(View.GONE);

                                }
                            });
                        }

                    }
                }
                catch (NullPointerException E){

                }
            });
        }
    }

    private void buttonSetForPassengerRequest() {
        String isUserDriver = getIntent().getExtras().getString("isUserDriver");
        String postID = getIntent().getExtras().getString("postID");
        String tripUserId = getIntent().getExtras().getString("driverID");
        if(isUserDriver.equals("Ищу водителя"))
        {
            buttonBookTrip.setText("Принять заявку");
        } else {
            buttonBookTrip.setText("Забронировать");
        }
    }

    private void buttonGetForPassengerRequest() {
        String isUserDriver = getIntent().getExtras().getString("isUserDriver");
        String postID = getIntent().getExtras().getString("postID");
        String tripUserId = getIntent().getExtras().getString("driverID");
        if(isUserDriver.equals("Ищу водителя"))
        {
            buttonBookCancel.setText("Отменить заявку");
        } else {
            buttonBookCancel.setText("Отменить бронирование");
        }
    }

    private void showDialogCreatPost() {
        dialogCreatPost.setContentView(R.layout.creat_post_dialog);
        dialogCreatPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView imageView = dialogCreatPost.findViewById(R.id.imageDialog);
        TextView textView = dialogCreatPost.findViewById(R.id.textDialog);
        Button buttonCancelD = dialogCreatPost.findViewById(R.id.buttonCancelDialog);
        dialogCreatPost.show();

        buttonCancelD.setOnClickListener(view -> {
            dialogCreatPost.cancel();
        });

    }


    private void bookTrip(String user_name, String user_phone, String postID) {
        String tripUserId = getIntent().getExtras().getString("driverID");
        String tripStart = getIntent().getExtras().getString("locationFrom");
        String tripEnd = getIntent().getExtras().getString("locationTo");
        String tripDate = getIntent().getExtras().getString("date");
        String notifiID = userKey + postID;
        bookRef.collection("posts/" + postID + "/books").document(userKey).get().addOnCompleteListener(task -> {
            buttonBookTrip.setVisibility(View.GONE);
            if (!task.getResult().exists()) {
                Map<String, Object> book = new HashMap<>();
                book.put("notifiID",notifiID);
                book.put("notifiStatus","show");
                book.put("userID", userKey);
                book.put("userName", user_name);
                book.put("userPhone", user_phone);
                book.put("postID", postID);
                book.put("postCreateID", tripUserId);
                book.put("locationFrom", tripStart);
                book.put("locationTo", tripEnd);
                book.put("date", tripDate);
                book.put("timestamp", FieldValue.serverTimestamp());
                buttonBookTrip.setVisibility(View.GONE);
                buttonBookCancel.setVisibility(View.VISIBLE);
                showDialogCreatPost();
                //bookRef.collection("posts/" + postID + "/books").document(userKey).set(book);
                noteRef.collection("notificat/" + tripUserId +  "/books").document(notifiID).set(book);
                bookRef.collection("posts/" + postID +  "/books").document(userKey).set(book);
            } else {

//                firebaseFirestore.collection("Posts/" + blogPostId + "/Likes").document(currentUser).delete();
            }
        });

    }
}