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


    // Геттеры и сеттеры
    // ...
}