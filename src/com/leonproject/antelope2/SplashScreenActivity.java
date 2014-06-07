package com.leonproject.antelope2;


import java.io.IOException;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;


public class SplashScreenActivity extends Activity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    GoogleCloudMessaging gcm;
	Context context;
	String regId;
	ShareExternalServer appUtil;
	AsyncTask<Void, Void, String> shareRegidTask;
	
	public static final String REG_ID = "regId";
	private static final String APP_VERSION = "appVersion";

	static final String TAG = "Register Activity";
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_splash_screen);
    	context = getApplicationContext();
    	
    	new PrefetchData().execute();
        }
       
       
    private class PrefetchData extends AsyncTask<Void, Void, Void> {
    	@Override
    	protected void onPreExecute(){
    	super.onPreExecute();
    	
    	}
        @Override
      	protected Void doInBackground(Void... arg0) {
 
        			if (TextUtils.isEmpty(regId)) {
        				System.out.println("Pre registration");
        				try {
							Thread.sleep(SPLASH_TIME_OUT);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
        				Intent rIntent = new Intent(context,UpdateReceiverIntentService.class);
        				context.startService(rIntent);	
					
    					System.out.println("GCM RegId: " + regId);
    				} else {
    					
    					System.out.println("Its already registerd.");
    				}
        			return null;
        				}
        				
        @Override
        protected void onPostExecute(Void result) {
        
        super.onPostExecute(result);
        // After completing http call
        // will close this activity and lauch main activity
        Intent i = new Intent(SplashScreenActivity.this, RaceListActivity.class);
        startActivity(i);
        // close this activity
         finish();
        }
    }
}
 