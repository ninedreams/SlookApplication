package cn.panorama.slook.utils;

import android.content.res.Resources;

/**
 * Created by xingyaoma on 16-4-29.
 */
public class SystemUtils {
    public static int getScreenOrientation() {
        return Resources.getSystem().getConfiguration().orientation;
    }
}
