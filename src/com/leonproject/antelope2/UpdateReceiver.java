package com.leonproject.antelope2;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UpdateReceiver extends BroadcastReceiver{
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		System.out.println("Update Receiver: Now calling intent");
		Intent rIntent = new Intent(context,UpdateReceiverIntentService.class);
		
		context.startService(rIntent);		
   		
	}
}
