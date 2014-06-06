package com.leonproject.antelope2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * An activity representing a single Race detail screen. This activity is only
 * used on handset devices. On tablet-size devices, item details are presented
 * side-by-side with a list of items in a {@link RaceListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link RaceDetailFragment}.
 */
public class RaceDetailActivity extends Activity {
	

	static final String KEY_DESC = "desc";
	static final String KEY_STATE = "state";
	static final String KEY_ST_DATE = "reg_st_date";
	static final String KEY_END_DATE = "reg_end_date";
	static final String KEY_UPPER_FEES = "upper_fees";
	static final String KEY_LOWER_FEES = "lower_fees";
	static final String KEY_WEBSITE = "website";
	static final String KEY_ROUTE_MAP = "route";
	public ImageLoader imageLoader; 
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_race_detail);
		
	    getActionBar().setDisplayHomeAsUpEnabled(false);
	    imageLoader=new ImageLoader(this.getApplicationContext());
	    
		
		Intent list_intent = getIntent();
		String title_id = list_intent.getStringExtra("title_id");
		
		XMLParser parser = new XMLParser();
		StringBuilder sb = new StringBuilder();
		
		try {
			
		FileInputStream in = openFileInput(RaceListActivity.xmlFile);
	    InputStreamReader inputStreamReader = new InputStreamReader(in);
	    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	    String line;
	    while ((line = bufferedReader.readLine()) != null) {
	        sb.append(line);
	    }
	    inputStreamReader.close();
		}
		catch (IOException e){
			Toast.makeText(getApplicationContext(),
					"Couldn't Load Race Detail",
					Toast.LENGTH_SHORT).show();
		}
		
		Document doc = parser.getDomElement(sb.toString()); // getting DOM element
		
		NodeList nl = doc.getElementsByTagName(RaceListActivity.KEY_RACE);
		// looping through all race nodes <song>
		for (int i = 0; i < nl.getLength(); i++) {
			// creating new HashMap
			Element e = (Element) nl.item(i);
			if (parser.getValue(e, RaceListActivity.KEY_ID).toString().equals(title_id))
			{
				/*Set the Title of the detail page with the Race Name*/
				String title_string  = parser.getValue(e,RaceListActivity.KEY_TITLE);
				setTitle(title_string);
				
				/*Set the race in the above the event tab*/ 
				ImageView thumb_image=(ImageView) findViewById(R.id.detail_image); // thumb image
				//String thumbnail_string = String.valueOf(parser.getValue(e,RaceListActivity.KEY_IMAGE));
				//thumb_image.setImageResource(getImageId(thumbnail_string));
				imageLoader.DisplayImage(RaceListActivity.logoUrl+ String.valueOf(parser.getValue(e,RaceListActivity.KEY_IMAGE)) + RaceListActivity.logoExtn, thumb_image);
		        
				
		        TextView info = (TextView) findViewById(R.id.detail_info);
		        info.setText(parser.getValue(e,KEY_DESC).toString());
		        
		        TextView racedate = (TextView) findViewById(R.id.detail_racedate);
		        String race_date_string  = parser.getValue(e, RaceListActivity.KEY_RACEDATE);
		        racedate.setText(race_date_string);
		        
		        TextView racelocation = (TextView) findViewById(R.id.detail_racelocation);
		        String race_location_string = parser.getValue(e, RaceListActivity.KEY_LOCATION) + ", " + parser.getValue(e, KEY_STATE);
		        racelocation.setText(race_location_string);
		        
		        TextView racetype = (TextView) findViewById(R.id.detail_racetype);
		        String race_type_string = "";
		        if (parser.getValue(e,RaceListActivity.KEY_FULL).toString().equals("Y"))
		        	race_type_string += "Full, ";
		        
		        if (parser.getValue(e,RaceListActivity.KEY_HALF).toString().equals("Y"))
		        	race_type_string += "Half, ";
		        if (parser.getValue(e,RaceListActivity.KEY_TEN_K).toString().equals("Y"))
		        	race_type_string += "10K, ";
		        if (parser.getValue(e,RaceListActivity.KEY_FIVE_K).toString().equals("Y"))
		        	race_type_string += "5K, ";
		        if (parser.getValue(e,RaceListActivity.KEY_DUAL).toString().equals("Y"))
		        	race_type_string += "Duathlon, ";
		        if (parser.getValue(e,RaceListActivity.KEY_TRI).toString().equals("Y"))
		        	race_type_string += "Triathlon, ";
		        if (parser.getValue(e,RaceListActivity.KEY_ULTRA).toString().equals("Y"))
		        	race_type_string += "Ultra, ";
		        
		        /*Removing the trailing blank and comma*/
		        race_type_string.trim();
		        race_type_string = race_type_string.substring(0, race_type_string.length() -2 );
		        racetype.setText(race_type_string);
		        
		        /* Setting the correct Registration dates depending on the inputs */
		        
		        TextView reg_date = (TextView) findViewById(R.id.detail_regdate);
		        String reg_start_date = parser.getValue(e,KEY_ST_DATE).toString() ;
		        String reg_end_date   = parser.getValue(e,KEY_END_DATE).toString();
		        String reg_date_limit = "";
		        
		        if (reg_start_date.equalsIgnoreCase("NA") && reg_end_date.equalsIgnoreCase("NA"))
		        	reg_date_limit = "Not Available";
		        else if (reg_start_date.equalsIgnoreCase("NA"))
		        		reg_date_limit = reg_end_date;
		        else if (reg_end_date.equalsIgnoreCase("NA"))
		        		reg_date_limit = reg_start_date;
		        else   reg_date_limit = reg_start_date + " - " + reg_end_date;
		        
		        reg_date.setText(reg_date_limit);
		        
		        /* Setting the correct display of the registration fees */
		        
		        TextView reg_fees = (TextView) findViewById(R.id.detail_racefees);
		        String reg_fees_limit = "";
		        String reg_lower_fees = parser.getValue(e, KEY_LOWER_FEES).toString();
		        String reg_upper_fees = parser.getValue(e, KEY_UPPER_FEES).toString();
		        
		        if (reg_lower_fees.equalsIgnoreCase("NA") && reg_upper_fees.equalsIgnoreCase("NA"))
		        	reg_fees_limit = "Not Available";
		        else if (reg_lower_fees.equalsIgnoreCase("NA") && reg_upper_fees.equalsIgnoreCase("FREE"))
		        		reg_fees_limit = reg_upper_fees;
		        	else if (reg_lower_fees.equalsIgnoreCase("NA"))
		        			reg_fees_limit = "Rs " + reg_upper_fees;
		        			else  reg_fees_limit = "Rs " + reg_lower_fees + " - Rs " + reg_upper_fees;
		        
		        reg_fees.setText(reg_fees_limit);
		        
		        TextView web_link = (TextView) findViewById(R.id.header_racewebsite);
		        String official_link = "<a href='"+ parser.getValue(e,KEY_WEBSITE).toString()+"'>Link To Official Website</a>";
		     
		        
		        web_link.setText( Html.fromHtml(official_link));
		        web_link.setMovementMethod(LinkMovementMethod.getInstance());
		       
		       ImageView routemap_image=(ImageView) findViewById(R.id.routemap_image); // thumb image
			   imageLoader.DisplayImage(RaceListActivity.routeUrl+ String.valueOf(parser.getValue(e,KEY_ROUTE_MAP)) + RaceListActivity.logoExtn, routemap_image);	        
		       
			}
			
		}       
	        
	}
	public int getImageId(String imageName) {
        return getResources().getIdentifier(imageName, "drawable", getPackageName());
	}
	
}
