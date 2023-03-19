package hamsafar.tj.activity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import hamsafar.tj.R;
import hamsafar.tj.activity.models.books;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {
    private ArrayList<books> booksArrayList;
    private Context context;
    private FirebaseFirestore bookRef;
    private FirebaseAuth firebaseAuth;
    private String userKey;

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




        firebaseAuth = FirebaseAuth.getInstance();
        userKey = firebaseAuth.getCurrentUser().getUid();
        books books = booksArrayList.get(position);
        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
        holder.textViewUserNameBook.setText(books.getUserName());

        if(userKey.equals(books.getPostCreateID())) {
            holder.buttonCallUser.setVisibility(View.VISIBLE);
            holder.buttonDeleteUser.setVisibility(View.VISIBLE);
        } else {
            holder.buttonCallUser.setVisibility(View.GONE);
            holder.buttonDeleteUser.setVisibility(View.GONE);
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
        holder.buttonDeleteUser.setOnClickListener(view1 -> {
            notifyDataSetChanged();
            booksArrayList.remove(booksArrayList.get(position));
            bookRef.collection("posts/" + postId + "/books").document(booksId).delete();
        });
    }

    @Override
    public int getItemCount() {
        return booksArrayList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewUserBook;
        TextView textViewUserNameBook;
        Button buttonCallUser, buttonDeleteUser;

        public ViewHolder(View view) {
            super(view);
            imageViewUserBook = view.findViewById(R.id.userImageBooks);
            textViewUserNameBook = view.findViewById(R.id.userNameBook);
            buttonCallUser = view.findViewById(R.id.buttonCall);
            buttonDeleteUser  = view.findViewById(R.id.buttonDelete);


            //tv_date = view.findViewById(R.id.comment_date);
        }

    }
}
