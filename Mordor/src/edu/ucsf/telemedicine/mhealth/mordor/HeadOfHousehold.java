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

public class HeadOfHousehold extends RootActivity implements OnClickListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.head_of_household);
		

		
		TextView textViewLocation = (TextView) findViewById(R.id.textViewLocation);
		textViewLocation.setText(settings.getString("village", ""));
		
		Button bttn = (Button) findViewById(R.id.buttonAddHeadOfHousehold);
		bttn.setOnClickListener(this);
		
		Button bttnSettings = (Button) findViewById(R.id.buttonSettings);
		bttnSettings.setOnClickListener(this);
		
		String village = settings.getString(Constants.SETTINGS_KEY_VILLAGE, "");
		
		SQLiteDatabase db = openOrCreateDatabase(Constants.DATABASE_NAME, MODE_PRIVATE, null);
		String sql = null;
		
		sql = String.format("SELECT * FROM head_of_household WHERE village = '%s';", village);
		
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
		            String uuid = c.getString(c.getColumnIndex("uuid_hoh"));
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


		
		ListView listView = (ListView) findViewById(R.id.listViewHeadOfHousehold);
		
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
	               Intent intent = new Intent(HeadOfHousehold.this, HouseholdReview.class);
	               intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_FIRST_NAME, valueFirstNames[position]);
	               intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_LAST_NAME, valueLastNames[position]);
	               intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_UUID, valueUuids[position]);
	               startActivity(intent);
			}
		});


	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.buttonSettings)
		{
			startActivity(new Intent(HeadOfHousehold.this, UserSettings.class));
		}
		else if (v.getId() == R.id.buttonAddHeadOfHousehold)
		{
			startActivity(new Intent(this,  HeadOfHouseholdDetails.class));
		}
	}
}
