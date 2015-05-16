package net.mudkips.simpleRescheckpointwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;


import java.text.DateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of App Widget functionality.
 */
public class CheckPointAppWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int appWidgetId : appWidgetIds) {
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new CheckPointTime(context, appWidgetManager, appWidgetId), 1, 1000);
        }
    }
    private class CheckPointTime extends TimerTask {
        RemoteViews remoteViews;
        AppWidgetManager appWidgetManager;
        int thisWidget;

        DateFormat format;

        public CheckPointTime(Context context, AppWidgetManager appWidgetManager,int appWidgetId) {
            format = android.text.format.DateFormat.getTimeFormat(context);
            this.appWidgetManager = appWidgetManager;
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            //thisWidget = new ComponentName(context, CheckPointAppWidget.class);
            thisWidget = appWidgetId;
        }
        @Override
        public void run() {
            int cp_interval=18000000;
            Date now = new Date();
            Date next = new Date();
            long countdown = cp_interval - (now.getTime() % cp_interval);
            next.setTime(now.getTime()+countdown);
            remoteViews.setTextViewText(R.id.appwidget_text,"Remaining\n"+ String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(countdown),
                    TimeUnit.MILLISECONDS.toMinutes(countdown-(TimeUnit.MILLISECONDS.toHours(countdown)*1000*60*60)),
                    TimeUnit.MILLISECONDS.toSeconds(countdown) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(countdown))
            )+"\nNext:\n"+format.format(next)
            );
            //remoteViews.setTextViewText(R.id.appwidget_text, "Checkpoint = " +format.format(new Date()));
            appWidgetManager.updateAppWidget(thisWidget, remoteViews);
        }

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    /* static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    } */
}


