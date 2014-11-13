package iLighTech.Quran_Khatema;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class QuranFadl extends ListActivity {
	ListView myList;
	Intent intent;
	TextView tv_hadith, tv_title;
	AlertDialog.Builder b;
	AlertDialog alert;
	String [] hadith_content, hadith_title;
	Context context;
	int item_position, pressed_times;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	getWindow().setBackgroundDrawableResource(R.drawable.quran_stats_bg); 
    getListView().setBackgroundColor(Color.TRANSPARENT); 
    getListView().setCacheColorHint(Color.TRANSPARENT); 

    
	setListAdapter(new WorshipRewardAdapter(this,
			android.R.layout.simple_list_item_1,
			R.id.tv_leftToRight,
			getResources().getStringArray(R.array.quran_fadl_array)));
			
}

private class WorshipRewardAdapter extends ArrayAdapter<String>{

	public WorshipRewardAdapter(Context context, int resource,
			int textViewResourceId, String[] objects) {
		super(context, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.arabic_list, parent, false);
		String []ReportItems = getResources().getStringArray(R.array.quran_fadl_array);
		
		TextView separator = (TextView) row.findViewById(R.id.tv_arabicSeparator);
		TextView report_tv = (TextView) row.findViewById(R.id.tv_leftToRight);
		report_tv.setText(ReportItems[position]);
		ImageView report_image = (ImageView) row.findViewById(R.id.iv_fada2el_reportImage);
		switch (position) {
		case 0:
			report_image.setImageDrawable(getResources().getDrawable(R.drawable.lv_quran));
			separator.setVisibility(View.VISIBLE);
			separator.setText("أحاديث فى فضل القرآن");
			report_tv.setText(ReportItems[position]);
			break;
		case 1:
			report_image.setImageDrawable(getResources().getDrawable(R.drawable.lv_quran));
			report_tv.setText(ReportItems[position]);	
			break;
		case 2:
			report_image.setImageDrawable(getResources().getDrawable(R.drawable.lv_quran));
			report_tv.setText(ReportItems[position]);	
			break;
		case 3:
			report_image.setImageDrawable(getResources().getDrawable(R.drawable.lv_quran));
			report_tv.setText(ReportItems[position]);	
			break;
		case 4:
			report_image.setImageDrawable(getResources().getDrawable(R.drawable.lv_quran));
			report_tv.setText(ReportItems[position]);	
			break;
		case 5:
			report_image.setImageDrawable(getResources().getDrawable(R.drawable.lv_quran));
			report_tv.setText(ReportItems[position]);	
			break;
		case 6:
			report_image.setImageDrawable(getResources().getDrawable(R.drawable.lv_quran));
			report_tv.setText(ReportItems[position]);	
			break;
		case 7:
			report_image.setImageDrawable(getResources().getDrawable(R.drawable.lv_quran));
			report_tv.setText(ReportItems[position]);	
			break;
		case 8:
			report_image.setImageDrawable(getResources().getDrawable(R.drawable.lv_quran));
			report_tv.setText(ReportItems[position]);	
			break;
		case 9:
			report_image.setImageDrawable(getResources().getDrawable(R.drawable.lv_quran));
			report_tv.setText(ReportItems[position]);	
			break;
		case 10:
			separator.setVisibility(View.VISIBLE);
			separator.setText("أحاديث فى فضل حفظ القرآن");
			report_image.setImageDrawable(getResources().getDrawable(R.drawable.lv_quran));
			report_tv.setText(ReportItems[position]);	
			break;
		case 11:
			report_image.setImageDrawable(getResources().getDrawable(R.drawable.lv_quran));
			report_tv.setText(ReportItems[position]);	
			break;
		case 12:
			report_image.setImageDrawable(getResources().getDrawable(R.drawable.lv_quran));
			report_tv.setText(ReportItems[position]);	
			break;
		case 13:
			report_image.setImageDrawable(getResources().getDrawable(R.drawable.lv_quran));
			report_tv.setText(ReportItems[position]);	
			break;
		case 14:
			report_image.setImageDrawable(getResources().getDrawable(R.drawable.lv_quran));
			report_tv.setText(ReportItems[position]);	
			break;
		case 15:
			report_image.setImageDrawable(getResources().getDrawable(R.drawable.lv_quran));
			report_tv.setText(ReportItems[position]);	
			break;
		default:
			break;
		}
		/*switch (position) {
		case 0:
			separator.setVisibility(View.VISIBLE);
			separator.setText(R.string.category_salah);
			break;
		case 5:
			separator.setVisibility(View.VISIBLE);
			separator.setText(R.string.category_quran);
			break;
		case 7:
			separator.setVisibility(View.VISIBLE);
			separator.setText(R.string.category_zekr);
			break;
		case 10:
			separator.setVisibility(View.VISIBLE);
			separator.setText(R.string.category_sawm);
			break;
		default:
			break;
		}*/
		return row;
	}

}
		@Override
		protected void onListItemClick(ListView l, View v, int position, long id) {
			// TODO Auto-generated method stub
			item_position = position;
			hadith_content = getResources().getStringArray(R.array.hadith_content);
			hadith_title = getResources().getStringArray(R.array.quran_fadl_array);

			b = new AlertDialog.Builder(QuranFadl.this);
			
			LayoutInflater inflater = getLayoutInflater();
			View dialoglayout = inflater.inflate(R.layout.fadl_dialog, null);
			b.setView(dialoglayout);
	        tv_hadith = (TextView) dialoglayout.findViewById(R.id.tv_hadith);
	        tv_title = (TextView) dialoglayout.findViewById(R.id.tv_fadl_title);
	        Button next = (Button) dialoglayout.findViewById(R.id.btn_next);
	        Button back = (Button) dialoglayout.findViewById(R.id.btn_back);
	        
	        next.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					try{
						pressed_times++;
				        tv_hadith.setText(hadith_content[item_position+pressed_times]);
				        tv_title.setText(hadith_title[item_position+pressed_times]);
					}catch (Exception e) {
						// TODO: handle exception
						pressed_times--;
					}

				}
			});
	        
	        back.setOnClickListener(new OnClickListener() {
				
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					try{
						pressed_times--;
				        tv_hadith.setText(hadith_content[item_position+pressed_times]);
				        tv_title.setText(hadith_title[item_position+pressed_times]);
					}catch (Exception e) {
						// TODO: handle exception
						pressed_times++;
					}

				}
			});
	        
	        tv_hadith.setText(hadith_content[position]);
	        tv_title.setText(hadith_title[position]);
			 alert = b.create();
			 alert.show();
			 
/*            AlertDialog.Builder adb = new AlertDialog.Builder(
                    QuranFadl.this);
                    adb.setTitle(""+l.getItemAtPosition(position));
                    adb.setMessage(hadith_content[position]);
                    adb.setPositiveButton("Ok", null);
                    adb.show();    


*/
			super.onListItemClick(l, v, position, id);
		}
		


}
