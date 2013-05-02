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

public class Child extends RootActivity implements OnClickListener {
	
	private String sHeadOfHouseholdFirstName = null;
	private String sHeadOfHouseholdLastName = null;
	private String sHeadOfHouseholdName = null;
	private String sHeadOfHouseholdUuid = null;
	private String sGuardianFirstName = null;
	private String sGuardianLastName = null;
	private String sGuardianName = null;
	private String sGuardianUuid = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.child);
		
	    if (getIntent().getExtras() != null) {
	    	sHeadOfHouseholdFirstName = getIntent().getExtras().getString(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_FIRST_NAME);
	    	sHeadOfHouseholdLastName = getIntent().getExtras().getString(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_LAST_NAME);
	    	sHeadOfHouseholdUuid = getIntent().getExtras().getString(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_UUID);
	    	sHeadOfHouseholdName = sHeadOfHouseholdFirstName + " " + sHeadOfHouseholdLastName;
	    	
	    	sGuardianFirstName = getIntent().getExtras().getString(Constants.INTENT_EXTRA_GUARDIAN_FIRST_NAME);
	    	sGuardianLastName = getIntent().getExtras().getString(Constants.INTENT_EXTRA_GUARDIAN_LAST_NAME);
	    	sGuardianUuid = getIntent().getExtras().getString(Constants.INTENT_EXTRA_GUARDIAN_UUID);
	    	sGuardianName = sGuardianFirstName + " " + sGuardianLastName;
	    }

		TextView textViewLocation = (TextView) findViewById(R.id.textViewLocation);
		textViewLocation.setText(settings.getString("village", ""));
	    
		TextView textViewHeadOfHousehold = (TextView) findViewById(R.id.textViewHeadOfHousehold);
		textViewHeadOfHousehold.setText(sHeadOfHouseholdName);
		
		TextView textViewGuardian = (TextView) findViewById(R.id.textViewGuardian);
		textViewGuardian.setText(sGuardianName);
		
		Button bttnAddChild = (Button) findViewById(R.id.buttonAddChild);
		bttnAddChild.setOnClickListener(this);
		
		Button bttnHousehold = (Button) findViewById(R.id.buttonHousehold);
		bttnHousehold.setOnClickListener(this);

		Button bttnAddCoordinates = (Button) findViewById(R.id.buttonAddCoordinates);
		bttnAddCoordinates.setOnClickListener(this);

		Button bttnSettings = (Button) findViewById(R.id.buttonSettings);
		bttnSettings.setOnClickListener(this);

		
		SQLiteDatabase db = openOrCreateDatabase(Constants.DATABASE_NAME, MODE_PRIVATE, null);
		String sql = null;
		
		sql = String.format("SELECT * FROM child WHERE uuid_hoh = '%s' AND uuid_guardian = '%s';", 
				sHeadOfHouseholdUuid, sGuardianUuid);
		
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
		            String uuid = c.getString(c.getColumnIndex("uuid_child"));
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

		
		ListView listView = (ListView) findViewById(R.id.listViewChild);
		
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
	               Intent intent = new Intent(Child.this, ChildSummary.class);
	               intent.putExtra(Constants.INTENT_EXTRA_CHILD_FIRST_NAME, valueFirstNames[position]);
	               intent.putExtra(Constants.INTENT_EXTRA_CHILD_LAST_NAME, valueLastNames[position]);
	               intent.putExtra(Constants.INTENT_EXTRA_CHILD_UUID, valueUuids[position]);
	               if (sGuardianFirstName != null)
	            	   intent.putExtra(Constants.INTENT_EXTRA_GUARDIAN_FIRST_NAME, sGuardianFirstName);
	               if (sGuardianLastName != null)
	            	   intent.putExtra(Constants.INTENT_EXTRA_GUARDIAN_LAST_NAME, sGuardianLastName);
	               if (sGuardianUuid != null)
	            	   intent.putExtra(Constants.INTENT_EXTRA_GUARDIAN_UUID, sGuardianUuid);
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
		if (v.getId() == R.id.buttonAddChild) 
		{
            Intent intent = new Intent(Child.this, ChildDetails.class);
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
            startActivity(intent);
		}
		else if (v.getId() == R.id.buttonHousehold)
		{
			startActivity(new Intent(Child.this, HeadOfHousehold.class));
		}
		else if (v.getId() == R.id.buttonAddCoordinates)
		{
			saveGPSLocation();
		}
		else if (v.getId() == R.id.buttonSettings)
		{
			startActivity(new Intent(Child.this, UserSettings.class));
		}
		else if (v.getId() == R.id.textViewHeadOfHousehold)
		{
            Intent intent = new Intent(this, HeadOfHouseholdDetails.class);
            intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_FIRST_NAME, sHeadOfHouseholdFirstName);
            intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_LAST_NAME, sHeadOfHouseholdLastName);
            intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_UUID, sHeadOfHouseholdUuid);
            startActivity(intent);

		}
		else if (v.getId() == R.id.textViewGuardian)
		{
            Intent intent = new Intent(this, GuardianDetails.class);
            intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_FIRST_NAME, sHeadOfHouseholdFirstName);
            intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_LAST_NAME, sHeadOfHouseholdLastName);
            intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_UUID, sHeadOfHouseholdUuid);
            
            intent.putExtra(Constants.INTENT_EXTRA_GUARDIAN_FIRST_NAME, sGuardianFirstName);
            intent.putExtra(Constants.INTENT_EXTRA_GUARDIAN_LAST_NAME, sGuardianLastName);
            intent.putExtra(Constants.INTENT_EXTRA_GUARDIAN_UUID, sGuardianUuid);

            startActivity(intent);

		}


			
		
	}

}
