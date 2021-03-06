package com.ware.common;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.OrientationEventListener;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by jianfeng.li on 2018/1/3.
 */

public class Utils {
    private static final String TAG = "Utils";
    public static Context mContext;
    // Orientation hysteresis amount used in rounding, in degrees
    public static final int ORIENTATION_HYSTERESIS = 5;
    private static Resources mResource;

    public static void init(Context context) {
        mContext = context;
        mResource = mContext.getResources();
    }

    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA"};
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    public static Boolean saveBitmap(Bitmap bmp, String file) {
        if (bmp == null) return false;
        File f = new File(file);
        try (FileOutputStream out = new FileOutputStream(f)) {
            File p = f.getParentFile();
            if (p == null || !p.exists() && !p.mkdirs()) {
                return false;
            }
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            Log.e(TAG, "saveBitmap: " + e.getMessage());
            return false;
        }
        return true;
    }

    public static String getStringById(@StringRes int resId, Object... formatArgs) {
        return mContext.getResources().getString(resId, formatArgs);
    }

    public static int getColorById(int resId) {
        return mContext.getResources().getColor(resId);
    }

    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static int getScreenWidth() {
        DisplayMetrics dm = mResource.getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight() {
        DisplayMetrics dm = mResource.getDisplayMetrics();
        return dm.heightPixels;
    }

    public static float getDensity() {
        DisplayMetrics metrics = mResource.getDisplayMetrics();
        return metrics.density;
    }

    private static int mStatusBarHeight;

    public static int getStatusBarHeight() {
        if (mStatusBarHeight != 0) return mStatusBarHeight;
        int resourceId = mResource.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            mStatusBarHeight = mResource.getDimensionPixelSize(resourceId);
        }
        return mStatusBarHeight;
    }

    public static float dp2px(float dpVal) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, mResource.getDisplayMetrics());
    }


    /**
     * @param orientation
     * @param orientationHistory
     * @return 返回屏幕旋转的角度 0、90、180，270
     */
    public static int roundOrientation(int orientation, int orientationHistory) {
        boolean changeOrientation;
        if (orientationHistory == OrientationEventListener.ORIENTATION_UNKNOWN) {
            changeOrientation = true;
        } else {
            int dist = Math.abs(orientation - orientationHistory);
            dist = Math.min(dist, 360 - dist);
            changeOrientation = (dist >= 45 + ORIENTATION_HYSTERESIS);
        }
        if (changeOrientation) {
            return ((orientation + 45) / 90 * 90) % 360;
        }
        return orientationHistory;
    }

    public static final Drawable getDrawable(@DrawableRes int resId) {
        return ContextCompat.getDrawable(mContext, resId);
    }

    public static Drawable getDrawableByID(int resId) {
        return mContext.getResources().getDrawable(resId);
    }

    public static void goNotifySetting() {
        Intent intent = new Intent().setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, mContext.getPackageName());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", mContext.getPackageName());
            intent.putExtra("app_uid", mContext.getApplicationInfo().uid);
        } else {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + mContext.getPackageName()));
        }
        mContext.startActivity(intent);
    }

    public static boolean isNotifyEnable(String channelId) {
        NotificationManagerCompat manager = NotificationManagerCompat.from(mContext);
        boolean enable = manager.areNotificationsEnabled();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!enable) return false;
            NotificationChannel channel = manager.getNotificationChannel(channelId);
//            List<NotificationChannel> channels = manager.getNotificationChannels();
            Log.d("NotificationActivity", "isNotifyEnable: channel = " + channel);
            return channel == null || channel.getImportance() != NotificationManager.IMPORTANCE_NONE;
        } else {
            return enable;
        }
    }

    private static Context leakContext;

    public static void testLeak(@NotNull Context context) {
        leakContext = context;
    }
}
