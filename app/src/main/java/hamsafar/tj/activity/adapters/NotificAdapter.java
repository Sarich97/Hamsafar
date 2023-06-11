package hamsafar.tj.activity.adapters;



import android.content.Context;
import android.content.Intent;


import android.graphics.Typeface;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import hamsafar.tj.R;
import hamsafar.tj.activity.TripDetalActivity;

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

        if(books.getStatusTrip().equals("Ищу водителя")) {
            if(books.getNotifiStatus().equals("show")) {
                holder.textViewDriverName.setText(books.getUserName() + " принял(а) вашу заявку");
                holder.textViewDriverName.setTypeface(Typeface.DEFAULT_BOLD);
            } else {
                holder.textViewDriverName.setText(books.getUserName() + " принял(а) вашу заявку");
            }
        } else if(books.getStatusTrip().equals("Ищу пассажиров")) {
            if(books.getNotifiStatus().equals("show")) {
                holder.textViewDriverName.setText(books.getUserName() + " забронировал(а) поездку");
                holder.textViewDriverName.setTypeface(Typeface.DEFAULT_BOLD);

            } else {
                holder.textViewDriverName.setText(books.getUserName() + " забронировал(а) поездку");
            }
        }
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
            String postId = books.getPostID();

            DocumentReference postRef = bookRef.collection("posts").document(postId);

            postRef.get().addOnSuccessListener(documentSnapshot -> {


                String user_UD = documentSnapshot.getString("userUD");
                String user_name = documentSnapshot.getString("userName");
                String user_phone = documentSnapshot.getString("userPhone");
                String user_car = documentSnapshot.getString("carModel");
                String start_trip = documentSnapshot.getString("startTrip");
                String end_trip = documentSnapshot.getString("endTrip");
                String data_trip = documentSnapshot.getString("dataTrip");
                String time_trip = documentSnapshot.getString("timeTrip");
                String price_trip = documentSnapshot.getString("priceTrip");
                String seat_trip = documentSnapshot.getString("seatTrip");
                String comments = documentSnapshot.getString("commentTrip");
                String is_driver_user = documentSnapshot.getString("isDriverUser");
                String status_trip = documentSnapshot.getString("statusTrip");
                String package_trip = documentSnapshot.getString("isPackage");
                String post_id = documentSnapshot.getString("postId");


                Intent postIntent = new Intent(context, TripDetalActivity.class);
                postIntent.putExtra("locationFrom",start_trip);
                postIntent.putExtra("locationTo",end_trip);
                postIntent.putExtra("date",data_trip + " в " + time_trip);
                postIntent.putExtra("price",price_trip);
                postIntent.putExtra("seatTrip",seat_trip);
                postIntent.putExtra("brandCar",user_car);
                postIntent.putExtra("driverName",user_name);
                postIntent.putExtra("driverID",user_UD);
                postIntent.putExtra("phone",user_phone);
                postIntent.putExtra("isUserDriver",is_driver_user);
                postIntent.putExtra("commentTrip",comments);
                postIntent.putExtra("postID",post_id);
                postIntent.putExtra("isPackBox",package_trip);
                context.startActivity(postIntent);
            });

        }
    }
}
