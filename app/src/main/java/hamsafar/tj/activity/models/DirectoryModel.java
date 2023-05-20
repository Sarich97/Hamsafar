package hamsafar.tj.activity.models;

import android.graphics.drawable.GradientDrawable;

public class DirectoryModel {
    private String titelDirectooryText;
    private String descpDirectoryText;
    GradientDrawable expandedDirectory;


    public DirectoryModel() {}

    public DirectoryModel(String titelDirectooryText, String descpDirectoryText, GradientDrawable expandedDirectory) {
        this.titelDirectooryText = titelDirectooryText;
        this.descpDirectoryText = descpDirectoryText;
        this.expandedDirectory = expandedDirectory;
    }

    public String getTitelDirectooryText() {
        return titelDirectooryText;
    }

    public void setTitelDirectooryText(String titelDirectooryText) {
        this.titelDirectooryText = titelDirectooryText;
    }

    public String getDescpDirectoryText() {
        return descpDirectoryText;
    }

    public void setDescpDirectoryText(String descpDirectoryText) {
        this.descpDirectoryText = descpDirectoryText;
    }

    public GradientDrawable getExpandedDirectory() {
        return expandedDirectory;
    }

    public void setExpandedDirectory(GradientDrawable expandedDirectory) {
        this.expandedDirectory = expandedDirectory;
    }
}
