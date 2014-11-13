package iLighTech.Quran_Khatema;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class About extends Activity implements OnClickListener {
	Button contactUs, review, share, otherApps, facebook;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_about);    
    
    
     facebook = (Button) findViewById(R.id.btn_facebook_like);
     contactUs = (Button) findViewById(R.id.btn_contactUs);
     review = (Button) findViewById(R.id.btn_review);
     share = (Button) findViewById(R.id.btn_share);
     otherApps = (Button) findViewById(R.id.btn_otherApps);
     
     contactUs.setOnClickListener(this);
     review.setOnClickListener(this);
     share.setOnClickListener(this);
     otherApps.setOnClickListener(this);
     facebook.setOnClickListener(this);
}

public void onClick(View buttonPressed) {
	// TODO Auto-generated method stub
	switch (buttonPressed.getId()) {
//==============Share Clicked======================		
	case R.id.btn_share:
		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "برنامج خاتمة القرآن");
		shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=iLighTech.Quran_Khatema" );
		startActivity(shareIntent);
		break;
//==============Review Clicked======================		
	case R.id.btn_review:
		Intent ReviewIntent = new Intent();
		ReviewIntent.setAction(Intent.ACTION_VIEW);
		ReviewIntent.addCategory(Intent.CATEGORY_BROWSABLE);
		ReviewIntent.setData(Uri.parse("https://play.google.com/store/apps/details?id=iLighTech.Quran_Khatema"));
		startActivity(ReviewIntent);
		break;
//===============Contact Us Clicked=====================		
	case R.id.btn_contactUs:
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        String[] recipients = new String[]{"iLighTechnology@gmail.com"};
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "From Quran Reader");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        emailIntent.setType("text/plain");
        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent, "Send mail to developer"));
        finish();
		break;
//================Other Apps Clicked====================	
	case R.id.btn_otherApps:
		Intent otherApps = new Intent();
		otherApps.setAction(Intent.ACTION_VIEW);
		otherApps.addCategory(Intent.CATEGORY_BROWSABLE);
		otherApps.setData(Uri.parse("https://play.google.com/store/apps/developer?id=iLighTech"));
		startActivity(otherApps);
		break;
//=================Facebook Clicked===================
	case R.id.btn_facebook_like:
		Intent Facebook = new Intent();
		Facebook.setAction(Intent.ACTION_VIEW);
		Facebook.addCategory(Intent.CATEGORY_BROWSABLE);
		Facebook.setData(Uri.parse("http://www.facebook.com/iLighTech"));
		startActivity(Facebook);
		break;
	default:
		break;
	}
}
}
