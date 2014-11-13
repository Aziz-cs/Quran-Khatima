package iLighTech.Quran_Khatema;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.joda.time.DateTime;
import org.joda.time.Days;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Statistics extends Activity {
	TextView tv_selectedDays, tv_start_date, tv_end_date, tv_today_date,
						tv_remaining_pages, tv_page_per_day, tv_last_page, tv_randomHadith;
	Button update_page;
	EditText current_page;
	SharedPreferences quranPref;
	
	private final String START_DATE_KEY = "START_DATE";
	private final String SELECTED_DAYS_KEY = "SELECTED_DAYS";
	private final String LAST_PAGE_KEY = "LAST_PAGE";

@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_statistics);
	

	getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    quranPref = PreferenceManager.getDefaultSharedPreferences(this);
	
	tv_selectedDays = (TextView) findViewById(R.id.tv_selected_days);
	tv_start_date = (TextView) findViewById(R.id.tv_start_date);
	tv_end_date = (TextView) findViewById(R.id.tv_end_date);
	tv_remaining_pages = (TextView) findViewById(R.id.tv_remaining_pages);
	tv_page_per_day = (TextView) findViewById(R.id.tv_page_per_day);
	tv_last_page = (TextView) findViewById(R.id.tv_last_page);
	tv_today_date = (TextView) findViewById(R.id.tv_today_date);
	tv_randomHadith = (TextView) findViewById(R.id.tv_RandomHadith);
	update_page = (Button) findViewById(R.id.btn_update_page);
	current_page = (EditText) findViewById(R.id.et_current_page);
	
	// ==== Set values according to user preferences:
	setUserPreferences();
	update_page.setOnClickListener(new OnClickListener() {
		
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			updateLastPage(getLastPage());
		}
	});
}
/*
 * Changing values according to user preferences:
 */
public void setUserPreferences(){
	tv_selectedDays.setText(getSelectedDays() +" "+ checkDay(getSelectedDays()));
	tv_start_date.setText(getStartDate());
	tv_end_date.setText(getEndDate(getStartDate()));
	tv_today_date.setText(getTodayDate());
	tv_remaining_pages.setText(getRemainingDays() +" "+ checkDay(getRemainingDays()));
	tv_page_per_day.setText(getPagesPerDay() + " " + "صفحة/يوم");
	tv_last_page.setText(""+getLastPage());
	tv_randomHadith.setText(getRandomHadith());
}


//get start day from shared prefernces
public String getStartDate(){
	String start_date = quranPref.getString(START_DATE_KEY, "00/00/00");
	return start_date;
}

public String getTodayDate(){
	String today_date;
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
	Calendar c = Calendar.getInstance();
	today_date = sdf.format(c.getTime());
	return today_date;
}

public String getEndDate(String startDate){
	String endDate;
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
	Calendar c = Calendar.getInstance();
	try {
		c.setTime(sdf.parse(startDate));
	} catch (Exception e) {
		// TODO Auto-generated catch block
	}
	c.add(Calendar.DATE, getSelectedDays());  // number of days to add
	endDate = sdf.format(c.getTime());  // dt is now the new date
	if(getSelectedDays() == 0) //user pressed statics without making new khatma
		return "00/00/00";
	return endDate;
}

//get selected days from shared prefernces
public int getSelectedDays(){
	int selectedDays = quranPref.getInt(SELECTED_DAYS_KEY, 0);
	return selectedDays;
}

/*
 * get remaining days until the end day of Quran khatma
 */
public int getRemainingDays(){
	int remainingDays = 0;
	try{
	Date endDate = new Date(getEndDate(getStartDate())); // June 20th, 2010
	Date today = new Date(getTodayDate()); // July 24th 
	remainingDays = Days.daysBetween(new DateTime(today), new DateTime(endDate)).getDays();
	}catch (Exception e) {
		// TODO: handle exception
	}
	return remainingDays;
}

/*
 * get pages user should read in day to finish Quran Khatma
 */
public String getPagesPerDay(){
	int quranPages = 604;
	int FinishedPages  = getLastPage();
	int remainingDays = getRemainingDays();
	
	if(remainingDays < 0){ // if period is finished and he didnt finish khatma.
		int pagesPerDay = (quranPages-FinishedPages);
		return ""+pagesPerDay;
	}
	double pagesPerDay = (double)(quranPages-FinishedPages)/remainingDays;

	return 	new DecimalFormat("###.#").format(pagesPerDay);
}

/*
 * set last page
 */
public int getLastPage(){
	int lastPage;
	lastPage = quranPref.getInt(LAST_PAGE_KEY, 0);
	return lastPage;
}

/*
 * update last page & check/handle input errors
 */
public void updateLastPage(int lastPage){
	int editText_LastPage = -1;
	try{
		editText_LastPage = Integer.parseInt(current_page.getText().toString());
	}catch (Exception e) {
		Toast.makeText(this, "من فضلك أدخل رقم آخر صفحة", Toast.LENGTH_SHORT).show();
	}
	if(editText_LastPage > 604){
		Toast.makeText(this, "رقم صفحة غير صحيح", Toast.LENGTH_SHORT).show();
	}else if(editText_LastPage < 604){
	SharedPreferences.Editor quran_editor = quranPref.edit();
	try{
	lastPage = Integer.parseInt(current_page.getText().toString());
	}catch (Exception e) {
		// TODO: handle exception
	}
	quran_editor.putInt(LAST_PAGE_KEY, lastPage);
	quran_editor.commit();
	tv_last_page.setText(""+getLastPage());
	tv_page_per_day.setText(getPagesPerDay() + " " + "صفحة/يوم");	
	}else if(editText_LastPage == 604){
		tv_page_per_day.setText("الحمد لله تمت");
		SharedPreferences.Editor quran_editor = quranPref.edit();
		lastPage = Integer.parseInt(current_page.getText().toString());
		quran_editor.putInt(LAST_PAGE_KEY, lastPage);
		quran_editor.commit();
		tv_last_page.setText(""+getLastPage());

	}
	
}

/*
 * get a random hadith from the hadith content string array 
 */
public String getRandomHadith() {
	// TODO Auto-generated method stub
	String [] hadith = getResources().getStringArray(R.array.hadith_content);
	int min = 0;
	int max = 14;
	Random rand = new Random();;

	// nextInt is normally exclusive of the top value,
	// so add 1 to make it inclusive
	int randomNum = rand.nextInt(max - min + 1) + min;
//	int random = min + (int)(Math.random() * ((max - min) + 1));
	return hadith[randomNum];
}
/*
 * check days to be يوم or to be أيام according to arabic format <10 = أيام
 */
public String checkDay(int day){
	String dayOnly = "يوم";
	String days = "أيام";
	if((day < 11) && ( day > 0)){
		return days;
	}else{
		return dayOnly;
	}
}

public void clearAllPreferences(){
    quranPref = PreferenceManager.getDefaultSharedPreferences(this);
    SharedPreferences.Editor quran_editor = quranPref.edit();
    quran_editor.clear();
    quran_editor.commit();
}


}
