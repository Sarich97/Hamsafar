package hamsafar.tj.activity;

import static hamsafar.tj.activity.utility.Utility.POSTS_COLLECTION;
import static hamsafar.tj.activity.utility.Utility.USERS_COLLECTION;
import static hamsafar.tj.activity.utility.Utility.isOnline;
import static hamsafar.tj.activity.utility.Utility.showSnakbarTypeOne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;

import hamsafar.tj.R;
import hamsafar.tj.activity.adapters.BooksAdapter;
import hamsafar.tj.activity.models.books;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class TripDetalActivity extends AppCompatActivity {

    private TextView textViewDriverName, textViewTripStatus, textViewTripPrice, textViewTripStart,textViewTripEnd, textViewBooksView;
    private TextView textViewComment, textViewDateTime, textViewCarModel, textViewSeat, textViewBookStatus, textViewStatusBox;
    private ImageView imageViewUserImage, buttonBackActivity, imageViewButtonDelete, imageViewCallButton, imageViewBooksView;
    private Button buttonBookTrip, buttonBookCancel, buttonBookFinish;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore UsersRef,bookRef, noteRef, postRef;
    private String userKey;
    private Dialog dialogCreatPost;
    private Dialog dialogUserInfo;
    private static final int REQUEST_CALL = 1;
    private View viewSnackbar;

    private RecyclerView recyclerViewBook;
    BooksAdapter booksAdapter;
    ArrayList<books> booksArrayList = new ArrayList<>();

    private Dialog dialogInternetCon; //


    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onStart() {
        super.onStart();
        booksAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detal);


        viewSnackbar = findViewById(android.R.id.content);

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
        buttonBookFinish = findViewById(R.id.bookEndButton);
        buttonBackActivity = findViewById(R.id.imageViewBackButton);
        textViewComment = findViewById(R.id.textCommentView);
        imageViewButtonDelete =  findViewById(R.id.imageViewDeletePost);
        imageViewCallButton = findViewById(R.id.imageViewCallToCreatorPost);
        textViewBooksView = findViewById(R.id.textViewBookTest);
        imageViewBooksView = findViewById(R.id.imageViewBook);
        textViewStatusBox = findViewById(R.id.statusBox);

        firebaseAuth = FirebaseAuth.getInstance();
        UsersRef = FirebaseFirestore.getInstance();
        bookRef = FirebaseFirestore.getInstance();
        noteRef = FirebaseFirestore.getInstance();
        postRef = FirebaseFirestore.getInstance();
        userKey = firebaseAuth.getCurrentUser().getUid();


        dialogCreatPost= new Dialog(TripDetalActivity.this);
        dialogUserInfo = new Dialog(TripDetalActivity.this);
        dialogInternetCon = new Dialog(TripDetalActivity.this); // ОКОШОКА ПОКАЗВАЕТ НЕТ ИНТЕРНЕТА




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
        String isPackBox = getIntent().getExtras().getString("isPackBox");
        String postID = getIntent().getExtras().getString("postID");

        recyclerViewBook = findViewById(R.id.booksRecyclerView);
        //recyclerViewBook.setHasFixedSize(true);
        recyclerViewBook.setLayoutManager(new LinearLayoutManager(TripDetalActivity.this, LinearLayoutManager.VERTICAL, false));
        booksAdapter = new BooksAdapter(booksArrayList, TripDetalActivity.this);
        recyclerViewBook.setAdapter(booksAdapter);

        if (tripUserId.equals(userKey) && !isUserDriver.equals("Поездка завершена")) { //  Если поездка не завершена то можно удлаить пользователя!
            new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerViewBook);
        }
        if(isPackBox.equals("Беру посылку: Да")) {
            textViewStatusBox.setText("  Посылки  ");
        } else {
            textViewStatusBox.setVisibility(View.INVISIBLE);
        }

        textViewDriverName.setText(tripNameUser);
        textViewTripStatus.setText(" " + isUserDriver + " ");

        textViewTripStart.setText(tripStart);
        textViewTripEnd.setText(tripEnd);
        textViewDateTime.setText(tripDate);
        textViewSeat.setText(String.format("%s чел.", tripSeat));
        textViewCarModel.setText(tripBrandCar == null ? "Марка: любая" : tripBrandCar); // Показываем если пост от пассажира

        textViewComment.setVisibility(comments.isEmpty() ? View.GONE : View.VISIBLE); // Есть есть комменты показываем// Иначе нет!
        textViewComment.setText(comments);

        showBookBtnStatus();
        showBooksList();



        if(isUserDriver.equals("Поездка завершена")) {
            textViewTripStatus.setBackgroundResource(R.drawable.tripstatuis_red);
            textViewStatusBox.setVisibility(View.INVISIBLE);
            buttonBookTrip.setVisibility(View.INVISIBLE);
            buttonBookCancel.setVisibility(View.INVISIBLE);
            buttonBookFinish.setVisibility(View.INVISIBLE);
        }

        if(tripPrice == null) {
            textViewTripPrice.setText("договорная");
        } else {
            textViewTripPrice.setText(tripPrice + " cомони");
        }

        switch (isUserDriver) { // Текст спипок водетеля/пассажира
            case "Ищу водителя":
                textViewBookStatus.setText("Водитель");
                break;
            default:
                textViewBookStatus.setText("Пассажиры");
                break;
        }

        imageViewButtonDelete.setVisibility(tripUserId.equals(userKey) ? View.VISIBLE : View.INVISIBLE);// Показываем кнопка "Удалить" если пост от пользователя
        imageViewCallButton.setVisibility(tripUserId.equals(userKey) ? View.INVISIBLE : View.VISIBLE); // Показываем кнопку "Позвонить" если чужой пост


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

        buttonBookTrip.setOnClickListener(view -> { // Кнопка забронировать.

            if(isOnline(this)) {
                if(booksArrayList.size() + 1 > Integer.valueOf(tripSeat))
                {
                    showSnakbarTypeOne(viewSnackbar, "Количество мест ограничено");
                } else {
                    UsersRef.collection(USERS_COLLECTION).document(userKey).get().addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            String user_name = task.getResult().getString("userName");
                            String user_phone = task.getResult().getString("userPhone");
                            String user_rating = task.getResult().get("userRating").toString();
                            String user_trip_count = task.getResult().get("userTrip").toString();
                            bookTrip(user_name, user_phone, user_rating, user_trip_count, postID);
                        }
                    });
                }
            } else  {
                dialogInternetCon.setContentView(R.layout.internet_connecting_dialog);
                dialogInternetCon.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogInternetCon.show();
            }
        });

        buttonBookCancel.setOnClickListener(view -> { // Кнопка Отмена бронирование поездки
            if(isOnline(this)) {
                AlertDialog.Builder deleteDialog = new AlertDialog.Builder(TripDetalActivity.this);
                // Указываем текст сообщение
                deleteDialog.setMessage("Вы уверены, что хотите отменить ?");

                deleteDialog.setPositiveButton("Да", (dialog, which) -> {
                    bookRef.collection("posts/" + postID + "/books").document(userKey).delete();
                    bookRef.collection("notificat/" + tripUserId + "/books").document(userKey+postID).delete();
                    buttonBookCancel.setVisibility(View.GONE);
                    booksAdapter.notifyDataSetChanged();
                    gotoMainIntent();
                    UpDatePostInfo();
                    buttonBookTrip.setVisibility(View.VISIBLE);
                });
                // Обработчик на нажатие НЕТ
                deleteDialog.setNegativeButton("Нет", (dialog, which) -> dialog.cancel());

                // показываем Alert
                deleteDialog.show();
            } else {
                dialogInternetCon.setContentView(R.layout.internet_connecting_dialog);
                dialogInternetCon.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogInternetCon.show();
            }

        });

        buttonBookFinish.setOnClickListener(view -> { // Завершить заявку а не удалить
            if(isOnline(this)) {
                showBookFinishDialog();
            } else {
                dialogInternetCon.setContentView(R.layout.internet_connecting_dialog);
                dialogInternetCon.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogInternetCon.show();
            }

        });


        buttonBackActivity.setOnClickListener(view -> {
            onBackPressed();
            finish();
        });

        imageViewButtonDelete.setOnClickListener(view -> { // Кнопка удалить пост
            if(isOnline(this)) {
                if(booksArrayList.size() > 0 ) {
                    showSnakbarTypeOne(viewSnackbar,"Нельзя удалить поезду пока есть активные заявки");
                } else  {
                    showDialogDeletePost();
                }
            } else  {
                dialogInternetCon.setContentView(R.layout.internet_connecting_dialog);
                dialogInternetCon.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogInternetCon.show();
            }

        });

        imageViewCallButton.setOnClickListener(view -> { // Кнопка ПОЗВОНИТЬ ВОДИТЕЛЮ/ПАСАЖИРУ
            String number = getIntent().getExtras().getString("phone");
            if (ContextCompat.checkSelfPermission(TripDetalActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) TripDetalActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        });


        textViewDriverName.setOnClickListener(view -> {
            if(tripUserId.equals(userKey)) {

            } else {
                dialogUserInfo.setContentView(R.layout.show_user_mini_info);
                dialogUserInfo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                String user_name = tripNameUser.substring(0,1);

                ImageView userImage = dialogUserInfo.findViewById(R.id.userImage);

                TextDrawable user_drawbleSheet = TextDrawable.builder()
                        .beginConfig()
                        .fontSize(26) /* size in px */
                        .bold()
                        .toUpperCase()
                        .endConfig()
                        .buildRoundRect(user_name, colorGenerator.getRandomColor(),4); // radius in
                userImage.setImageDrawable(user_drawbleSheet);

                TextView userName = dialogUserInfo.findViewById(R.id.userName);
                TextView userRating = dialogUserInfo.findViewById(R.id.textViewRating);
                TextView userCount = dialogUserInfo.findViewById(R.id.textViewTripCount);

                UsersRef.collection(USERS_COLLECTION).document(tripUserId).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String user_phone = task.getResult().getString("userPhone");
                        String user_rating = task.getResult().get("userRating").toString();
                        String user_trip_count = task.getResult().get("userTrip").toString();
                        userName.setText(tripNameUser);
                        userRating.setText(user_rating);
                        userCount.setText(user_trip_count);
                    } else {

                    }
                });
                dialogUserInfo.show();
            }

        });
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {  // CВАЙП ДЛЯ УДАЛЕНИЯ ПОЛЬЗОВАТЕЛ
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            String tripUserId = getIntent().getExtras().getString("driverID");
            String postID = getIntent().getExtras().getString("postID");
            int position = viewHolder.getAdapterPosition();
           if(tripUserId.equals(userKey)) {

               bookRef.collection("posts/" + postID + "/books").document(booksArrayList.get(position).getUserID()).delete();
               bookRef.collection("notificat/" + tripUserId + "/books").document(booksArrayList.get(position).getUserID()+postID).delete();
               booksArrayList.remove(position);
               if(booksArrayList.size() == 0) {
                   DocumentReference documentReference = bookRef.collection(POSTS_COLLECTION).document(postID);
                   Map<String, Object> posts = new HashMap<>();
                   posts.put("statusTrip", "show");
                   documentReference.update(posts).addOnCompleteListener(taskPost -> {
                       if(taskPost.isSuccessful()) {
                           booksAdapter.notifyDataSetChanged();
                       } else {
                           showSnakbarTypeOne(viewSnackbar, "Ошибка. Повторите попытку позже");
                       }
                   });

               } else {

               }
           } else {

           }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(TripDetalActivity.this, R.color.colorRed))
                    .addSwipeRightLabel("Удалить")
                    .setSwipeRightLabelColor(ContextCompat.getColor(TripDetalActivity.this, R.color.colorWhite))
                    .addActionIcon(R.drawable.baseline_delete_forever_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };


    private void showBookFinishDialog() {
        String postID = getIntent().getExtras().getString("postID");
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(TripDetalActivity.this);
        // Указываем текст сообщение
        deleteDialog.setMessage("Вы уверены, что хотите завершить поездку?\nВнимание! Если вы завершили поездку, но не совершили ее фактически, это может привести к получению бана.");

        deleteDialog.setPositiveButton("Да", (dialog, which) -> {
            DocumentReference documentReference = postRef.collection(POSTS_COLLECTION).document(postID);
            Map<String, Object> posts = new HashMap<>();
            posts.put("isDriverUser", "Поездка завершена");
            posts.put("statusTrip", "null");

            changeTripStatusNote();

            documentReference.update(posts).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {

                    Intent mainIntent = new Intent(TripDetalActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                    UsersRef.collection(USERS_COLLECTION).document(userKey).get().addOnCompleteListener(task1 -> {
                        if(task1.isSuccessful()) {
                            if(booksArrayList.size() !=0) {
                                int user_rating = Integer.parseInt(String.valueOf(task1.getResult().get("userTrip")));
                                user_rating++;
                                UsersRef.collection(USERS_COLLECTION).document(userKey).update("userTrip", user_rating);
                            }

                        }
                    });
                    showSnakbarTypeOne(viewSnackbar,  "Вы успешно завершили поездку");
                } else {
                    showSnakbarTypeOne(viewSnackbar, "Ошибка. Поездка не завершена");
                }
            });
        });
        // Обработчик на нажатие НЕТ
        deleteDialog.setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());

        // показываем Alert
        deleteDialog.show();
    }

    private void changeTripStatusNote() {
        String tripUserId = getIntent().getExtras().getString("driverID");
        String postID = getIntent().getExtras().getString("postID");
        DocumentReference documentReference = bookRef.collection("notificat/" + tripUserId + "/books").document();
        Map<String, Object> notificat = new HashMap<>();
        notificat.put("statusTrip", "Поездка завершена");

        documentReference.update(notificat).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {

            } else {

            }

        });
    }

    private void UpDatePostInfo() { // ОБНОВИТЬ СТАТУС ПОСТА ЕСЛИ ПОЛЬЗОВАТЕЛЬ НАЖАЛ НА КНОПКУ ОТМЕНИТЬ БРОНЬ
        String postID = getIntent().getExtras().getString("postID");
        String tripSeat = getIntent().getExtras().getString("seatTrip");
        if(booksArrayList.size() + 1 > Integer.valueOf(tripSeat)) {
            DocumentReference documentReference = postRef.collection(POSTS_COLLECTION).document(postID);
            Map<String, Object> posts = new HashMap<>();
            posts.put("statusTrip", "show");
            documentReference.update(posts).addOnCompleteListener(taskPost -> {
                if(taskPost.isSuccessful()) {

                } else {
                    showSnakbarTypeOne(viewSnackbar,  "Ошибка. Повторите попытку позже");
                }
            });
        }
    }

    private void showDialogDeletePost() { // Диалог удаления поста
        String postID = getIntent().getExtras().getString("postID");
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(TripDetalActivity.this);
        // Указываем текст сообщение
        deleteDialog.setMessage("Вы уверены, что хотите удалить заявку?");

        deleteDialog.setPositiveButton("Да", (dialog, which) -> {
            bookRef.collection("posts").document(postID).delete();
            gotoMainIntent();
            finish();
        });
        // Обработчик на нажатие НЕТ
        deleteDialog.setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());

        // показываем Alert
        deleteDialog.show();

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
                    textViewBooksView.setVisibility(View.INVISIBLE);
                    imageViewBooksView.setVisibility(View.INVISIBLE);
                    booksArrayList.add(books);
                    booksAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    private void showBookBtnStatus() {
        // Получаем данные из intent'a
        Bundle extras = getIntent().getExtras();
        String postID = extras.getString("postID");
        String tripUserId = extras.getString("driverID");
        String isUserDriver = extras.getString("isUserDriver");

        // Проверяем, авторизован ли пользователь
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            // Создаем ссылку на документ коллекции books
            DocumentReference bookDocumentRef = bookRef.collection("posts/" + postID + "/books").document(userKey);

            // Получаем данные документа и обрабатываем результат
            bookDocumentRef.get().addOnCompleteListener(task -> {
                // Проверяем, существует ли уже запись о бронировании для данного пользователя
                boolean bookExists = task.getResult().exists();
                // Если запись есть, получаем id пассажира
                String passengerID = bookExists ? task.getResult().getString("userID") : null;

                // Проверяем состояние поездки
                boolean isTripFinished = isUserDriver.equals("Поездка завершена");
                if (isTripFinished) {
                    // Если поездка завершена, скрываем все кнопки и делаем список неактивным
                    textViewTripStatus.setBackgroundResource(R.drawable.tripstatuis_red);
                    setButtonVisibility(false, false);
                    buttonBookFinish.setVisibility(View.INVISIBLE);
                    recyclerViewBook.setClickable(false);
                } else {
                    if(userKey.equals(tripUserId)) {
                        // Если пользователь - водитель поездки, показываем кнопку "Завершить поездку"
                        buttonBookFinish.setVisibility(View.VISIBLE);
                        setButtonVisibility(false, false);
                    } else {
                        if (bookExists) {
                            // Если пользователь уже забронировал место в поездке, показываем кнопку "Отменить бронь"
                            // и скрываем кнопку "Забронировать место"
                            setButtonVisibility(false, true);
                        } else {
                            // Если пользователь еще не забронировал место в поездке
                            bookDocumentRef.get().addOnCompleteListener(task1 -> {
                                boolean bookExists2 = task1.getResult().exists();
                                if (bookExists2) {
                                    // Если другой пассажир уже забронировал место для этой поездки,
                                    // скрываем обе кнопки
                                    setButtonVisibility(false, false);
                                } else {
                                    // Иначе показываем кнопку "Забронировать место"
                                    setButtonVisibility(true, false);
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    // Метод для управления видимостью кнопок "Забронировать место" и "Отменить бронь"
    private void setButtonVisibility(boolean bookTripVisible, boolean cancelVisible) {
        // Если нужно показать кнопку "Забронировать место", делаем ее видимой и скрываем кнопку "Отменить бронь"
        if (bookTripVisible) {
            buttonGetForPassengerRequest();
            buttonBookTrip.setVisibility(View.VISIBLE);
        } else {
            buttonBookTrip.setVisibility(View.GONE);
        }

        // Если нужно показать кнопку "Отменить бронь", делаем ее видимой и скрываем кнопку "Забронировать место"
        if (cancelVisible) {
            buttonBookCancel.setVisibility(View.VISIBLE);
        } else {
            buttonBookCancel.setVisibility(View.GONE);
        }
    }

    private void buttonSetForPassengerRequest() {
        String isUserDriver = getIntent().getExtras().getString("isUserDriver");
        if(isUserDriver.equals("Ищу водителя"))
        {
            buttonBookTrip.setText("Принять заявку");
        } else {
            buttonBookTrip.setText("Забронировать");
        }
    }

    private void buttonGetForPassengerRequest() {
        String isUserDriver = getIntent().getExtras().getString("isUserDriver");
        if(isUserDriver.equals("Ищу водителя"))
        {
            buttonBookCancel.setText("Отменить заявку");
        } else {
            buttonBookCancel.setText("Отменить бронирование");
        }
    }

    private void showDialogCreatPost() {
        String isUserDriver = getIntent().getExtras().getString("isUserDriver");
        dialogCreatPost.setContentView(R.layout.creat_post_dialog);
        dialogCreatPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView textView = dialogCreatPost.findViewById(R.id.textDialog);
        if(isUserDriver.equals("Ищу водителя")) {
            textView.setText("Вы успешно приняли заявку Желаем вам хорошей поездки!");
        } else {
            textView.setText("Вы успешно забронировали поездку Желаем вам хорошей поездки!");
        }
        dialogCreatPost.show();

    }


    private void bookTrip(String user_name, String user_phone, String rating, String count, String postID) {
        Bundle extras = getIntent().getExtras();
        String tripUserId = extras.getString("driverID");
        String tripStart = extras.getString("locationFrom");
        String tripEnd = extras.getString("locationTo");
        String tripDate = extras.getString("date");
        String tripSeat = extras.getString("seatTrip");
        String status = extras.getString("isUserDriver");

        String tripPrice = getIntent().getExtras().getString("price");
        String tripBrandCar = getIntent().getExtras().getString("brandCar");
        String dirverName = getIntent().getExtras().getString("driverName");
        String comments = getIntent().getExtras().getString("commentTrip");
        String isPackBox = getIntent().getExtras().getString("isPackBox");
        String driverPhone = getIntent().getExtras().getString("phone");

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
                book.put("seatTrip", tripSeat);
                book.put("date", tripDate);
                book.put("statusTrip", status);
                book.put("tripPrice", tripPrice);
                book.put("tripBrandCar", tripBrandCar);
                book.put("dirverName", dirverName);
                book.put("comments", comments);
                book.put("isPackBox", isPackBox);
                book.put("driverPhone", driverPhone);
                book.put("userRating", rating);
                book.put("userTripCount", count);
                book.put("rating", "no");
                book.put("timestamp", FieldValue.serverTimestamp());
                buttonBookTrip.setVisibility(View.GONE);
                //bookRef.collection("posts/" + postID + "/books").document(userKey).set(book);
                noteRef.collection("notificat/" + tripUserId +  "/books").document(notifiID).set(book);
                bookRef.collection("posts/" + postID +  "/books").document(userKey).set(book);
                if(booksArrayList.size() + 1 == Integer.valueOf(tripSeat)) {
                    DocumentReference documentReference = postRef.collection(POSTS_COLLECTION).document(postID);
                    Map<String, Object> posts = new HashMap<>();
                    posts.put("statusTrip", "null");
                    documentReference.update(posts).addOnCompleteListener(taskPost -> {
                        if(taskPost.isSuccessful()) {
                            buttonBookCancel.setVisibility(View.VISIBLE);
                        } else {
                            showSnakbarTypeOne(viewSnackbar,  "Ошибка. Повторите попытку позже");
                        }
                    });
                }
                showDialogCreatPost();
                buttonBookTrip.setVisibility(View.GONE);
            } else {
                buttonBookTrip.setVisibility(View.VISIBLE);
            }
        });

    }
}