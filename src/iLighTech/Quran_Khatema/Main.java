package iLighTech.Quran_Khatema;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class Main extends Activity implements OnClickListener {
	Button newKhatma, statistics, btn_fadl, btn_about;
	TextView tv_newPages;
	SharedPreferences quranPref; 
	int selectedDays;
	private Handler mHandler = new Handler();
	
    public static AlarmManager am;
    public static PendingIntent pi;
    public static Intent Quran_Service;
    
    private static Context MainContext;

	private final String START_DATE_KEY = "START_DATE";
	private final String SELECTED_DAYS_KEY = "SELECTED_DAYS";
	private final String LAST_PAGE_KEY = "LAST_PAGE";
	private final String KEY_PICKER_HOUR = "PICKER_HOUR";
	private final String KEY_PICKER_MIN = "PICKER_MIN";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_main);
    	

    	   
         Main.MainContext = getApplicationContext();
        
        quranPref = PreferenceManager.getDefaultSharedPreferences(this);
        
        newKhatma = (Button) findViewById(R.id.btn_newkhatma);
        statistics = (Button) findViewById(R.id.btn_stats); 
        btn_fadl = (Button) findViewById(R.id.btn_fadl);
        btn_about = (Button) findViewById(R.id.btn_about);
        
        newKhatma.setOnClickListener(this);
        statistics.setOnClickListener(this);
        btn_fadl.setOnClickListener(this);        
        btn_about.setOnClickListener(this);        
    }
    

	public void onClick(View btn) {
		// TODO Auto-generated method stub
		switch (btn.getId()) {
		case R.id.btn_stats:
			startActivity(new Intent(this, Statistics.class));
			break;
		case R.id.btn_fadl:
			startActivity(new Intent(this, QuranFadl.class));
			break;
		case R.id.btn_about:
			startActivity(new Intent(this, About.class));
			break;
		case R.id.btn_newkhatma:
			LayoutInflater inflater = getLayoutInflater();
			View dialoglayout = inflater.inflate(R.layout.dialog_settings, (ViewGroup) getCurrentFocus());
	         tv_newPages = (TextView) dialoglayout.findViewById(R.id.tv_newDays);

			AlertDialog.Builder b = new AlertDialog.Builder(Main.this);
			b.setTitle("تفاصيل الختمة");
			b.setIcon(android.R.drawable.ic_dialog_info);
			b.setView(dialoglayout);
			//spinner
			final Spinner spinner = (Spinner) dialoglayout.findViewById(R.id.sp_days);
			// Create an ArrayAdapter using the string array and a default spinner layout
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Main.this, R.array.khatma_days, android.R.layout.simple_spinner_item);
			// Specify the layout to use when the list of choices appears
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// Apply the adapter to the spinner
			spinner.setAdapter(adapter);
			//Filter day selected:
			spinner.setSelection(4);
			spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> parent, View selectedItemView,
						int position, long id) {
					// TODO Auto-generated method stub
					if(position == 8){
						AlertDialog.Builder alert = new AlertDialog.Builder(Main.this);

						alert.setMessage("رجاء كتابة عدد الأيام المرادة للختمة");

						// Set an EditText view to get user input 
						final EditText input = new EditText(Main.this);
						input.setInputType(InputType.TYPE_CLASS_NUMBER);
						alert.setView(input);

						alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
						  selectedDays = Integer.parseInt(input.getText().toString());
						  // Do something with value!
						  tv_newPages.setText(selectedDays+ " " + "يوم");
						  tv_newPages.setVisibility(0);
						  }
						});

						alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						  public void onClick(DialogInterface dialog, int whichButton) {
						    // Canceled.
						  }
						});

						alert.show();
					}
				}

				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			
			final TimePicker timerPicker = (TimePicker) dialoglayout.findViewById(R.id.timer_reportTime);
			
			b.setPositiveButton("بدأ الختمة", new DialogInterface.OnClickListener() {


				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(Main.this, "تم فتح ختمة جديدة، وسيتم التذكير فى الوقت المحدد", Toast.LENGTH_LONG).show();
					//Clear any previous Alarm, Notification & Preferences:
					QuranService.cancelNotifications();
			        cancelAlarm();
		    		clearAllPreferences();
					// Spinner part:
					switch ((int)spinner.getSelectedItemId()) {
					case 0:		
						selectedDays = 3;
						break;
					case 1:
						selectedDays = 5;
						break;
					case 2:
						selectedDays = 7;
						break;
					case 3:
						selectedDays = 10;
						break;
					case 4:
						selectedDays = 15;
						break;
					case 5:
						selectedDays = 20;
						break;
					case 6:
						selectedDays = 30;
						break;
					case 7:
						selectedDays = 40;
						break;
					default:
						break;
					}

					//================
			        am = (AlarmManager) getSystemService(ALARM_SERVICE);
			        Quran_Service = new Intent(Main.this, QuranService.class);
			        pi = PendingIntent.getService(Main.this, 0, Quran_Service, 0);
			        am.cancel(pi);

			        // Start repeating alarm:
			        Calendar c = Calendar.getInstance();
					Date alarmTime = new Date(System.currentTimeMillis());
			        alarmTime.setHours(timerPicker.getCurrentHour());
			        alarmTime.setMinutes(timerPicker.getCurrentMinute());
			        c.setTimeInMillis(alarmTime.getTime());			      
			        am.setRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
			        
			        //Save important data in shared preferences:
			        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
					String quran_date = df.format(c.getTime());
					
			        SharedPreferences.Editor quranPref_editor = quranPref.edit();
			        quranPref_editor.putString(START_DATE_KEY, quran_date);
			        quranPref_editor.putInt(SELECTED_DAYS_KEY, selectedDays);
			        quranPref_editor.putInt(LAST_PAGE_KEY, 0);
			        quranPref_editor.putInt(KEY_PICKER_HOUR, timerPicker.getCurrentHour());
			        quranPref_editor.putInt(KEY_PICKER_MIN, timerPicker.getCurrentMinute());

			        quranPref_editor.commit();
				}
			});
			
			b.setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			});
			
			AlertDialog alert = b.create();
			alert.show();
			break;
		default:
			break;
		}
	}
	
	public void cancelAlarm() {
        am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Quran_Service = new Intent(Main.this, QuranService.class);
        pi = PendingIntent.getService(Main.this, 0, Quran_Service, 0);
        am.cancel(pi);
	}


	public static Context getAppContext(){
	    return Main.MainContext;
	}
	
	public void clearAllPreferences(){
        quranPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor quran_editor = quranPref.edit();
        quran_editor.clear();
        quran_editor.commit();
	}
	
	/*
	 * menu to stop and clear all
	 */
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	    	// TODO Auto-generated method stub
	    	menu.add("إلغاء الختمة").setIcon(android.R.drawable.ic_input_delete);
	    	return super.onCreateOptionsMenu(menu);
	    }

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	    	// TODO Auto-generated method stub
	    	try {
	    		QuranService.cancelNotifications();
	    		cancelAlarm();
	    		clearAllPreferences();
	    		Toast.makeText(this, "تم إلغاء الختمة وإيقاف المذكر اليومى", Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				// TODO: handle exception
			}
	    	return super.onOptionsItemSelected(item);

}
	    
	    
}