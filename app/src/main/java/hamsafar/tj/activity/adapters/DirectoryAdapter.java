package hamsafar.tj.activity.adapters;

import static hamsafar.tj.activity.utility.Utility.CONFIG_COLLECTION;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import hamsafar.tj.R;
import hamsafar.tj.activity.HelpDetalActivity;
import hamsafar.tj.activity.SecurityStories;
import hamsafar.tj.activity.models.DirectoryModel;

public class DirectoryAdapter extends RecyclerView.Adapter<DirectoryAdapter.DirectoryVH> {

    Context mContext;
    ArrayList<DirectoryModel> directoryModels;

    public DirectoryAdapter(Context mContext, ArrayList<DirectoryModel> directoryModels) {
        this.mContext = mContext;
        this.directoryModels = directoryModels;
    }

    @NonNull
    @Override
    public DirectoryAdapter.DirectoryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.help_items, parent, false);
        return new DirectoryVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DirectoryVH holder, int position) {
        DirectoryModel movie = directoryModels.get(position);
        holder.titleTextDirect.setText(movie.getTitelDirectooryText());
        holder.descpTextDirect.setText(movie.getDescpDirectoryText());
        holder.expandableLayout.setBackground(movie.getExpandedDirectory());

    }

    @Override
    public int getItemCount() {
        return directoryModels.size();
    }

    public class DirectoryVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final String TAG = "MovieVH";

        ConstraintLayout expandableLayout;
        TextView titleTextDirect, descpTextDirect;

        public DirectoryVH(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            titleTextDirect = itemView.findViewById(R.id.titleCard);
            descpTextDirect = itemView.findViewById(R.id.textDescpCard);
            expandableLayout = itemView.findViewById(R.id.cardColor);


        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            Intent postDetailActivity = new Intent(mContext, HelpDetalActivity.class);
            postDetailActivity.putExtra("dirTitle",directoryModels.get(clickedPosition).getTitelDirectooryText());
            postDetailActivity.putExtra("dirDesc",directoryModels.get(clickedPosition).getDescpDirectoryText());
            mContext.startActivity(postDetailActivity);
        }
    }
}
