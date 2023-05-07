package hamsafar.tj.activity.models;

import java.sql.Timestamp;

public class User {
    private String userEmail;
    private String userName;
    private String userPhone;
    private String userCity;
    private String userCarModel;
    private int userRating;
    private int userTrip;
    private String ipAddress;
    private Timestamp regDate;

    public User(String userEmail, String userName, String userPhone, String userCity, String userCarModel, int userRating, int userTrip, String ipAddress, Timestamp regDate) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userCity = userCity;
        this.userCarModel = userCarModel;
        this.userRating = userRating;
        this.userTrip = userTrip;
        this.ipAddress = ipAddress;
        this.regDate = regDate;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
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

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public String getUserCarModel() {
        return userCarModel;
    }

    public void setUserCarModel(String userCarModel) {
        this.userCarModel = userCarModel;
    }

    public int getUserRating() {
        return userRating;
    }

    public void setUserRating(int userRating) {
        this.userRating = userRating;
    }

    public int getUserTrip() {
        return userTrip;
    }

    public void setUserTrip(int userTrip) {
        this.userTrip = userTrip;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Timestamp getRegDate() {
        return regDate;
    }

    public void setRegDate(Timestamp regDate) {
        this.regDate = regDate;
    }

    // Геттеры и сеттеры
    // ...
}