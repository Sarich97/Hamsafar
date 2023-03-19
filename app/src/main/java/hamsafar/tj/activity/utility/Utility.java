package hamsafar.tj.activity.utility;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.Toast;

public class Utility {


    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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
                monthName = "Янв";
                break;
            case 1:
                monthName = "Фев";
                break;
            case 2:
                monthName = "Март";
                break;
            case 3:
                monthName = "Апр";
                break;
            case 4:
                monthName = "Мая";
                break;
            case 5:
                monthName = "Июня";
                break;
            case 6:
                monthName = "Июля";
                break;
            case 7:
                monthName = "Авг";
                break;
            case 8:
                monthName = "Сен";
                break;
            case 9:
                monthName = "Окт";
                break;
            case 10:
                monthName = "Ноя";
                break;
            case 11:
                monthName = "Дек";
                break;
            default:
                monthName = "Invalid month";
                break;

        }

        return monthName;
    }

}
