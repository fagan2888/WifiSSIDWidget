package com.ak.wifissidwidget;

import java.util.Timer;
import java.util.TimerTask;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.RemoteViews;

/* TODO:
 * 	- two row layout
 *  - listen to WIFI state changes and update string only then
 *  - create activity if widget is clicked -> go to Wifi settings
 *  - fix all warnings
 *  - better icon
 *  
 *  DONE:
 *  - remove "" from SSID string on some android versions
 *  - create icon
 *  - fix background
 */


public class WifiSSIDWidget extends AppWidgetProvider {
	private static final String TAG = "WifiSSIDWidget";
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new MySSIDDisplay(context, appWidgetManager), 1, 1000);
		Log.v(TAG, "onUpdate()");
	}
	
	private class MySSIDDisplay extends TimerTask {
		RemoteViews remoteViews;
		AppWidgetManager appWidgetManager;
		ComponentName thisWidget;
		private WifiManager wifiManager;
		
		
	public MySSIDDisplay(Context context, AppWidgetManager appWidgetManager) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		this.wifiManager = wifiManager;
		this.appWidgetManager = appWidgetManager;		
		remoteViews = new RemoteViews(context.getPackageName(), R.layout.main);
		thisWidget = new ComponentName(context, WifiSSIDWidget.class);
	}
	
	@Override
	public void run() {
		WifiInfo wifiInfo = this.wifiManager.getConnectionInfo();	
		String ssid = wifiInfo.getSSID();
		
		// remove starting and ending '"' characters (if present)
		ssid = ssid.replaceAll("^\"|\"$", "");
		Log.v(TAG, ssid);

		String text_to_show = "No connection";
		if ((ssid != null) && (ssid != "<unknown ssid>")) { 
			text_to_show = ssid;
		}
		remoteViews.setTextViewText(R.id.widget_textview, text_to_show);
		appWidgetManager.updateAppWidget(thisWidget, remoteViews);
	}
		
	} 

}
