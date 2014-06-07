package com.leonproject.antelope2;

public interface Config {

	// used to share GCM regId with application server - using php app server
	static final String APP_SERVER_URL = "http://www.leonprojects.in/Calathon/TestingEnv/gcm.php?shareRegId=1";

	// GCM server using java
	// static final String APP_SERVER_URL =
	// "http://192.168.1.17:8080/GCM-App-Server/GCMNotification?shareRegId=1";

	// Google Project Number
	static final String GOOGLE_PROJECT_ID = "897870458440";
	static final String MESSAGE_KEY = "message";
	static final String TITLE_ID = "title_id";
	static final String TITLE = "title";

}
