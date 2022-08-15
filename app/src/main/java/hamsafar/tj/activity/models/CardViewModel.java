package hamsafar.tj.activity.models;

import android.graphics.drawable.GradientDrawable;

public class CardViewModel {

    int imageCard;
    String titleCard;
    GradientDrawable colorCard;

    public CardViewModel(int imageCard, String titleCard, GradientDrawable colorCard) {
        this.imageCard = imageCard;
        this.titleCard = titleCard;
        this.colorCard = colorCard;
    }

    public int getImageCard() {
        return imageCard;
    }

    public String getTitleCard() {
        return titleCard;
    }

    public GradientDrawable getColorCard() {
        return colorCard;
    }
}
