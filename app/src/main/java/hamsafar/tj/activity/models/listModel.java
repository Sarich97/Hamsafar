package hamsafar.tj.activity.models;

import java.util.ArrayList;

public class listModel {
    int listImage;
    String textViewList;

   listModel(ArrayList<listModel> listModels) {

   }

    public listModel(int listImage, String textViewList) {
        this.listImage = listImage;
        this.textViewList = textViewList;
    }

    public int getListImage() {
        return listImage;
    }

    public void setListImage(int listImage) {
        this.listImage = listImage;
    }

    public String getTextViewList() {
        return textViewList;
    }

    public void setTextViewList(String textViewList) {
        this.textViewList = textViewList;
    }
}
