package hamsafar.tj.activity.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import hamsafar.tj.R;
import hamsafar.tj.activity.models.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{
    private ArrayList<Post> postsItems;
    private Context context;

    private String user_id;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;

    public PostAdapter(ArrayList<Post> posts, Context context) {
        this.postsItems = posts;
        this.context = context;
    }



    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);

        firebaseFirestore = FirebaseFirestore.getInstance();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        Post posts = postsItems.get(position);
        holder.startTrip.setText(posts.getStartTrip());
        holder.endTrip.setText(posts.getEndTrip());
        holder.priceTrip.setText(posts.getPriceTrip() + " TJK");

        //SetDate

        //get user id and retrieve user image stored in Users Collection

    }

    @Override
    public int getItemCount() {
        return postsItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView startTrip, endTrip, priceTrip;

        public ViewHolder(@NonNull View view) {
            super(view);
            startTrip = view.findViewById(R.id.start_of_route);
            endTrip = view.findViewById(R.id.end_of_route);
            priceTrip = view.findViewById(R.id.trip_Price);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Post postAdapter = postsItems.get(position);

//            Intent postIntent = new Intent(context, DetalpostActivity.class);
//            postIntent.putExtra("locationFrom",postAdapter.getLocationFrom());
//            postIntent.putExtra("locationTo",postAdapter.getLocationTo());
//            postIntent.putExtra("data",postAdapter.getDate() + " " + postAdapter.getTime());
//            postIntent.putExtra("price",postAdapter.getPrice());
//            postIntent.putExtra("place",postAdapter.getPlace());
//            postIntent.putExtra("brandCar",postAdapter.getUserBrandCar());
//            postIntent.putExtra("userName",postAdapter.getUserName());
//            postIntent.putExtra("userID",postAdapter.getUserid());
//            context.startActivity(postIntent);

        }

    }


}