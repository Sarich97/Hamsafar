package hamsafar.tj.activity.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;



import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import hamsafar.tj.R;
import hamsafar.tj.activity.EditProfileActivity;
import hamsafar.tj.activity.MyPostActivity;
import hamsafar.tj.activity.MyTripActivity;
import hamsafar.tj.activity.models.listModel;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHold> {
        ArrayList<listModel> listModels;
        private Context context;



public ListAdapter(ArrayList<listModel> listModels, Context context) {
            this.listModels = listModels;
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
        listModel cardHelper = listModels.get(position);
        holder.imageViewCard.setImageResource(cardHelper.getListImage());
        holder.textViewTitleCard.setText(cardHelper.getTextViewList());
}

@Override
public int getItemCount() {
        return listModels.size();

        }

public class ListViewHold extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView imageViewCard;
    TextView textViewTitleCard;


    public ListViewHold(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        //hooks
        imageViewCard = itemView.findViewById(R.id.listImage);
        textViewTitleCard = itemView.findViewById(R.id.textViewList);

    }

    @Override
    public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            switch (clickedPosition) {
                case 0:
                    Intent intentMyPost = new Intent(context, MyPostActivity.class);
                    context.startActivity(intentMyPost);
                    break;

                case 1:
                    Intent tripIntent = new Intent(context, MyTripActivity.class);
                    context.startActivity(tripIntent);
                    break;
                case 2:
                    Intent editIntent = new Intent(context, EditProfileActivity.class);
                    context.startActivity(editIntent);
                    break;
                case 3:
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
                    bottomSheetDialog.setContentView(R.layout.terms_of_use_sheet);
                    bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    bottomSheetDialog.show();
                    break;
                case 4:

                    break;

                default:
            }
        }
    }


}
