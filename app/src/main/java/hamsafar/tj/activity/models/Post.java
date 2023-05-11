package hamsafar.tj.activity.models;

public class Post {
    private String postId;
    private String userUD;
    private String userName;
    private String userPhone;
    private String startTrip;
    private String endTrip;
    private String dataTrip;
    private String timeTrip;
    private String priceTrip;
    private String seatTrip;
    private String carModel;
    private String commentTrip;
    private String isPackage;
    private String isDriverUser;
    private String statusTrip;


    public Post() {}




    public Post(String postId, String userUD, String userName, String userPhone, String startTrip, String endTrip, String dataTrip, String timeTrip, String priceTrip, String seatTrip, String carModel, String commentTrip, String isPackage, String isDriverUser, String statusTrip) {
        this.postId = postId;
        this.userUD = userUD;
        this.userName = userName;
        this.userPhone = userPhone;
        this.startTrip = startTrip;
        this.endTrip = endTrip;
        this.dataTrip = dataTrip;
        this.timeTrip = timeTrip;
        this.priceTrip = priceTrip;
        this.seatTrip = seatTrip;
        this.carModel = carModel;
        this.commentTrip = commentTrip;
        this.isPackage = isPackage;
        this.isDriverUser = isDriverUser;
        this.statusTrip = statusTrip;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserUD() {
        return userUD;
    }

    public void setUserUD(String userUD) {
        this.userUD = userUD;
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

    public String getStartTrip() {
        return startTrip;
    }

    public void setStartTrip(String startTrip) {
        this.startTrip = startTrip;
    }

    public String getEndTrip() {
        return endTrip;
    }

    public void setEndTrip(String endTrip) {
        this.endTrip = endTrip;
    }

    public String getDataTrip() {
        return dataTrip;
    }

    public void setDataTrip(String dataTrip) {
        this.dataTrip = dataTrip;
    }

    public String getTimeTrip() {
        return timeTrip;
    }

    public void setTimeTrip(String timeTrip) {
        this.timeTrip = timeTrip;
    }

    public String getPriceTrip() {
        return priceTrip;
    }

    public void setPriceTrip(String priceTrip) {
        this.priceTrip = priceTrip;
    }

    public String getSeatTrip() {
        return seatTrip;
    }

    public void setSeatTrip(String seatTrip) {
        this.seatTrip = seatTrip;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCommentTrip() {
        return commentTrip;
    }

    public void setCommentTrip(String commentTrip) {
        this.commentTrip = commentTrip;
    }

    public String getIsDriverUser() {
        return isDriverUser;
    }

    public void setIsDriverUser(String isDriverUser) {
        this.isDriverUser = isDriverUser;
    }

    public String getStatusTrip() {
        return statusTrip;
    }

    public void setStatusTrip(String statusTrip) {
        this.statusTrip = statusTrip;
    }

    public String getIsPackage() {
        return isPackage;
    }

    public void setIsPackage(String isPackage) {
        this.isPackage = isPackage;
    }
}
