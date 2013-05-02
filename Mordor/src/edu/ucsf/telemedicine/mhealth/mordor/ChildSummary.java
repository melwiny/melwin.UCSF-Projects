package edu.ucsf.telemedicine.mhealth.mordor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ChildSummary extends RootActivity implements OnClickListener {
	
	static final LatLng HAMBURG = new LatLng(53.558, 9.927);
	static final LatLng KIEL = new LatLng(53.551, 9.993);
	private GoogleMap map;
	  	
	private String sHeadOfHouseholdFirstName = null;
	private String sHeadOfHouseholdLastName = null;
	private String sHeadOfHouseholdName = null;
	private String sHeadOfHouseholdUuid = null;
	
	private String sGuardianFirstName = null;
	private String sGuardianLastName = null;
	private String sGuardianName = null;
	private String sGuardianUuid = null;
	
	private String sChildFirstName = null;
	private String sChildLastName = null;
	private String sChildName = null;
	private String sChildUuid = null;
	
	private String sGpsLatitude = null;
	private String sGpsLongitude = null;
			
	
	TextView textViewLocation, textViewSummary, textViewLastModified;




	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.child_summary);
	    
	    if (getIntent().getExtras() != null) {
	    	sHeadOfHouseholdFirstName = getIntent().getExtras().getString(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_FIRST_NAME);
	    	sHeadOfHouseholdLastName = getIntent().getExtras().getString(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_LAST_NAME);
	    	sHeadOfHouseholdUuid = getIntent().getExtras().getString(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_UUID);
	    	sHeadOfHouseholdName = sHeadOfHouseholdFirstName + " " + sHeadOfHouseholdLastName;
	    	
	    	sGuardianFirstName = getIntent().getExtras().getString(Constants.INTENT_EXTRA_GUARDIAN_FIRST_NAME);
	    	sGuardianLastName = getIntent().getExtras().getString(Constants.INTENT_EXTRA_GUARDIAN_LAST_NAME);
	    	sGuardianUuid = getIntent().getExtras().getString(Constants.INTENT_EXTRA_GUARDIAN_UUID);
	    	sGuardianName = sGuardianFirstName + " " + sGuardianLastName;
	    	
	    	sChildFirstName = getIntent().getExtras().getString(Constants.INTENT_EXTRA_CHILD_FIRST_NAME);
	    	sChildLastName = getIntent().getExtras().getString(Constants.INTENT_EXTRA_CHILD_LAST_NAME);
	    	sChildUuid = getIntent().getExtras().getString(Constants.INTENT_EXTRA_CHILD_UUID);
	    	sChildName = sChildFirstName + " " + sChildLastName;
	    }
	    
	    
	    Button bttnEdit = (Button) findViewById(R.id.buttonEdit);
	    bttnEdit.setOnClickListener(this);
	    
	    Button bttnHousehold = (Button) findViewById(R.id.buttonHousehold);
	    bttnHousehold.setOnClickListener(this);

	    Button bttnSettings = (Button) findViewById(R.id.buttonSettings);
	    bttnSettings.setOnClickListener(this);
	    
	    textViewLocation = (TextView) findViewById(R.id.textViewLocation);
	    textViewSummary = (TextView) findViewById(R.id.textViewSummary);
	    textViewLastModified = (TextView) findViewById(R.id.TextViewLastModified);
	    
	    initFields();

	    
	    SharedPreferences settings = this.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, 0);
	    
	    double curLatitude = Double.parseDouble(sGpsLatitude);
	    double curLongitude = Double.parseDouble(sGpsLongitude);

	    LatLng curLatLng = new LatLng(curLatitude, curLongitude);
	    
	    
	    map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
	        .getMap();
	    if (map != null)
	    {
	    	if (!sGpsLatitude.equals("0") && !sGpsLongitude.equals("0"))
	    	{
			    Marker curMarker = map.addMarker(new MarkerOptions().position(curLatLng).title(sHeadOfHouseholdName + ": " + sGpsLatitude + ", " + sGpsLongitude));
		
		    	map.moveCamera(CameraUpdateFactory.newLatLngZoom(curLatLng, 15));
		
			    // Zoom in, animating the camera.
			    map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
	    	}
	    }
	    

	  }

	@Override
	public void onClick(View v) {
		
		if (v.getId() == R.id.buttonEdit) 
		{
			Intent intent = new Intent(ChildSummary.this, ChildDetails.class);
            if (sHeadOfHouseholdFirstName != null)
            	intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_FIRST_NAME, sHeadOfHouseholdFirstName);
             if (sHeadOfHouseholdLastName != null)
            	 intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_LAST_NAME, sHeadOfHouseholdLastName);
             if (sHeadOfHouseholdUuid != null)
            	 intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_UUID, sHeadOfHouseholdUuid);
             
             if (sGuardianFirstName != null)
            	 intent.putExtra(Constants.INTENT_EXTRA_GUARDIAN_FIRST_NAME, sGuardianFirstName);
             if (sGuardianLastName != null)
            	 intent.putExtra(Constants.INTENT_EXTRA_GUARDIAN_LAST_NAME, sGuardianLastName);
             if (sGuardianUuid != null)
            	 intent.putExtra(Constants.INTENT_EXTRA_GUARDIAN_UUID, sGuardianUuid);
             
             if (sChildFirstName != null)
            	 intent.putExtra(Constants.INTENT_EXTRA_CHILD_FIRST_NAME, sChildFirstName);
             if (sChildLastName != null)
            	 intent.putExtra(Constants.INTENT_EXTRA_CHILD_LAST_NAME, sChildLastName);
             if (sChildUuid != null)
            	 intent.putExtra(Constants.INTENT_EXTRA_CHILD_UUID, sChildUuid);

            startActivity(intent);
		}
		else if (v.getId() == R.id.buttonHousehold)
		{
			startActivity(new Intent(ChildSummary.this, HeadOfHousehold.class));
		}
		else if (v.getId() == R.id.buttonSettings)
		{
			startActivity(new Intent(ChildSummary.this, UserSettings.class));
		}
		
	}
	
	private void initFields()
	{
		String firstName = null, lastName = null, nickname = null, ageUnit = null, gender = null, status = null, note = null, dateCreated = null, dateLastModified = null;
		String hohFirstName = null, hohLastName = null, gps = null, village = null, country = null;
		String guardianFirstName = null, guardianLastName = null;
		String uuidAgentCreated = null, uuidAgentModified = null;
		String agentCreatedFirstName = null, agentCreatedLastName = null;
		String agentModifiedFirstName = null, agentModifiedLastName = null;
		int age = 0;

		SQLiteDatabase db = openOrCreateDatabase(Constants.DATABASE_NAME, MODE_PRIVATE, null);
		String sql = null;
		sql = String.format("SELECT c.first_name AS first_name, c.last_name AS last_name, " +
				"c.nickname AS nickname, c.age AS age, c.age_unit AS age_unit, c.gender AS gender, c.status AS status, " +
				"c.note AS note, c.date_created AS date_created, c.date_last_modified AS date_last_modified, " +
				"c.uuid_agent_created AS uuid_agent_created, c.uuid_agent_modified AS uuid_agent_modified, " +
				"h.first_name AS hoh_first_name, h.last_name AS hoh_last_name, " +
				"h.gps AS gps, h.village AS village, h.country AS country, " +
				"g.first_name AS guardian_first_name, g.last_name AS guardian_last_name " + 
				"FROM child c, head_of_household h, guardian g WHERE " +
				"c.uuid_child = '%s' AND c.uuid_hoh = h.uuid_hoh AND c.uuid_guardian = g.uuid_guardian;", sChildUuid);

		
		
		Cursor c = db.rawQuery(sql, null);
		int results = c.getCount();
		if (results == 0)
			return;
		
		
		if (c != null ) 
		{
		    if  (c.moveToFirst()) 
		    {

		        do {
		        	firstName = c.getString(c.getColumnIndex("first_name"));
		            lastName = c.getString(c.getColumnIndex("last_name"));
		            nickname = c.getString(c.getColumnIndex("nickname"));
		            age = c.getInt(c.getColumnIndex("age"));
		            ageUnit = c.getString(c.getColumnIndex("age_unit"));
		            gender = c.getString(c.getColumnIndex("gender"));
		            status = c.getString(c.getColumnIndex("status"));
		            note = c.getString(c.getColumnIndex("note"));
		            uuidAgentCreated = c.getString(c.getColumnIndex("uuid_agent_created"));
		            uuidAgentModified = c.getString(c.getColumnIndex("uuid_agent_modified"));
		            dateCreated = c.getString(c.getColumnIndex("date_created"));
		            dateLastModified = c.getString(c.getColumnIndex("date_last_modified"));
		            hohFirstName = c.getString(c.getColumnIndex("hoh_first_name"));
		            hohLastName = c.getString(c.getColumnIndex("hoh_last_name"));
		            gps = c.getString(c.getColumnIndex("gps"));
		            village = c.getString(c.getColumnIndex("village"));
		            country = c.getString(c.getColumnIndex("country"));
		            guardianFirstName = c.getString(c.getColumnIndex("guardian_first_name"));
		            guardianLastName = c.getString(c.getColumnIndex("guardian_last_name"));
		            
		            break;
		            

		        }while (c.moveToNext());
		    } 
		}
		
		sql = String.format("SELECT * from agent WHERE uuid_agent = '%s'; ", uuidAgentCreated);
		c = db.rawQuery(sql, null);
		results = c.getCount();
		if (results > 0)
		{
			{
			    if  (c.moveToFirst()) 
			    {

			        do {
			        	agentCreatedFirstName = c.getString(c.getColumnIndex("first_name"));
			            agentCreatedLastName = c.getString(c.getColumnIndex("last_name"));
			            break;
			        }while (c.moveToNext());
			    }
			}
		}
		
		sql = String.format("SELECT * from agent WHERE uuid_agent = '%s'; ", uuidAgentModified);
		c = db.rawQuery(sql, null);
		results = c.getCount();
		if (results > 0)
		{
			{
			    if  (c.moveToFirst()) 
			    {

			        do {
			        	agentModifiedFirstName = c.getString(c.getColumnIndex("first_name"));
			            agentModifiedLastName = c.getString(c.getColumnIndex("last_name"));
			            break;
			        }while (c.moveToNext());
			    }
			}
		}

		
		c.close();
		db.close();
		
		if ((gps != null) && !gps.equals(""))
		{
			String[] latlongChild = gps.split(", ");
			sGpsLatitude = latlongChild[0];
			sGpsLongitude = latlongChild[1];
		}
		else
		{
			sGpsLatitude = "0";
			sGpsLongitude = "0";
		}
		
		String location = village + ", " + hohFirstName + " " + hohLastName + ", " + guardianFirstName + " " + guardianLastName;
		textViewLocation.setText(location);
		
		String summary = sChildFirstName + " " + sChildLastName + "\n" +
		nickname + "\n" +
		gender + "\n" +
		Integer.toString(age) + " " + ageUnit + '\n' +
		"Status: " + status;
		
		textViewSummary.setText(summary);
		
		String lastMordified = "Added: " + dateCreated + " By: " + agentCreatedFirstName + " " + agentCreatedLastName + "\n" +
		"Last Updated: " + dateLastModified + " By: " + agentModifiedFirstName + " " + agentModifiedLastName;
		
		textViewLastModified.setText(lastMordified);
	}

}
