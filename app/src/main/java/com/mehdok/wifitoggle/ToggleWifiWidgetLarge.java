package com.mehdok.wifitoggle;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.RemoteViews;

/**
 * Created by Mehdi Sohrabi (mehdok@gmail.com) on 3/30/2015.
 */
public class ToggleWifiWidgetLarge extends AppWidgetProvider
{
    public static final String CHANGE_WIFI_STATE = "com.mehdok.wifitoggle.CHANGE_WIFI_STATE";
    public static final String TAG = "wifi toggle";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        //Log.d(TAG, "onUpdate");
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout_large);
        remoteViews.setOnClickPendingIntent(R.id.widgetLayoutLarge, buildButtonPendingIntent(context));
        updateWidgetImageFirstTime(context, remoteViews);
        pushWidgetUpdate(context, remoteViews);
    }

    public static void pushWidgetUpdate(Context context, RemoteViews remoteViews)
    {
        //Log.d(TAG, "pushWidgetUpdate");
        ComponentName myWidget = new ComponentName(context, ToggleWifiWidgetLarge.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget, remoteViews);
    }

    public static PendingIntent buildButtonPendingIntent(Context context)
    {
        //Log.d(TAG, "buildButtonPendingIntent");
        Intent intent = new Intent();
        intent.setAction(CHANGE_WIFI_STATE);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void updateWidgetImageFirstTime(Context context, RemoteViews remoteViews)
    {
        //Log.d(TAG, "updateWidgetImageFirstTime");
        changeIcon(remoteViews, isWifiConnected(context));
    }

    private boolean isWifiConnected(Context context)
    {
        //Log.d(TAG, "isWifiConnected");
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return mWifi.isConnected();
    }

    private void changeIcon(RemoteViews remoteViews, Boolean isConnected)
    {
        //Log.d(TAG, "changeIcon");
        if(isConnected)
        {
            remoteViews.setImageViewResource(R.id.wifiImageLarge, R.drawable.wifi_on_x2);
        }
        else
        {
            remoteViews.setImageViewResource(R.id.wifiImageLarge, R.drawable.wifi_off_x2);
        }
    }
}
