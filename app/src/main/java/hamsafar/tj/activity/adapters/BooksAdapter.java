package hamsafar.tj.activity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import hamsafar.tj.R;
import hamsafar.tj.activity.models.books;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {
    private static final String TAG = "MyActivity";
    private ArrayList<books> booksArrayList;
    private Context context;
    private FirebaseFirestore firebaseFirestore;

    public BooksAdapter(ArrayList<books> books, Context context) {
        this.booksArrayList = books;
        this.context = context;
    }

    @NonNull
    @Override
    public BooksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.books_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull BooksAdapter.ViewHolder holder, int position) {

        books books = booksArrayList.get(position);
        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
        holder.user_name.setText(books.getUserName());

        String userNameName = books.getUserName().substring(0,1);

        TextDrawable user_drawble = TextDrawable.builder()
                .beginConfig()
                .fontSize(26) /* size in px */
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRoundRect(userNameName, colorGenerator.getRandomColor(),4); // radius in
        holder.img_user.setImageDrawable(user_drawble);
    }

    @Override
    public int getItemCount() {
        return booksArrayList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img_user;
        TextView user_name;

        public ViewHolder(View view) {
            super(view);
            img_user = view.findViewById(R.id.userImageBooks);
            user_name = view.findViewById(R.id.userNameBook);

            //tv_date = view.findViewById(R.id.comment_date);
        }

    }
}
