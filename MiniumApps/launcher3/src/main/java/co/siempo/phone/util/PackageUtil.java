package co.siempo.phone.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import co.siempo.phone.R;
import co.siempo.phone.db.TableNotificationSms;
import co.siempo.phone.service.SiempoDndService;
import minium.co.core.app.CoreApplication;

/**
 * Created by Shahab on 5/17/2017.
 */

public class PackageUtil {

    private static final String SYSTEM_PACKAGE_NAME = "android";

    public static boolean isCallPackage(String pkg) {
        return pkg != null && !pkg.equalsIgnoreCase("") && (pkg.contains("telecom") || pkg.contains("dialer"));
    }

    public static boolean isMsgPackage(String pkg) {
        return pkg != null && !pkg.equalsIgnoreCase("") && (pkg.contains("messaging") || pkg.contains("com.android.mms"));
    }

    public static boolean isSiempoLauncher(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo defaultLauncher = context.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (defaultLauncher != null && defaultLauncher.activityInfo != null && defaultLauncher.activityInfo.packageName != null) {
            String defaultLauncherStr = defaultLauncher.activityInfo.packageName;
            return defaultLauncherStr.equals(context.getPackageName());
        }
        return false;

    }

    public static boolean isSiempoBlocker(int notifId) {
        return notifId == SiempoDndService.NOTIFICATION_ID;
    }

    public static int getIdByPackage(String pkg) {
        int ret = 0;
        for (int i = 0; i < pkg.length(); i++) {
            ret += ((i + 1) * pkg.charAt(i));
        }
        return ret;
    }

    public static void checkPermission(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Show alert dialog to the user saying a separate permission is needed
            // Launch the settings activity if the user prefers
            if (!Settings.canDrawOverlays(context)) {
                Toast.makeText(context, R.string.msg_overlay_settings, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
                context.startActivity(intent);
            }
        }


    }

    /**
     * Match signature of application to identify that if it is signed by system
     * or not.
     *
     * @param packageName package of application. Can not be blank.
     * @return <code>true</code> if application is signed by system certificate,
     * otherwise <code>false</code>
     */
    public static boolean isSystemApp(String packageName, Context context) {
        try {
            PackageManager mPackageManager = context.getPackageManager();
            // Get packageinfo for target application

            ApplicationInfo ai = mPackageManager.getApplicationInfo(packageName, 0);
            return ((ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0);


        } catch (PackageManager.NameNotFoundException e) {
            CoreApplication.getInstance().logException(e);
            return false;
        }
    }


    public synchronized static void recreateNotification(TableNotificationSms notification, Context context, Integer tempoType, Integer tempoSound) {
        if (tempoType == 0) {
            NotificationCompat.Builder b = new NotificationCompat.Builder(context, "11111");
            Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(notification.getPackageName());

            int requestID = (int) System.currentTimeMillis();
            PendingIntent contentIntent = PendingIntent.getActivity(context, requestID, launchIntentForPackage, PendingIntent.FLAG_UPDATE_CURRENT);

            if (launchIntentForPackage != null) {
                launchIntentForPackage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            }
            b.setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_airplane_air_balloon)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setContentTitle(notification.get_contact_title())
                    .setContentText(notification.get_message())
                    .setContentIntent(contentIntent)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentInfo("Info");
            if (tempoSound == 0) {
                b.setDefaults(Notification.DEFAULT_LIGHTS);
            } else {
                CoreApplication.getInstance().playNotificationSoundVibrate();
            }
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            String CHANNEL_ID = "Siempo";
            if (Build.VERSION.SDK_INT >= 26) {
                CharSequence name = "Siempo";// The user-visible name of the channel.
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                b.setChannelId(CHANNEL_ID);
                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(mChannel);
                }

            }
            if (notificationManager != null) {
                notificationManager.notify(notification.getId().intValue(), b.build());
            }
        }
    }
}