package edu.ucsf.telemedicine.mhealth.mordor;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class Guardian extends RootActivity implements OnClickListener {
	
	private String sHeadOfHouseholdFirstName = null;
	private String sHeadOfHouseholdLastName = null;
	private String sHeadOfHouseholdName = null;
	private String sHeadOfHouseholdUuid = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		setContentView(R.layout.guardian);
				
	    if (getIntent().getExtras() != null) {
	    	sHeadOfHouseholdFirstName = getIntent().getExtras().getString(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_FIRST_NAME);
	    	sHeadOfHouseholdLastName = getIntent().getExtras().getString(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_LAST_NAME);
	    	sHeadOfHouseholdUuid = getIntent().getExtras().getString(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_UUID);
	    	sHeadOfHouseholdName = sHeadOfHouseholdFirstName + " " + sHeadOfHouseholdLastName;
	    }
	    
		TextView textViewLocation = (TextView) findViewById(R.id.textViewLocation);
		textViewLocation.setText(settings.getString("village", ""));

		TextView textView = (TextView) findViewById(R.id.textViewHeadOfHousehold);
		textView.setText(sHeadOfHouseholdName);
		
		Button bttnAddGuardian = (Button) findViewById(R.id.buttonAddGuardian);
		bttnAddGuardian.setOnClickListener(this);

		Button bttnHousehold = (Button) findViewById(R.id.buttonHousehold);
		bttnHousehold.setOnClickListener(this);

		Button bttnAddCoordinates = (Button) findViewById(R.id.buttonAddCoordinates);
		bttnAddCoordinates.setOnClickListener(this);

		Button bttnSettings = (Button) findViewById(R.id.buttonSettings);
		bttnSettings.setOnClickListener(this);

		
		SQLiteDatabase db = openOrCreateDatabase(Constants.DATABASE_NAME, MODE_PRIVATE, null);
		String sql = null;
		
		sql = String.format("SELECT * FROM guardian WHERE uuid_hoh = '%s';", sHeadOfHouseholdUuid);
		
		Cursor c = db.rawQuery(sql, null);
		int results = c.getCount();
		String[] firstNames = null;
		String[] lastNames = null;
		String[] fullNames = null;
		String[] uuids = null;
		if (results > 0)
		{
			firstNames = new String[results];
			lastNames = new String[results];
			fullNames = new String[results];
			uuids = new String[results];
		}
		else
			return;
		
		if (c != null ) 
		{
		    if  (c.moveToFirst()) 
		    {
		    	int n = 0;
		        do {
		            String firstName = c.getString(c.getColumnIndex("first_name"));
		            String lastName = c.getString(c.getColumnIndex("last_name"));
		            String uuid = c.getString(c.getColumnIndex("uuid_guardian"));
		            firstNames[n] = firstName;
		            lastNames[n] = lastName;
		            fullNames[n] = firstName + " " + lastName;
		            uuids[n] = uuid;
		            n++;
		        }while (c.moveToNext());
		    } 
		}
		
		c.close();
		db.close();

		
		ListView listView = (ListView) findViewById(R.id.listViewGuardian);

		final String[] valueFirstNames = firstNames;
		final String[] valueLastNames = lastNames;
		final String[] values = fullNames;
		final String[] valueUuids = uuids;
		
		
		// Define a new Adapter
		// First parameter - Context
		// Second parameter - Layout for the row
		// Third parameter - ID of the TextView to which the data is written
		// Forth - the Array of data
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		  android.R.layout.simple_list_item_1, android.R.id.text1, values);

		
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
			{
	               Intent intent = new Intent(Guardian.this, Child.class);
	               intent.putExtra(Constants.INTENT_EXTRA_GUARDIAN_FIRST_NAME, valueFirstNames[position]);
	               intent.putExtra(Constants.INTENT_EXTRA_GUARDIAN_LAST_NAME, valueLastNames[position]);
	               intent.putExtra(Constants.INTENT_EXTRA_GUARDIAN_UUID, valueUuids[position]);
	               if (sHeadOfHouseholdFirstName != null)
	            	   intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_FIRST_NAME, sHeadOfHouseholdFirstName);
	               if (sHeadOfHouseholdLastName != null)
	            	   intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_LAST_NAME, sHeadOfHouseholdLastName);
	               if (sHeadOfHouseholdUuid != null)
	            	   intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_UUID, sHeadOfHouseholdUuid);
	               startActivity(intent);
			}
			});

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.buttonAddGuardian)
		{
            Intent intent = new Intent(Guardian.this, GuardianDetails.class);
            if (sHeadOfHouseholdFirstName != null)
         	   intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_FIRST_NAME, sHeadOfHouseholdFirstName);
            if (sHeadOfHouseholdLastName != null)
          	   intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_LAST_NAME, sHeadOfHouseholdLastName);
            if (sHeadOfHouseholdUuid != null)
           	   intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_UUID, sHeadOfHouseholdUuid);
            startActivity(intent);
		}
		else if (v.getId() == R.id.buttonHousehold)
		{
			startActivity(new Intent(Guardian.this, HeadOfHousehold.class));
		}
		else if (v.getId() == R.id.buttonAddCoordinates)
		{
			saveGPSLocation();
		}
		else if (v.getId() == R.id.buttonSettings)
		{
			startActivity(new Intent(Guardian.this, UserSettings.class));
		}
		else if (v.getId() == R.id.textViewHeadOfHousehold)
		{
            Intent intent = new Intent(this, HeadOfHouseholdDetails.class);
            intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_FIRST_NAME, sHeadOfHouseholdFirstName);
            intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_LAST_NAME, sHeadOfHouseholdLastName);
            intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_UUID, sHeadOfHouseholdUuid);
            startActivity(intent);

		}

			
		
	}

}
