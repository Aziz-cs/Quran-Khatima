package iLighTech.Quran_Khatema;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

public class QuranService extends Service {
	SharedPreferences quranPref; 
	int selectedDays;
	int notificationID = 0;
	private final String START_DATE_KEY = "START_DATE";
	private final String SELECTED_DAYS_KEY = "SELECTED_DAYS";
	private final String NOTIFICATION_ID_KEY = "NOTIFICATION_ID";
	
	NotificationCompat.Builder mBuilder;
    private WakeLock mWakeLock;
    public static NotificationManager mNotificationManager;

    /**
     * Simply return null, since our Service will not be communicating with
     * any other components. It just does its work silently.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    /**
     * This is where we initialize. We call this when onStart/onStartCommand is
     * called by the system. We won't do anything with the intent here, and you
     * probably won't, either.
     */
    private void handleIntent(Intent intent) {
        // obtain the wake lock
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Quran_Tag");
        mWakeLock.acquire();
        
        // check the global background data setting
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (!cm.getBackgroundDataSetting()) {
            stopSelf();
            return;
        }
        
        // do the actual work, in a separate thread
        new PollTask().execute();
    }
    
    private class PollTask extends AsyncTask<Void, Void, Void> {
        /**
         * This is where YOU do YOUR work. There's nothing for me to write here
         * you have to fill this in. Make your HTTP request(s) or whatever it is
         * you have to do to get your updates in here, because this is run in a
         * separate thread
         */
        @Override
        protected Void doInBackground(Void... params) {
            SharedPreferences quranPref = PreferenceManager.getDefaultSharedPreferences(QuranService.this);
        	selectedDays = quranPref.getInt(SELECTED_DAYS_KEY, 0);
        	
            // do stuff!
        	mBuilder =
        	        new NotificationCompat.Builder(QuranService.this)
        			.setDefaults(Notification.DEFAULT_ALL)
        			.setAutoCancel(true)
        			.setTicker("خاتمة القرآن")
        	        .setSmallIcon(R.drawable.quran_notify)
        	        .setContentTitle("خاتمة القرآن - تذكرة قراءة الورد اليومى")
        	        .setContentText("اضعط هنا لتعرف تفاصيل ختمتك");
        	
        	// Creates an explicit intent for an Activity in your app
        	Intent resultIntent = new Intent(QuranService.this, Statistics.class);

        	// The stack builder object will contain an artificial back stack for the
        	// started Activity.
        	// This ensures that navigating backward from the Activity leads out of
        	// your application to the Home screen.
        	TaskStackBuilder stackBuilder = TaskStackBuilder.create(QuranService.this);
        	// Adds the back stack for the Intent (but not the Intent itself)
        	stackBuilder.addParentStack(Main.class);
        	// Adds the Intent that starts the Activity to the top of the stack
        	stackBuilder.addNextIntent(resultIntent);
        	PendingIntent resultPendingIntent =
        	        stackBuilder.getPendingIntent(
        	            0,
        	            PendingIntent.FLAG_UPDATE_CURRENT
        	        );
        	mBuilder.setContentIntent(resultPendingIntent);
        	mNotificationManager =
        	    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        	// mId allows you to update the notification later on.

            return null;
        }
        
        /**
         * In here you should interpret whatever you fetched in doInBackground
         * and push any notifications you need to the status bar, using the
         * NotificationManager. I will not cover this here, go check the docs on
         * NotificationManager.
         *
         * What you HAVE to do is call stopSelf() after you've pushed your
         * notification(s). This will:
         * 1) Kill the service so it doesn't waste precious resources
         * 2) Call onDestroy() which will release the wake lock, so the device
         *    can go to sleep again and save precious battery.
         */
        @Override
        protected void onPostExecute(Void result) {
            // handle your data
        	if(getNotificationNumber() <= getSelectedDays()){
        		mNotificationManager.notify(getNotificationNumber(), mBuilder.build());
            	incrementNotificationByOne();
        	}else{
        		try{
        		cancelAlarm();
        		cancelNotifications();
        		}catch (Exception e) {
					// TODO: handle exception
				}
        	}
            stopSelf();
        }
    }
    
    
    /**
     * This is called on 2.0+ (API level 5 or higher). Returning
     * START_NOT_STICKY tells the system to not restart the service if it is
     * killed because of poor resource (memory/cpu) conditions.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleIntent(intent);
        return START_NOT_STICKY;
    }
    
    /**
     * In onDestroy() we release our wake lock. This ensures that whenever the
     * Service stops (killed for resources, stopSelf() called, etc.), the wake
     * lock will be released.
     */
    public void onDestroy() {
        super.onDestroy();
        mWakeLock.release();
    }
    
    public static void cancelNotifications(){
    	try{
        	mNotificationManager.cancelAll();
    	}catch (Exception e) {
			// TODO: handle exception
		}
    	}
    
    public void incrementNotificationByOne(){
        SharedPreferences quranPref = PreferenceManager.getDefaultSharedPreferences(QuranService.this);
    	notificationID = quranPref.getInt(NOTIFICATION_ID_KEY, 1);
    	notificationID++;
    	SharedPreferences.Editor quranPref_editor = quranPref.edit();
    	quranPref_editor.putInt(NOTIFICATION_ID_KEY, notificationID);
    	quranPref_editor.commit();
    }
    
    public int getNotificationNumber(){
        SharedPreferences quranPref = PreferenceManager.getDefaultSharedPreferences(QuranService.this);
    	int notificationNumber = quranPref.getInt(NOTIFICATION_ID_KEY, 1);
    	return notificationNumber;
    }
    
    public int getSelectedDays(){
        SharedPreferences quranPref = PreferenceManager.getDefaultSharedPreferences(QuranService.this);
    	selectedDays = quranPref.getInt(SELECTED_DAYS_KEY, 0);
    	return selectedDays;
    }
    
    public void resetNotificationID(){
        SharedPreferences quranPref = PreferenceManager.getDefaultSharedPreferences(QuranService.this);
        SharedPreferences.Editor quran_editor = quranPref.edit();
        quran_editor.putInt(NOTIFICATION_ID_KEY, 1);
        quran_editor.commit();
    }
    
    public void cancelAlarm(){
    	AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent Quran_Service = new Intent(Main.getAppContext(), QuranService.class);
    	PendingIntent pi = PendingIntent.getService(Main.getAppContext(), 0, Quran_Service, 0);
    	am.cancel(pi);
    }
}