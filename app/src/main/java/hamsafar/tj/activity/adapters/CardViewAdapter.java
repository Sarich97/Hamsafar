package hamsafar.tj.activity.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import hamsafar.tj.R;
import hamsafar.tj.activity.models.CardViewModel;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.CardViewHold> {
    ArrayList<CardViewModel> cardViewModels;
    private Context context;



    public CardViewAdapter(ArrayList<CardViewModel> cardViewModels, Context context) {
        this.cardViewModels = cardViewModels;
        this.context = context;
    }

    @NonNull

    @Override
    public CardViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_main, parent, false);
        return new CardViewHold(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHold holder, int position) {
        CardViewModel cardHelper = cardViewModels.get(position);
        holder.image.setImageResource(cardHelper.getImageCard());
        holder.title.setText(cardHelper.getTitleCard());
        holder.relativeLayout.setBackground(cardHelper.getColorCard());
    }

    @Override
    public int getItemCount() {
        return cardViewModels.size();

    }

    public class CardViewHold extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        TextView title;
        RelativeLayout relativeLayout;


        public CardViewHold(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            //hooks
            image = itemView.findViewById(R.id.cardImage);
            title = itemView.findViewById(R.id.titleCard);
            relativeLayout = itemView.findViewById(R.id.cardColor);

        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            switch (clickedPosition) {
                case 0:
//                        Intent intent = new Intent(context, CreatPostActivity.class);
//                        context.startActivity(intent);
                    break;

                default:
            }
        }
    }


}
