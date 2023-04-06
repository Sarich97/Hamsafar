package hamsafar.tj.activity.utility;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class Utility {


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
                monthName = "янв";
                break;
            case 1:
                monthName = "фев";
                break;
            case 2:
                monthName = "март";
                break;
            case 3:
                monthName = "апр";
                break;
            case 4:
                monthName = "мая";
                break;
            case 5:
                monthName = "июня";
                break;
            case 6:
                monthName = "июля";
                break;
            case 7:
                monthName = "авг";
                break;
            case 8:
                monthName = "сен";
                break;
            case 9:
                monthName = "окт";
                break;
            case 10:
                monthName = "ноя";
                break;
            case 11:
                monthName = "дек";
                break;
            default:
                monthName = "Invalid month";
                break;

        }

        return monthName;
    }

}
