package hamsafar.tj.activity.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import hamsafar.tj.activity.TripDetalActivity;
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
        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
        Post posts = postsItems.get(position);
        holder.startTrip.setText(posts.getStartTrip());
        holder.endTrip.setText(posts.getEndTrip());
        holder.dateTime.setText(posts.getDataTrip() + " в " + posts.getTimeTrip());
        holder.statusTrip.setText(posts.getIsDriverUser());
        holder.driverName.setText(posts.getUserName());

        if(posts.getPriceTrip() == null) {
            holder.priceTrip.setText("Договорная");
        } else {
            holder.priceTrip.setText(posts.getPriceTrip() + " cомони");
        }

        String firstName = posts.getUserName().substring(0,1);
        TextDrawable user_drawble = TextDrawable.builder()
                .beginConfig()
                .fontSize(38) /* size in px */
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRoundRect(firstName, colorGenerator.getRandomColor(),4); // radius in
        holder.driverImagePost.setImageDrawable(user_drawble);
        //SetDate

        //get user id and retrieve user image stored in Users Collection

    }

    @Override
    public int getItemCount() {
        return postsItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView startTrip, endTrip, priceTrip,driverName, dateTime, statusTrip;
        ImageView driverImagePost;

        public ViewHolder(@NonNull View view) {
            super(view);
            startTrip = view.findViewById(R.id.start_of_route);
            endTrip = view.findViewById(R.id.end_of_route);
            priceTrip = view.findViewById(R.id.trip_Price);
            driverName = view.findViewById(R.id.driverName);
            driverImagePost = view.findViewById(R.id.driverImage);
            dateTime = view.findViewById(R.id.dateTimeTrip);
            statusTrip = view.findViewById(R.id.statusTravel);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Post postAdapter = postsItems.get(position);

            Intent postIntent = new Intent(context, TripDetalActivity.class);
            postIntent.putExtra("locationFrom",postAdapter.getStartTrip());
            postIntent.putExtra("locationTo",postAdapter.getEndTrip());
            postIntent.putExtra("date",postAdapter.getDataTrip() + " в " + postAdapter.getTimeTrip());
            postIntent.putExtra("price",postAdapter.getPriceTrip());
            postIntent.putExtra("seatTrip",postAdapter.getSeatTrip());
            postIntent.putExtra("brandCar",postAdapter.getCarModel());
            postIntent.putExtra("driverName",postAdapter.getUserName());
            postIntent.putExtra("driverID",postAdapter.getUserUD());
            postIntent.putExtra("phone",postAdapter.getUserPhone());
            postIntent.putExtra("isUserDriver",postAdapter.getIsDriverUser());
            postIntent.putExtra("commentTrip",postAdapter.getCommentTrip());
            postIntent.putExtra("postID",postAdapter.getPostId());
            context.startActivity(postIntent);

        }

    }


}