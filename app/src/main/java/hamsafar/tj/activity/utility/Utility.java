package hamsafar.tj.activity.utility;

import android.content.Context;
import android.widget.Toast;

public class Utility {
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static String dayMonthText(int day) {
        String monthName;
        switch (day) {
            case 1:
                monthName = "0" + day;
                break;
            case 2:
                monthName = "0" + day;
                break;
            case 3:
                monthName = "0" + day;
            case 4:
                monthName = "0" + day;
            case 5:
                monthName = "0" + day;
            case 6:
                monthName = "0" + day;
            case 7:
                monthName = "0" + day;
            case 8:
                monthName = "0" + day;
            case 9:
                monthName = "0" + day;
                break;
            default:
                monthName = String.valueOf(day);
                break;

        }
        return monthName;
    }

}
