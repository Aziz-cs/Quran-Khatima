package iLighTech.Quran_Khatema;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class BootReceiver extends BroadcastReceiver {
	private final String KEY_PICKER_HOUR = "PICKER_HOUR";
	private final String KEY_PICKER_MIN = "PICKER_MIN";
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		    // in our case intent will always be BOOT_COMPLETED, so we can just set
		    // the alarm
		    // Note that a BroadcastReceiver is *NOT* a Context. Thus, we can't use
		    // "this" whenever we need to pass a reference to the current context.
		    // Thankfully, Android will supply a valid Context as the first parameter
	    	Calendar c = Calendar.getInstance();
			SharedPreferences quran = PreferenceManager.getDefaultSharedPreferences(context);
			int selectedHour = quran.getInt(KEY_PICKER_HOUR, c.get(Calendar.HOUR));
			int selectedMin = quran.getInt(KEY_PICKER_MIN, c.get(Calendar.MINUTE));
			
		    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		    Intent i = new Intent(context, QuranService.class);
		    PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
		    am.cancel(pi);
		    
			Date alarmTime = new Date(System.currentTimeMillis());
	        alarmTime.setHours(selectedHour);
	        alarmTime.setMinutes(selectedMin);
	        c.setTimeInMillis(alarmTime.getTime());		
	        c.add(Calendar.DATE, 1);
	        am.setRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
	        

		    }
		
	}


