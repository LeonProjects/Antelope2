package com.leonproject.antelope2;

import java.util.ArrayList;
import com.leonproject.antelope2.R;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class LazyAdapter extends BaseAdapter {
    
	private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    private Context mContext;
    public final static int  activeRace = Color.parseColor("#E12828");
    
    public final static int  inactiveRace = Color.parseColor("#808080");
    
    
    public LazyAdapter(Context c,Activity a, ArrayList<HashMap<String, String>> d) {
    	mContext = c;
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_row, null);

        TextView title = (TextView)vi.findViewById(R.id.title); // title
        TextView location = (TextView)vi.findViewById(R.id.location); // location name
        TextView racedate = (TextView)vi.findViewById(R.id.racedate); // race date
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image
        TextView full = (TextView)vi.findViewById(R.id.fmtag);
        TextView half = (TextView)vi.findViewById(R.id.hmtag);
        TextView ten_k = (TextView)vi.findViewById(R.id.tenktag);
        TextView five_k = (TextView)vi.findViewById(R.id.fivektag);
        TextView dual = (TextView)vi.findViewById(R.id.dualtag);
        TextView tri = (TextView)vi.findViewById(R.id.tritag);
        TextView ultra = (TextView)vi.findViewById(R.id.ultratag);
        TextView id   = (TextView)vi.findViewById(R.id.idtag);
        
        HashMap<String, String> race = new HashMap<String, String>();
        race = data.get(position);
        
        // Setting all values in listview
        title.setText(race.get(RaceListActivity.KEY_TITLE));
        location.setText(race.get(RaceListActivity.KEY_LOCATION));
        String race_date_string  = race.get(RaceListActivity.KEY_RACEDATE);
        race_date_string = race_date_string.trim();
        race_date_string = race_date_string.substring(0, race_date_string.length() - 5);
        
        
        racedate.setText(race_date_string);
        
        id.setText(race.get(RaceListActivity.KEY_ID));
        
        if (race.get(RaceListActivity.KEY_FULL).toString().equals(RaceListActivity.YES))
        	full.setTextColor(activeRace);
        else full.setTextColor(inactiveRace);     
        
        if (race.get(RaceListActivity.KEY_HALF).toString().equals(RaceListActivity.YES))
        	half.setTextColor(activeRace);
        else half.setTextColor(inactiveRace);
        
        
        if (race.get(RaceListActivity.KEY_TEN_K).toString().equals(RaceListActivity.YES))
        	ten_k.setTextColor(activeRace);
        else ten_k.setTextColor(inactiveRace);
        
        
        if (race.get(RaceListActivity.KEY_FIVE_K).toString().equals(RaceListActivity.YES))
        	five_k.setTextColor(activeRace);
        else five_k.setTextColor(inactiveRace);
        
        
        if (race.get(RaceListActivity.KEY_DUAL).toString().equals(RaceListActivity.YES))
        	dual.setTextColor(activeRace);
        else dual.setTextColor(inactiveRace);
        
        
        if (race.get(RaceListActivity.KEY_TRI).toString().equals(RaceListActivity.YES))
        	tri.setTextColor(activeRace);
        else tri.setTextColor(inactiveRace);
        
        
        if (race.get(RaceListActivity.KEY_ULTRA).toString().equals(RaceListActivity.YES))
        	ultra.setTextColor(activeRace);
        else ultra.setTextColor(inactiveRace);
        	
      /*  	
        String thumbnail_string = String.valueOf(race.get(RaceListActivity.KEY_IMAGE));
                
        thumb_image.setImageResource(getImageId(thumbnail_string));
        */
        
        /* TODO Below to be used when images on web.Cache the data and save on disk*/
        imageLoader.DisplayImage(RaceListActivity.logoUrl+ race.get(RaceListActivity.KEY_IMAGE)+ RaceListActivity.logoExtn, thumb_image);
     
        return vi;
    }
    
   public int getImageId(String imageName) {
        return mContext.getResources().getIdentifier(imageName, "drawable", mContext.getPackageName());
       
        
    }
    
}