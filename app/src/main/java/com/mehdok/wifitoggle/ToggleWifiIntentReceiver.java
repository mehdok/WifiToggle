package com.mehdok.wifitoggle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Mehdi Sohrabi (mehdok@gmail.com) on 3/30/2015.
 */
public class ToggleWifiIntentReceiver extends BroadcastReceiver
{
    private AnimationDrawable wifiAnim;
    private boolean animateFlag = false;
    private final int SLEEP_TIME = 100; //millis
    private final int WAIT_FOR_ERROR = 1000 * 10; //15 second

    @Override
    public void onReceive(Context context, Intent intent)
    {
        //Log.d(ToggleWifiWidget.TAG, "onReceive");
        if (intent.getAction().equals(ToggleWifiWidget.CHANGE_WIFI_STATE))
        {
            //animateFlag = true;
            //startAnimation(context);
            toggleWifiState(context);
        }
        else if(intent.getAction().equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION))
        {
            //animateFlag = false;
            boolean connected = intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false);
            updateWidgetState(context, connected);
        }
    }

    private void updateWidgetState(Context context,boolean connected)
    {
        //Log.d(ToggleWifiWidget.TAG, "updateWidgetState");
        RemoteViews rv = updateWidgetImage(context, connected/*isWifiConnected(context)*/);
        //rv.setOnClickPendingIntent(R.id.widgetLayout, ToggleWifiWidget.buildButtonPendingIntent(context));
        ToggleWifiWidget.pushWidgetUpdate(context.getApplicationContext(), rv);
        RemoteViews rv2 = updateWidgetImageLarge(context, connected);
        //rv2.setOnClickPendingIntent(R.id.widgetLayoutLarge, ToggleWifiWidget.buildButtonPendingIntent(context));
        ToggleWifiWidgetLarge.pushWidgetUpdate(context.getApplicationContext(), rv2);
    }

    private boolean isWifiConnected(Context context)
    {
        //Log.d(ToggleWifiWidget.TAG, "isWifiConnected");
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return mWifi.isConnected();
    }

    private RemoteViews updateWidgetImage(Context context, boolean state)
    {
        //Log.d(ToggleWifiWidget.TAG, "updateWidgetImage");
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        remoteViews.setOnClickPendingIntent(R.id.widgetLayout, ToggleWifiWidget.buildButtonPendingIntent(context));
        changeIcon(remoteViews, state);
        return remoteViews;
    }

    private RemoteViews updateWidgetImageLarge(Context context, boolean state)
    {
        //Log.d(ToggleWifiWidget.TAG, "updateWidgetImageLarge");
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout_large);
        remoteViews.setOnClickPendingIntent(R.id.widgetLayoutLarge, ToggleWifiWidgetLarge.buildButtonPendingIntent(context));
        changeIconLarge(remoteViews, state);
        return remoteViews;
    }

    private void changeIcon(RemoteViews remoteViews, Boolean isConnected)
    {
        //Log.d(ToggleWifiWidget.TAG, "changeIcon");
        if(isConnected)
        {
            remoteViews.setImageViewResource(R.id.wifiImage, R.drawable.wifi_on);
        }
        else
        {
            remoteViews.setImageViewResource(R.id.wifiImage, R.drawable.wifi_off);
        }
    }

    private void changeIconLarge(RemoteViews remoteViews, Boolean isConnected)
    {
        //Log.d(ToggleWifiWidget.TAG, "changeIconLarge");
        if(isConnected)
        {
            remoteViews.setImageViewResource(R.id.wifiImageLarge, R.drawable.wifi_on_x2);
        }
        else
        {
            remoteViews.setImageViewResource(R.id.wifiImageLarge, R.drawable.wifi_off_x2);
        }
    }

    private void toggleWifiState(Context context)
    {
        //Log.d(ToggleWifiWidget.TAG, "toggleWifiState");
        if(isWifiConnected(context))
        {
            startAnimation(context, false);
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(false);
        }
        else
        {
            startAnimation(context, true);
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(true);
        }
    }


    private void startAnimation(Context context, boolean on)
    {
        if(on)
        {
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.anim_wifi_on);
            ToggleWifiWidget.pushWidgetUpdate(context.getApplicationContext(), rv);
            RemoteViews rv2 = new RemoteViews(context.getPackageName(), R.layout.anim_wifi_on_large);
            ToggleWifiWidgetLarge.pushWidgetUpdate(context.getApplicationContext(), rv2);
        }
        else
        {
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.anim_wifi_on);
            ToggleWifiWidget.pushWidgetUpdate(context.getApplicationContext(), rv);
            RemoteViews rv2 = new RemoteViews(context.getPackageName(), R.layout.anim_wifi_on_large);
            ToggleWifiWidgetLarge.pushWidgetUpdate(context.getApplicationContext(), rv2);
        }

        changeLayoutOnError(context);
    }

    private void changeLayoutOnError(final Context context)
    {
        Log.e(ToggleWifiWidget.TAG, "changeLayoutOnError");
        new Timer().schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                Log.e(ToggleWifiWidget.TAG, "changeLayoutOnError-run");
                updateWidgetState(context, isWifiConnected(context));
            }
        }, WAIT_FOR_ERROR);

        /*
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
            }
        }, 100);
        */
    }

}
