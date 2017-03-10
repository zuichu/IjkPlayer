package me.zuichu.ijkplayer.utils;

import android.app.Activity;
import android.view.WindowManager;

import java.util.Locale;

/**
 * Created by office on 2016/10/10.
 */
public class Utils {

    public static int getWidth(Activity context) {
        WindowManager wm = context.getWindowManager();
        return wm.getDefaultDisplay().getWidth();
    }

    public static int getHeight(Activity context) {
        WindowManager wm = context.getWindowManager();
        return wm.getDefaultDisplay().getHeight();
    }

    public static String generateTime(long position) {
        int totalSeconds = (int) (position / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        if (hours > 0) {
            return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes,
                    seconds).toString();
        } else {
            return String.format(Locale.US, "%02d:%02d", minutes, seconds)
                    .toString();
        }
    }
}
