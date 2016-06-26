
package com.andreamontanari.placeholder;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class InformationActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_information);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	protected void onResume() {
		ImageView iv01 = (ImageView)findViewById(R.id.imageView1);
		ImageView iv02 = (ImageView)findViewById(R.id.imageView2);
		ImageView iv03 = (ImageView)findViewById(R.id.imageView3);
        ImageView iv04 = (ImageView)findViewById(R.id.imageView5);

		iv01.setBackgroundResource(0);
		iv02.setBackgroundResource(0);
		iv03.setBackgroundResource(0);
		iv04.setBackgroundResource(0);
		
		super.onResume();
	}
	
	public  void facebookMe(View v) {
		Context context = getApplicationContext();
		ImageView iv = (ImageView)findViewById(R.id.imageView1);
		iv.setBackgroundResource(R.drawable.highlight3);
		 try {
			    context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
			     Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/1402856883316764"));
			     startActivity(intent);
			   } catch (Exception e) {
			     Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/placeholderapp"));
			     startActivity(intent);
			   }
			}
	
	public void mailMe(View v) {
		ImageView iv = (ImageView)findViewById(R.id.imageView2);
		iv.setBackgroundResource(R.drawable.normal);
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"andrea.montanari@gmail.com"});
		i.putExtra(Intent.EXTRA_SUBJECT, R.string.mailinfo);
		i.putExtra(Intent.EXTRA_TEXT   , "");
		try {
		    startActivity(Intent.createChooser(i, "Mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
		    Toast.makeText(InformationActivity.this,  R.string.mailerr, Toast.LENGTH_SHORT).show();
		}
	}
	
	public void visitMe(View v) {
		try{
			ImageView iv = (ImageView)findViewById(R.id.imageView3);
			iv.setBackgroundResource(R.drawable.highlight5);
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
			Toast.makeText(this, R.string.webinf,  Toast.LENGTH_LONG).show();
			startActivity(browserIntent);
		    }
				catch (ActivityNotFoundException e) {
				  Toast.makeText(this, R.string.weberr1
				    + R.string.weberr2,  Toast.LENGTH_LONG).show();
				  e.printStackTrace();
	}
		
	}
	
	public void toDesigner(View v) {
		try {
		ImageView iv = (ImageView) findViewById(R.id.imageView5);
		iv.setBackgroundResource(R.drawable.highlight5);
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.deltaaskii.com/web/"));
		startActivity(browserIntent);
	    }
			catch (ActivityNotFoundException e) {
			  Toast.makeText(this, R.string.weberr1
			    + R.string.weberr2,  Toast.LENGTH_LONG).show();
			  e.printStackTrace();
			}
	}
	
}
