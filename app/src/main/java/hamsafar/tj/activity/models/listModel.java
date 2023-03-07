package hamsafar.tj.activity.models;

public class listModel {
    private int imageList;
    private String textList;

    listModel() {}

    public listModel(int imageList, String textList) {
        this.imageList = imageList;
        this.textList = textList;
    }

    public int getImageList() {
        return imageList;
    }

    public void setImageList(int imageList) {
        this.imageList = imageList;
    }

    public String getTextList() {
        return textList;
    }

    public void setTextList(String textList) {
        this.textList = textList;
    }
}
