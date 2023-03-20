package hamsafar.tj.activity.adapters;

import static hamsafar.tj.activity.utility.Utility.showToast;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import hamsafar.tj.R;
import hamsafar.tj.activity.models.books;

public class NotificAdapter extends RecyclerView.Adapter<NotificAdapter.ViewHolder> {
    private ArrayList<books> booksItems;
    private Context context;

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

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificAdapter.ViewHolder holder, int position) {
        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
        books books = booksItems.get(position);
        holder.textViewStartTrip.setText(books.getLocationFrom());
        holder.textViewEndTrip.setText(books.getLocationTo());
        holder.textViewDateTime.setText(books.getDate());
        holder.textViewDriverName.setText(books.getUserName());


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

    public class ViewHolder extends RecyclerView.ViewHolder  {

        TextView textViewStartTrip, textViewEndTrip, textViewDriverName, textViewDateTime;
        ImageView imageViewDriverImage;

        public ViewHolder(@NonNull View view) {
            super(view);
            textViewStartTrip = view.findViewById(R.id.start_of_route);
            textViewEndTrip = view.findViewById(R.id.end_of_route);
            textViewDriverName = view.findViewById(R.id.driverName);
            imageViewDriverImage = view.findViewById(R.id.driverImage);
            textViewDateTime = view.findViewById(R.id.dateTimeTrip);
        }

    }
}
