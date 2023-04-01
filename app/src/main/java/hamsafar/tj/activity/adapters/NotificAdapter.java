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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import hamsafar.tj.R;
import hamsafar.tj.activity.models.Post;
import hamsafar.tj.activity.models.books;

public class NotificAdapter extends RecyclerView.Adapter<NotificAdapter.ViewHolder> {
    private ArrayList<books> booksItems;
    private Context context;
    private Dialog dialogBooks;
    private static final int REQUEST_CALL = 1;

    private FirebaseFirestore firebaseFirestore, bookRef;


    public NotificAdapter(ArrayList<books> books, Context context) {
        this.booksItems = books;
        this.context = context;
    }


    @NonNull
    @Override
    public NotificAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        bookRef = FirebaseFirestore.getInstance();
        dialogBooks= new Dialog(context);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificAdapter.ViewHolder holder, int position) {
        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
        books books = booksItems.get(position);
        holder.textViewDriverName.setText(books.getUserName());
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);

        String firstName = books.getUserName().substring(0, 1);
        TextDrawable user_drawble = TextDrawable.builder()
                .beginConfig()
                .fontSize(38) /* size in px */
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRoundRect(firstName, colorGenerator.getRandomColor(), 4); // radius in
        holder.imageViewDriverImage.setImageDrawable(user_drawble);
        //SetDate
    }


    @Override
    public int getItemCount() {
        return booksItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        TextView textViewDriverName;
        ImageView imageViewDriverImage;

        public ViewHolder(@NonNull View view) {
            super(view);
            textViewDriverName = view.findViewById(R.id.driverName);
            imageViewDriverImage = view.findViewById(R.id.driverImage);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            books books = booksItems.get(position);

            ColorGenerator colorGenerator = ColorGenerator.MATERIAL;

            dialogBooks.setContentView(R.layout.show_books_detal_sheet);
            dialogBooks.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            String user_name = books.getUserName().substring(0,1);
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
            Button button = dialogBooks.findViewById(R.id.callButtonUser);

            button.setOnClickListener(view12 -> { // Нажимаем на кнопку звонок
                String number =  booksItems.get(position).getUserPhone();
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                } else {
                    String dial = "tel:" + number;
                    context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                }
            });
            userName.setText(books.getUserName());
            startTrip.setText(books.getLocationFrom());
            endTrip.setText(books.getLocationTo());
            dialogBooks.show();
        }
    }
}
