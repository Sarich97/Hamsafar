package hamsafar.tj.activity.fragments;


import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import hamsafar.tj.R;
import hamsafar.tj.activity.adapters.PostAdapter;
import hamsafar.tj.activity.models.Post;


public class MyPostFragment extends Fragment {

    // Инициализация FireBase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore travelPostRef;

    // Элементы пользовательского интерфейса
    private RecyclerView recyclerViewPost;
    private PostAdapter postAdapter;
    private ArrayList<Post> posts = new ArrayList<>();
    private ImageView imageViewNotPost;
    private TextView textViewDescpProfile;

    // Идентификатор пользователя
    private String userKey;

    // Диалог для отображения ошибки подключения к Интернету
    private Dialog dialogInternetCon;

    // Константы для работы с FireBase
    private static final String POSTS_COLLECTION = "posts";
    private static final String USER_ID_FIELD = "userUD";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Загрузка макета фрагмента
        View view = inflater.inflate(R.layout.fragment_my_post, container, false);

        // Инициализация элементов пользовательского интерфейса
        recyclerViewPost = view.findViewById(R.id.recyclerViewMyPosts);
        recyclerViewPost.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        postAdapter = new PostAdapter(posts, getContext());
        recyclerViewPost.setAdapter(postAdapter);

        imageViewNotPost = view.findViewById(R.id.imageViewNotifiivat);
        textViewDescpProfile = view.findViewById(R.id.textViewDescpP);

        // Вынесены константы в отдельный класс Constants

        // Инициализация FireBase
        travelPostRef = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userKey = firebaseAuth.getCurrentUser().getUid();

        // Инициализация диалогового окна для отображения ошибки подключения к Интернету
        dialogInternetCon = new Dialog(getContext());

        // Загрузка постов пользователя
        showPostForUsers();

        return view;
    }

    // Метод для загрузки постов пользователя
    private void showPostForUsers() {
        // Ограничение количества постов
        final int postLimit = 20;

        Query query = travelPostRef.collection(POSTS_COLLECTION)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .whereEqualTo(USER_ID_FIELD, userKey)
                .limit(postLimit);

        query.addSnapshotListener((documentSnapshots, e) -> {
            if (e != null) {
                return;
            }

            // Перебор полученных постов
            List<Post> newPosts = new ArrayList<>();
            for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                if (doc.getType() == DocumentChange.Type.ADDED) {
                    Post post = doc.getDocument().toObject(Post.class);
                    newPosts.add(post);
                }
            }

            // Добавление новых постов в общий список постов и обновление адаптера
            posts.addAll(newPosts);
            postAdapter.notifyDataSetChanged();

            // Скрытие элементов уведомления, если имеются посты
            if (!posts.isEmpty()) {
                imageViewNotPost.setVisibility(View.INVISIBLE);
                textViewDescpProfile.setVisibility(View.INVISIBLE);
            }
        });
    }

//    // Метод для отображения диалогового окна при ошибке подключения к Интернету
//    private void showDialogInternetCon() {
//        if (dialogInternetCon.isShowing()) {
//            return;
//        }
//
//        dialogInternetCon.setContentView(R.layout.dialog_internet_con);
//        dialogInternetCon.setCancelable(false);
//
//        Button buttonTryAgain = dialogInternetCon.findViewById(R.id.buttonTryAgain);
//        buttonTryAgain.setOnClickListener(v -> {
//            dialogInternetCon.dismiss();
//            showPostForUsers();
//        });
//
//        dialogInternetCon.show();
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        postAdapter = null;

        // Удаление слушателя при уничтожении фрагмента
        Query query = travelPostRef.collection(POSTS_COLLECTION)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .whereEqualTo(USER_ID_FIELD, userKey)
                .limit(20);

        query.addSnapshotListener((documentSnapshots, e) -> {
        }).remove();
    }
}