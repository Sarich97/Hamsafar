package hamsafar.tj.activity.adapters;

import static hamsafar.tj.activity.utility.Utility.showToast;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import hamsafar.tj.R;
import hamsafar.tj.activity.TripDetalActivity;
import hamsafar.tj.activity.models.books;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {
    private ArrayList<books> booksArrayList;
    private Context context;
    private FirebaseFirestore bookRef;
    private FirebaseAuth firebaseAuth;
    private String userKey;
    private Dialog dialogBooks;
    private static final int REQUEST_CALL = 1;

    public BooksAdapter(ArrayList<books> books, Context context) {
        this.booksArrayList = books;
        this.context = context;
    }

    @NonNull
    @Override
    public BooksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.books_item, parent, false);

        bookRef = FirebaseFirestore.getInstance();
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull BooksAdapter.ViewHolder holder, int position) {

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);


        firebaseAuth = FirebaseAuth.getInstance();
        userKey = firebaseAuth.getCurrentUser().getUid();
        dialogBooks= new Dialog(context);
        books books = booksArrayList.get(position);
        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
        holder.textViewUserNameBook.setText(books.getUserName());

        if(userKey.equals(books.getPostCreateID())) {
            holder.imageDetalBooks.setVisibility(View.VISIBLE);
            holder.buttonDeleteUsers.setVisibility(View.VISIBLE);
        } else {
            holder.imageDetalBooks.setVisibility(View.GONE);
            holder.buttonDeleteUsers.setVisibility(View.GONE);
        }

        String userNameName = books.getUserName().substring(0,1);

        TextDrawable user_drawble = TextDrawable.builder()
                .beginConfig()
                .fontSize(26) /* size in px */
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRoundRect(userNameName, colorGenerator.getRandomColor(),4); // radius in
        holder.imageViewUserBook.setImageDrawable(user_drawble);

        final String postId = books.getPostID();
        final String booksId = booksArrayList.get(position).getUserID();

        holder.buttonDeleteUsers.setOnClickListener(view1 -> {  // Кнопка удалить пользователя из списка пассажиров/водитель
            AlertDialog.Builder deleteDialog = new AlertDialog.Builder(context);
            deleteDialog.setTitle("Подтвердить удаление...");
            // Указываем текст сообщение
            deleteDialog.setMessage("Вы уверены, что хотите удалить ?");

            deleteDialog.setPositiveButton("Да", (dialog, which) -> {
                notifyDataSetChanged();
                booksArrayList.remove(booksArrayList.get(position));
                bookRef.collection("posts/" + postId + "/books").document(booksId).delete();
                bookRef.collection("notificat/" + books.getPostCreateID() + "/books").document(booksId+postId).delete();
                if(booksArrayList.size() == 0) {
                    DocumentReference documentReference = bookRef.collection("posts").document(postId);
                    Map<String, Object> posts = new HashMap<>();
                    posts.put("statusTrip", "show");
                    documentReference.update(posts).addOnCompleteListener(taskPost -> {
                        if(taskPost.isSuccessful()) {

                        } else {
                            showToast(context, "Ошибка. Повторите попытку позже");
                        }
                    });
                } else {

                }

            });
            // Обработчик на нажатие НЕТ
            deleteDialog.setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());

            // показываем Alert
            deleteDialog.show();
        });

        holder.imageDetalBooks.setOnClickListener(view -> { // ОКОШКО С ПОЛЬЗОВАТЕЛЕМ ДЛЯ ЗВОНКА
            dialogBooks.setContentView(R.layout.show_books_detal_sheet);
            dialogBooks.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            String user_name = booksArrayList.get(position).getUserName().substring(0,1);
            ImageView userImage = dialogBooks.findViewById(R.id.userImageBooks);
            TextDrawable user_drawbleSheet = TextDrawable.builder()
                    .beginConfig()
                    .fontSize(26) /* size in px */
                    .bold()
                    .toUpperCase()
                    .endConfig()
                    .buildRoundRect(user_name, colorGenerator.getRandomColor(),4); // radius in
            userImage.setImageDrawable(user_drawbleSheet);


            TextView userName = dialogBooks.findViewById(R.id.userNameBook);
            TextView startTrip = dialogBooks.findViewById(R.id.start_of_route);
            TextView endTrip = dialogBooks.findViewById(R.id.end_of_route);
            Button buttonCall = dialogBooks.findViewById(R.id.callButtonUser);
            buttonCall.setOnClickListener(view12 -> { // Нажимаем на кнопку звонок
                String number =  booksArrayList.get(position).getUserPhone();
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                } else {
                    String dial = "tel:" + number;
                    context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                }
            });
            userName.setText(booksArrayList.get(position).getUserName());
            startTrip.setText(booksArrayList.get(position).getLocationFrom());
            endTrip.setText(booksArrayList.get(position).getLocationTo());
            dialogBooks.show();
        });
    }



    @Override
    public int getItemCount() {
        return booksArrayList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewUserBook, imageDetalBooks;
        TextView textViewUserNameBook;
        Button buttonDeleteUsers;

        public ViewHolder(View view) {
            super(view);
            imageViewUserBook = view.findViewById(R.id.userImageBooks);
            textViewUserNameBook = view.findViewById(R.id.userNameBook);
            imageDetalBooks = view.findViewById(R.id.imageViewDetalBook);
            buttonDeleteUsers = view.findViewById(R.id.buttonDeleteUser);
//            buttonDeleteUser  = view.findViewById(R.id.buttonDelete);


            //tv_date = view.findViewById(R.id.comment_date);
        }

    }
}
