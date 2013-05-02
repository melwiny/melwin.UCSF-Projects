package edu.ucsf.telemedicine.mhealth.mordor;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.CompoundButton;
import android.widget.TextView;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;


public class HeadOfHouseholdDetails extends RootActivity implements CompoundButton.OnCheckedChangeListener, OnClickListener {
	
	private String sHeadOfHouseholdFirstName = null, sHeadOfHouseholdLastName = null, sHeadOfHouseholdUuid = null;
	EditText editTextFirstName, editTextLastName, editTextAge, editTextNote;
	String sGender = "Male";
	Switch switchMaleFemale = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.head_of_household_details);
		
	    if (getIntent().getExtras() != null) 
	    {
	    	sHeadOfHouseholdFirstName = getIntent().getExtras().getString(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_FIRST_NAME);
	    	sHeadOfHouseholdLastName = getIntent().getExtras().getString(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_LAST_NAME);
	    	sHeadOfHouseholdUuid = getIntent().getExtras().getString(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_UUID);
	    }
		
	    switchMaleFemale = (Switch) findViewById(R.id.switch_male_female);
        if (switchMaleFemale != null) {
        	switchMaleFemale.setOnCheckedChangeListener(this);
        }
        
        TextView textViewLocationDetails = (TextView) findViewById(R.id.textViewLocationDetails);
        textViewLocationDetails.setText(settings.getString("village", ""));
        
        Button bttnSave = (Button) findViewById(R.id.buttonSave);
        bttnSave.setOnClickListener(this);
        
        Button bttnHousehold = (Button) findViewById(R.id.buttonHousehold);
        bttnHousehold.setOnClickListener(this);

		Button bttnAddCoordinates = (Button) findViewById(R.id.buttonAddCoordinates);
		bttnAddCoordinates.setOnClickListener(this);

        Button bttnSettings = (Button) findViewById(R.id.buttonSettings);
        bttnSettings.setOnClickListener(this);
        
        editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        editTextLastName = (EditText) findViewById(R.id.editTextLastName);
        editTextAge = (EditText) findViewById(R.id.editTextAge);
        editTextNote = (EditText) findViewById(R.id.editTextNote);
        
        if (sHeadOfHouseholdUuid != null)
        	initFields();

    }
 
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
       if (isChecked)
    	   sGender = "Female";
       else
    	   sGender = "Male";
    }

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.buttonSave)
		{
			if (onSaveClicked() == true)
			{
	            Intent intent = new Intent(this, GuardianDetails.class);
	            intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_FIRST_NAME, sHeadOfHouseholdFirstName);
	            intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_LAST_NAME, sHeadOfHouseholdLastName);
	            intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_UUID, sHeadOfHouseholdUuid);
	            startActivity(intent);
			}
		}
		else if (v.getId() == R.id.buttonHousehold)
		{
			Intent intent = new Intent(HeadOfHouseholdDetails.this, HeadOfHousehold.class);
			startActivity(intent);

			
		}
		else if (v.getId() == R.id.buttonAddCoordinates)
		{
			saveGPSLocation();
		}
		else if (v.getId() == R.id.buttonSettings)
		{
			startActivity(new Intent(HeadOfHouseholdDetails.this, UserSettings.class));
		}

	}
		
		
	private boolean onSaveClicked()
	{
		String sFirstName = editTextFirstName.getText().toString();
		String sLastName = editTextLastName.getText().toString();
		String sAge = editTextAge.getText().toString();
		String sUuidAgent = settings.getString(Constants.SETTINGS_KEY_UUID_AGENT, "");
		int age = 0;
		
		if (sFirstName.isEmpty() || sLastName.isEmpty())
		{
			showToast("Please enter First and Last name");
			return false;
		}
		
		if (sAge.isEmpty() || sAge.equals(""))
			age = 0;
		else
			age = Integer.parseInt(sAge);

		
		String curLatitude = settings.getString("latitude", "");
		String curLongitude = settings.getString("longitude", "");
		String curGPS = "";
		if (!curLatitude.equals("") && !curLongitude.equals(""))
			curGPS = settings.getString("latitude", "") + ", " +  settings.getString("longitude", "");
		else
			showToast("Please add coordinates and re-save!");
		
		Date date = new Date();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sDateTime = formatter.format(date);
		
		String sVillage = settings.getString(Constants.SETTINGS_KEY_VILLAGE, "");
		String sCountry = Constants.findCountryOfVillage(sVillage);
				
		SQLiteDatabase db = openOrCreateDatabase(Constants.DATABASE_NAME, MODE_PRIVATE, null);
		String sql = null;
		
		
		sql = String.format("SELECT * FROM head_of_household WHERE first_name = '%s' AND last_name = '%s' AND village = '%s';",
				sFirstName,
				sLastName, 
				sVillage);
		
		Cursor cursor = db.rawQuery(sql, null);
		int results = cursor.getCount();
		//cursor.moveToFirst();
		//Log.d("Mordor", cursor.getString(cursor.getColumnIndex("first_name"))));

		if (results == 0)
		{
			sHeadOfHouseholdUuid = generateUUID();
			sHeadOfHouseholdFirstName = sFirstName;
			sHeadOfHouseholdLastName = sLastName;

			sql = String.format("INSERT INTO head_of_household " +
					" (uuid_hoh, first_name, last_name, age, gender, note, gps, village, country, date_created, date_last_modified, uuid_agent_created, uuid_agent_modified) " +
					" VALUES " +
					"('%s', '%s', '%s', %d, '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');", 
					sHeadOfHouseholdUuid,
					sFirstName, 
					sLastName, 
					age, 
					sGender, 
					editTextNote.getText().toString(),
					curGPS,
					sVillage,
					sCountry,
					sDateTime,
					sDateTime,
					sUuidAgent,
					sUuidAgent);
			dbExecSQL(db, sql);
			
			showToast("Added new Head of Household: " + sFirstName + " " + sLastName);
		}
		else
		{
			
			cursor.moveToFirst();
			String subjectUuid = cursor.getString(cursor.getColumnIndex("uuid_hoh"));

			sql = String.format("UPDATE head_of_household " +
					"SET age = %d, " +
					"gender = '%s', " +
					"note = '%s', " +
					"gps = '%s', " +
					"village = '%s', " +
					"country = '%s', " +
					"date_last_modified = '%s', " +
					"uuid_agent_modified = '%s' " +
					"WHERE uuid_hoh = '%s';",
					age, 
					sGender, 
					editTextNote.getText().toString(),
					curGPS,
					sVillage,
					sCountry,
					sDateTime,
					sUuidAgent,
					subjectUuid);

			dbExecSQL(db, sql);
			
			showToast("Updated Head of Household: " + sFirstName + " " + sLastName);
		}
		db.close();
		return true;

	}
	
	private void initFields()
	{
		String firstName = null, lastName = null, age = null, gender = null, note = null;
		String gps = null, village = null, country = null;
		String dateCreated = null, dateLastModified = null;
		
		String uuidAgentCreated = null, uuidAgentModified = null;
		String agentCreatedFirstName = null, agentCreatedLastName = null;
		String agentModifiedFirstName = null, agentModifiedLastName = null;;
		
		
		SQLiteDatabase db = openOrCreateDatabase(Constants.DATABASE_NAME, MODE_PRIVATE, null);
		String sql = null;
		sql = String.format("SELECT * from head_of_household WHERE uuid_hoh = '%s';", sHeadOfHouseholdUuid);
		
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
		            age = c.getString(c.getColumnIndex("age"));
		            gender = c.getString(c.getColumnIndex("gender"));
		            note = c.getString(c.getColumnIndex("note"));
		            gps = c.getString(c.getColumnIndex("gps"));
		            village = c.getString(c.getColumnIndex("village"));
		            country = c.getString(c.getColumnIndex("country"));
		            //uuidAgentCreated = c.getString(c.getColumnIndex("uuid_agent_created"));
		            //uuidAgentModified = c.getString(c.getColumnIndex("uuid_agent_modified"));
		            dateCreated = c.getString(c.getColumnIndex("date_created"));
		            dateLastModified = c.getString(c.getColumnIndex("date_last_modified"));
		            
		            break;
		            

		        }while (c.moveToNext());
		    } 
		}
		c.close();
		db.close();
		
		editTextFirstName.setText(firstName);
		editTextLastName.setText(lastName);
		editTextAge.setText(age);
		editTextNote.setText(note);
		
		if (gender.equals("Male"))
			switchMaleFemale.setChecked(false);
		else
			switchMaleFemale.setChecked(true);

	}

}
