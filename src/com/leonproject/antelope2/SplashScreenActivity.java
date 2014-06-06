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
							regId = registerGCM();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    					System.out.println("GCM RegId: " + regId);
    				} else {
    					
    					System.out.println("Its already registerd.");
    					
    					Toast.makeText(getApplicationContext(),
    							"Already Registered with GCM Server!",
    							Toast.LENGTH_SHORT).show();
    					
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
    public String registerGCM() throws InterruptedException {

		gcm = GoogleCloudMessaging.getInstance(this);
		regId = getRegistrationId(context);

		if (TextUtils.isEmpty(regId)) {

			registerInBackground();

			System.out.println("RegisterActivity"+
					"registerGCM - successfully registered with GCM server - regId: "
							+ regId);
			appUtil = new ShareExternalServer();
			appUtil.shareRegIdWithAppServer(context, regId);
			
		} else {
			System.out.println("RegId already registered.Re-registerting with App server");
			/*Here the regId is registered with the GCM server we will still try to re-register with the App server 
			  as in the first case it might have failed. Not an ideal way of doing it , should save state whether it is registered with app server
			  but currently the server is not communicating back to the app. So we will re register and at the the server end taken care that no 
			  duplicate registration is done */
			
			appUtil = new ShareExternalServer();
			appUtil.shareRegIdWithAppServer(context, regId);
			
			Thread.sleep(SPLASH_TIME_OUT);
			
		}
		return regId;
	}

	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getSharedPreferences(
				RaceListActivity.class.getSimpleName(), Context.MODE_PRIVATE);
		String registrationId = prefs.getString(REG_ID, "");
		if (registrationId.isEmpty()) {
			System.out.println(TAG + "Registration not found.");
			return "";
		}
		int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			System.out.println(TAG + "App version changed.");
			return "";
		}
		return registrationId;
	}

	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			System.out.println("RegisterActivity"+
					"I never expected this! Going down, going down!" + e);
			throw new RuntimeException(e);
		}
	}

	private void registerInBackground() {
		
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regId = gcm.register(Config.GOOGLE_PROJECT_ID);
					System.out.println("RegisterActivity"+ "registerInBackground - regId: "
							+ regId);
					msg = "Device registered, registration ID=" + regId;

					storeRegistrationId(context, regId);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					System.out.println("RegisterActivity"+  "Error: " + msg);
				}
				System.out.println("RegisterActivity"+ "AsyncTask completed: " + msg);		
		}
	

	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getSharedPreferences(
				RaceListActivity.class.getSimpleName(), Context.MODE_PRIVATE);
		int appVersion = getAppVersion(context);
		System.out.println(TAG + "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(REG_ID, regId);
		editor.putInt(APP_VERSION, appVersion);
		editor.commit();
	}

}
