package hamsafar.tj.activity.models;

public class books {
    private String postID, userID, userName, userPhone, postCreateID;

    public books () {}

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getPostCreateID() {
        return postCreateID;
    }

    public void setPostCreateID(String postCreateID) {
        this.postCreateID = postCreateID;
    }

    public books(String postID, String userID, String userName, String userPhone, String postCreateID) {
        this.postID = postID;
        this.userID = userID;
        this.userName = userName;
        this.userPhone = userPhone;
        this.postCreateID = postCreateID;
    }
}
