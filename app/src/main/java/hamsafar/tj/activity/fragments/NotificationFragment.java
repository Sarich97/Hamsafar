package hamsafar.tj.activity.fragments;

import static hamsafar.tj.activity.utility.Utility.isOnline;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import hamsafar.tj.R;
import hamsafar.tj.activity.adapters.NotificAdapter;
import hamsafar.tj.activity.models.Post;
import hamsafar.tj.activity.models.books;

public class NotificationFragment extends Fragment {

    private FirebaseFirestore bookRef;
    private FirebaseAuth firebaseAuth;
    private CollectionReference notificatRef;
    private String userKey;
    private ImageView imageViewUNLoadData;
    private TextView textViewDescpNot;

    private RecyclerView recyclerViewBookNotif;
    NotificAdapter notificAdapter;
    ArrayList<books> booksArrayList = new ArrayList<>();
    private Dialog dialogInternetCon;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_notification, container, false);


        dialogInternetCon = new Dialog(getContext());

        firebaseAuth = FirebaseAuth.getInstance();
        bookRef = FirebaseFirestore.getInstance();
        notificatRef = bookRef.collection("notificat");
        userKey = firebaseAuth.getCurrentUser().getUid();

        imageViewUNLoadData = view.findViewById(R.id.imageViewNotifiivat);
        textViewDescpNot = view.findViewById(R.id.textViewDescpN);

        recyclerViewBookNotif = view.findViewById(R.id.booksRecyclerViewNotif);
        //recyclerViewBookNotif.setHasFixedSize(true);
        recyclerViewBookNotif.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        notificAdapter = new NotificAdapter(booksArrayList, getContext());
        recyclerViewBookNotif.setAdapter(notificAdapter);


        showNotificationList();

        return view;
    }


    private void showNotificationList() {
       notificatRef.document(userKey).collection("books").get().addOnSuccessListener(queryDocumentSnapshots -> {
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