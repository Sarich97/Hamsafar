package hamsafar.tj.activity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import hamsafar.tj.R;
import hamsafar.tj.activity.models.listModel;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHold> {
    ArrayList<listModel> listModels;
    private Context context;



    public ListAdapter(ArrayList<listModel> listModel, Context context) {
        this.listModels = listModel;
        this.context = context;
    }

    @NonNull

    @Override
    public ListViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_profile_menu, parent, false);
        return new ListViewHold(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHold holder, int position) {
        listModel listModel = listModels.get(position);
        holder.imageViewList.setImageResource(listModel.getImageList());
        holder.textViewList.setText(listModel.getTextList());
    }

    @Override
    public int getItemCount() {
        return listModels.size();

    }

    public class ListViewHold extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageViewList;
        TextView textViewList;


        public ListViewHold(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            //hooks
            imageViewList = itemView.findViewById(R.id.listImage);
            textViewList = itemView.findViewById(R.id.textViewList);

        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            switch (clickedPosition) {
                case 0:

                    break;

                default:
            }
        }
    }


}
