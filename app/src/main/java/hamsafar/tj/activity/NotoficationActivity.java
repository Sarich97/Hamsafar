package hamsafar.tj.activity;

import static hamsafar.tj.activity.utility.Utility.BOOKS_COLLECTION;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import hamsafar.tj.R;
import hamsafar.tj.activity.adapters.NotificAdapter;
import hamsafar.tj.activity.models.books;

public class NotoficationActivity extends AppCompatActivity {

    private FirebaseFirestore bookRef;
    private FirebaseAuth firebaseAuth;
    private CollectionReference notificatRef;
    private String userKey;
    private ImageView imageViewUNLoadData;
    private TextView textViewDescpNot;

    private RecyclerView recyclerViewBookNotif;
    NotificAdapter notificAdapter;
    ArrayList<books> booksArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notofication);

        firebaseAuth = FirebaseAuth.getInstance();
        bookRef = FirebaseFirestore.getInstance();
        notificatRef = bookRef.collection("notificat");
        userKey = firebaseAuth.getCurrentUser().getUid();

        imageViewUNLoadData = findViewById(R.id.imageViewNotifiivat);
        textViewDescpNot = findViewById(R.id.textViewDescpN);

        recyclerViewBookNotif = findViewById(R.id.booksRecyclerViewNotif);
        //recyclerViewBookNotif.setHasFixedSize(true);
        recyclerViewBookNotif.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        notificAdapter = new NotificAdapter(booksArrayList, this);
        recyclerViewBookNotif.setAdapter(notificAdapter);


        showNotificationList();
    }

    private void showNotificationList() {
        notificatRef.document(userKey).collection(BOOKS_COLLECTION).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                books books = documentSnapshot.toObject(books.class);
                if (books.getPostCreateID().equals(userKey)) {
                    booksArrayList.add(books);
                    notificAdapter.notifyDataSetChanged();
                    imageViewUNLoadData.setVisibility(View.INVISIBLE);
                    textViewDescpNot.setVisibility(View.INVISIBLE);
                } else {

                }
            }
        });
    }
}