package hamsafar.tj.activity.models;

public class books {
    private String notifiID, notifiStatus, postID, userID, userName, userPhone,
            postCreateID, date, locationFrom, locationTo, rating, statusTrip,
            userRating, userTripCount, tripPrice, tripBrandCar, dirverName, comments , driverPhone, isPackBox, seatTrip;

    books() {}

    public books(String notifiID, String notifiStatus, String postID, String userID, String userName, String userPhone, String postCreateID, String date, String locationFrom, String locationTo, String rating, String statusTrip, String userRating, String userTripCount, String tripPrice, String tripBrandCar, String dirverName, String comments, String driverPhone, String isPackBox, String seatTrip) {
        this.notifiID = notifiID;
        this.notifiStatus = notifiStatus;
        this.postID = postID;
        this.userID = userID;
        this.userName = userName;
        this.userPhone = userPhone;
        this.postCreateID = postCreateID;
        this.date = date;
        this.locationFrom = locationFrom;
        this.locationTo = locationTo;
        this.rating = rating;
        this.statusTrip = statusTrip;
        this.userRating = userRating;
        this.userTripCount = userTripCount;
        this.tripPrice = tripPrice;
        this.tripBrandCar = tripBrandCar;
        this.dirverName = dirverName;
        this.comments = comments;
        this.driverPhone = driverPhone;
        this.isPackBox = isPackBox;
        this.seatTrip = seatTrip;
    }

    public String getNotifiID() {
        return notifiID;
    }

    public void setNotifiID(String notifiID) {
        this.notifiID = notifiID;
    }

    public String getNotifiStatus() {
        return notifiStatus;
    }

    public void setNotifiStatus(String notifiStatus) {
        this.notifiStatus = notifiStatus;
    }

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocationFrom() {
        return locationFrom;
    }

    public void setLocationFrom(String locationFrom) {
        this.locationFrom = locationFrom;
    }

    public String getLocationTo() {
        return locationTo;
    }

    public void setLocationTo(String locationTo) {
        this.locationTo = locationTo;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getStatusTrip() {
        return statusTrip;
    }

    public void setStatusTrip(String statusTrip) {
        this.statusTrip = statusTrip;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getUserTripCount() {
        return userTripCount;
    }

    public void setUserTripCount(String userTripCount) {
        this.userTripCount = userTripCount;
    }

    public String getTripPrice() {
        return tripPrice;
    }

    public void setTripPrice(String tripPrice) {
        this.tripPrice = tripPrice;
    }

    public String getTripBrandCar() {
        return tripBrandCar;
    }

    public void setTripBrandCar(String tripBrandCar) {
        this.tripBrandCar = tripBrandCar;
    }

    public String getDirverName() {
        return dirverName;
    }

    public void setDirverName(String dirverName) {
        this.dirverName = dirverName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getIsPackBox() {
        return isPackBox;
    }

    public void setIsPackBox(String isPackBox) {
        this.isPackBox = isPackBox;
    }

    public String getSeatTrip() {
        return seatTrip;
    }

    public void setSeatTrip(String seatTrip) {
        this.seatTrip = seatTrip;
    }
}
