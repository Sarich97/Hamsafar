package hamsafar.tj.activity.adapters;

import static hamsafar.tj.activity.utility.Utility.CONFIG_COLLECTION;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import hamsafar.tj.R;
import hamsafar.tj.activity.HelpActivity;
import hamsafar.tj.activity.SecurityStories;
import hamsafar.tj.activity.models.CardViewModel;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.CardViewHold> {
    ArrayList<CardViewModel> cardViewModels;
    private Context context;

    private FirebaseFirestore ConfigReg;


    public CardViewAdapter(ArrayList<CardViewModel> cardViewModels, Context context) {
        this.cardViewModels = cardViewModels;
        this.context = context;
    }

    @NonNull

    @Override
    public CardViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view2, parent, false);

        ConfigReg = FirebaseFirestore.getInstance();
        return new CardViewHold(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHold holder, int position) {
        CardViewModel cardHelper = cardViewModels.get(position);
        holder.imageViewCard.setImageResource(cardHelper.getImageCard());
        holder.textViewTitleCard.setText(cardHelper.getTitleCard());
        holder.relativeLayoutCard.setBackground(cardHelper.getColorCard());
    }

    @Override
    public int getItemCount() {
        return cardViewModels.size();

    }

    public class CardViewHold extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageViewCard;
        TextView textViewTitleCard;
        ConstraintLayout relativeLayoutCard;


        public CardViewHold(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            //hooks
            imageViewCard = itemView.findViewById(R.id.cardImage);
            textViewTitleCard = itemView.findViewById(R.id.titleCard);
            relativeLayoutCard = itemView.findViewById(R.id.cardColor);

        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            switch (clickedPosition) {
                case 0:
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(itemView.getContext(), R.style.BottomSheetDialogTheme);
                    bottomSheetDialog.setContentView(R.layout.road_condition_sheets);
                    bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

                    TextView textViewStatus = bottomSheetDialog.findViewById(R.id.textSheetRoad);
                    TextView textViewComment = bottomSheetDialog.findViewById(R.id.textCommentView);

                    ConfigReg.collection(CONFIG_COLLECTION).document("road").get().addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            String textStatus = task.getResult().getString("status");
                            String textComment = task.getResult().getString("comment");

                            textViewStatus.setText("Автотрасса Душанбе — Худжанд:  " + textStatus);
                            textViewComment.setText(textComment);
                        }
                    });

                    bottomSheetDialog.show();
                    break;
                case 1:
                    Intent stories = new Intent(v.getContext(), SecurityStories.class);
                    v.getContext().startActivity(stories);
                    break;

                case 2:
                    String url = "https://avia.havopajmo.ru/";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);
                    break;

                case 3:
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/hamsafar_t"));
                    context.startActivity(intent);
                    break;

                default:
            }
        }
    }


}
