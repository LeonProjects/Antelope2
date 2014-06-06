package com.leonproject.antelope2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.leonproject.antelope2.LazyAdapter;
import com.leonproject.antelope2.R;
import com.leonproject.antelope2.XMLParser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * An activity representing a list of Races. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link RaceDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link RaceListFragment} and the item details (if present) is a
 * {@link RaceDetailFragment}.
 * <p>
 * This activity also implements the required {@link RaceListFragment.Callbacks}
 * interface to listen for item selections.
 */
public class RaceListActivity extends Activity {

	public static final String KEY_RACE = "race";
	static final String KEY_ID = "id";
	static final String KEY_TITLE = "title";
	static final String KEY_LOCATION = "city";
	static final String KEY_RACEDATE = "racedate";
	static final String KEY_IMAGE = "image";
	static final String KEY_FULL = "full";
	static final String KEY_HALF = "half";
	static final String KEY_TEN_K = "ten_k";
	static final String KEY_FIVE_K = "five_k";
	static final String KEY_DUAL = "dual";
	static final String KEY_TRI = "tri";
	static final String KEY_ULTRA = "ultra";
	public static final String YES = "Y";
	static final String raceXML =  "races3.xml";
	static final String logoUrl =  "http://www.leonprojects.in/Calathon/images/logo/";
	static final String routeUrl = "http://www.leonprojects.in/Calathon/images/route/";
	static final String xmlUrl =   "http://www.leonprojects.in/Calathon/xml/races.xml";
	static final String logoExtn = ".jpg";
	String xml;
	static final String xmlFile = "races.xml";
	
	
	ListView list;
    LazyAdapter adapter;
    int index = 0 ;
    int topOffset = 0;
	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setTitle("Calathon - India's Marathon Calendar");	
	
		new PrefetchXML().execute();
				
	}
	
	public void parseXML() throws IOException{
		
		  FileInputStream in = openFileInput(xmlFile);
		    InputStreamReader inputStreamReader = new InputStreamReader(in);
		    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		    StringBuilder sb = new StringBuilder();
		    String line;
		    while ((line = bufferedReader.readLine()) != null) {
		        sb.append(line);
		    }
		    inputStreamReader.close();
		
		ArrayList<HashMap<String, String>> raceList = new ArrayList<HashMap<String, String>>();
		XMLParser parser = new XMLParser();
		
		Document doc = parser.getDomElement(sb.toString()); // getting DOM element
		
		NodeList nl = doc.getElementsByTagName(KEY_RACE);
		// looping through all song nodes <song>
		for (int i = 0; i < nl.getLength(); i++) {
			// creating new HashMap
			HashMap<String, String> map = new HashMap<String, String>();
			Element e = (Element) nl.item(i);
			// adding each child node to HashMap key => value
			map.put(KEY_ID, parser.getValue(e, KEY_ID));
			map.put(KEY_TITLE, parser.getValue(e, KEY_TITLE));
			map.put(KEY_LOCATION, parser.getValue(e, KEY_LOCATION));
			map.put(KEY_RACEDATE, parser.getValue(e, KEY_RACEDATE));
			map.put(KEY_IMAGE, parser.getValue(e, KEY_IMAGE));
			map.put(KEY_FULL, parser.getValue(e, KEY_FULL));
			map.put(KEY_HALF, parser.getValue(e, KEY_HALF));
			map.put(KEY_TEN_K, parser.getValue(e, KEY_TEN_K));
			map.put(KEY_FIVE_K, parser.getValue(e, KEY_FIVE_K));
			map.put(KEY_DUAL, parser.getValue(e, KEY_DUAL));
			map.put(KEY_TRI, parser.getValue(e, KEY_TRI));
			map.put(KEY_ULTRA, parser.getValue(e, KEY_ULTRA));
			// adding HashList to ArrayList
			raceList.add(map);
		}
		
		
		
		list=(ListView)findViewById(R.id.list);
		
		
		// Getting adapter by passing xml data ArrayList
        adapter = new LazyAdapter(this,this, raceList);        
        list.setAdapter(adapter);
                
        list.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                    long arg3) {
                
                Intent i = new Intent(getApplicationContext(), RaceDetailActivity.class);
                
                TextView title_view = ((TextView)view.findViewById(R.id.idtag));
                String title_id = 	title_view.getText().toString();
                 
                i.putExtra("title_id", title_id);
                startActivity(i);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                
            }
        });
		
	}
	    private class PrefetchXML extends AsyncTask<Void, Void, Integer> {
	    	private ProgressDialog Dialog = new ProgressDialog(RaceListActivity.this);
	    	
	    	@Override
	    	protected void onPreExecute(){
	    	super.onPreExecute();
	    	Dialog.setMessage("Fetching Details...");
	        Dialog.show();
	    	
	    	}
	        @Override
	      	protected Integer doInBackground(Void... arg0) {
	 
	    		try {
	    			
	    			// defaultHttpClient
	    			DefaultHttpClient httpClient = new DefaultHttpClient();
	    			HttpPost httpPost = new HttpPost(xmlUrl);

	    			HttpResponse httpResponse = httpClient.execute(httpPost);
	    			HttpEntity httpEntity = httpResponse.getEntity();
	    			xml = EntityUtils.toString(httpEntity);
	    			
	    			if (xml != null)
	    			{
	    				FileOutputStream fos = openFileOutput(xmlFile, Context.MODE_PRIVATE);
	    				fos.write(xml.getBytes());
	    				fos.close();
	    				
	    			}

	    		} catch (UnsupportedEncodingException e) {
	    			e.printStackTrace();
	    		} catch (ClientProtocolException e) {
	    			e.printStackTrace();
	    		} catch (IOException e) {
	    			e.printStackTrace();
	    		}
	    		// return XML
	    		return 0;
	    	}
	        				
	        @Override
	        protected void onPostExecute(Integer result) {
	        	Dialog.dismiss();        
	        try {
				parseXML();
				
		//		 Toast.makeText(getApplicationContext(),
		//					"XML fetched from URL!",
		//					Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Toast.makeText(getApplicationContext(),
						"Couldn't Load Races",
						Toast.LENGTH_SHORT).show();
			}        
	        
	        }
	 }
}
