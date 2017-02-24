package com.jackpan.TaiwanpetadoptionApp;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by HYXEN20141227 on 2016/3/9.
 */
public class MyGAManager {
    private static Tracker TRACKER;

    public synchronized static Tracker getTracker(Context context)
    {
        if (TRACKER == null)
        {
            String trackerId = context.getResources().getString(R.string.ga_trackingId);
            TRACKER = GoogleAnalytics.getInstance(context).newTracker(trackerId);
//            TRACKER.enableExceptionReporting(true);
            TRACKER.enableAdvertisingIdCollection(true);
//            TRACKER.enableAutoActivityTracking(true);
        }

        return TRACKER;
    }

    public static void sendScreenName(Context context, String name)
    {
        Tracker tracker = getTracker(context);
        tracker.setScreenName(name);

        tracker.send(new HitBuilders.ScreenViewBuilder().set(name, null).build());
    }
    public static void setGaEvent(Context context,String category ,String action,String label){

        Tracker tracker = getTracker(context);

        tracker.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());

    }
}