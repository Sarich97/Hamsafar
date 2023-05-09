package hamsafar.tj.activity.utility;


import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class Utility {


    public static final String POSTS_COLLECTION = "posts";
    public static final String BOOKS_COLLECTION = "books";
    public static final String USERS_COLLECTION = "users";
    public static final String CONFIG_COLLECTION = "appconfig";

    public static boolean isOnline(Context context) {
        try {
            Process p1 = Runtime.getRuntime().exec(
                    "ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal == 0);
            if (reachable) {
                System.out.println("Internet access");
                return reachable;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }


    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showSnakbarTypeOne(View view, String mMessage) {
        Snackbar.make(view, mMessage, Snackbar.LENGTH_LONG)
                .show();
    }


    public static String dayMonthText(int day) {
        String monthName;
        switch (day) {
            case 0:
                monthName = "0" + day;
                break;
            case 1:
                monthName = "0" + day;
                break;
            case 2:
                monthName = "0" + day;
                break;
            case 3:
                monthName = "0" + day;
                break;
            case 4:
                monthName = "0" + day;
                break;
            case 5:
                monthName = "0" + day;
                break;
            case 6:
                monthName = "0" + day;
                break;
            case 7:
                monthName = "0" + day;
                break;
            case 8:
                monthName = "0" + day;
                break;
            case 9:
                monthName = "0" + day;
                break;
            default:
                monthName = String.valueOf(day);
                break;

        }
        return monthName;
    }


    public static String minuteText(int hour) {
        String minString;
        switch (hour) {
            case 0:
                minString = hour + "0";
                break;
            case 1:
                minString = hour + "0";
                break;
            case 2:
                minString = hour + "0";
                break;
            case 3:
                minString = hour + "0";
                break;
            case 4:
                minString = hour + "0";
                break;
            case 5:
                minString = hour + "0";
                break;
            case 6:
                minString = hour + "0";
                break;
            case 7:
                minString = hour + "0";
                break;
            case 8:
                minString = hour + "0";
                break;
            case 9:
                minString = hour + "0";
                break;
            default:
                minString = String.valueOf(hour);
                break;

        }
        return minString;
    }


    public static String getMonthText(int monthOfYear) {
        String monthName;
        switch (monthOfYear) {
            case 0:
                monthName = "01";
                break;
            case 1:
                monthName = "02";
                break;
            case 2:
                monthName = "03";
                break;
            case 3:
                monthName = "04";
                break;
            case 4:
                monthName = "05";
                break;
            case 5:
                monthName = "06";
                break;
            case 6:
                monthName = "07";
                break;
            case 7:
                monthName = "08";
                break;
            case 8:
                monthName = "09";
                break;
            case 9:
                monthName = "10";
                break;
            case 10:
                monthName = "11";
                break;
            case 11:
                monthName = "12";
                break;
            default:
                monthName = "Invalid month";
                break;

        }

        return monthName;
    }

}
