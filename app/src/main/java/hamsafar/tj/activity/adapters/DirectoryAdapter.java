package hamsafar.tj.activity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import hamsafar.tj.R;
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

        boolean isExpanded = directoryModels.get(position).isExpandedDirectory();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return directoryModels.size();
    }

    public class DirectoryVH extends RecyclerView.ViewHolder {

        private static final String TAG = "MovieVH";

        ConstraintLayout expandableLayout;
        TextView titleTextDirect, descpTextDirect;

        public DirectoryVH(@NonNull View itemView) {
            super(itemView);

            titleTextDirect = itemView.findViewById(R.id.titleTextDirectory);
            descpTextDirect = itemView.findViewById(R.id.descpTextDirectory);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);

            titleTextDirect.setOnClickListener(view -> {
                DirectoryModel directoryModel = directoryModels.get(getAdapterPosition());
                directoryModel.setExpandedDirectory(!directoryModel.isExpandedDirectory());
                notifyItemChanged(getAdapterPosition());

            });

        }
    }
}
