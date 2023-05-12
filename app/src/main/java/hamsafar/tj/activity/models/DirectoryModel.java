package hamsafar.tj.activity.models;

public class DirectoryModel {
    private String titelDirectooryText;
    private String descpDirectoryText;
    private boolean expandedDirectory;

    public DirectoryModel () {}

    public DirectoryModel(String titelDirectooryText, String descpDirectoryText) {
        this.titelDirectooryText = titelDirectooryText;
        this.descpDirectoryText = String.valueOf(descpDirectoryText);
        this.expandedDirectory = false;
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

    public boolean isExpandedDirectory() {
        return expandedDirectory;
    }

    public void setExpandedDirectory(boolean expandedDirectory) {
        this.expandedDirectory = expandedDirectory;
    }

}
