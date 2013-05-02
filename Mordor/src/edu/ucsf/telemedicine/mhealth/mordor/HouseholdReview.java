package edu.ucsf.telemedicine.mhealth.mordor;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HouseholdReview extends RootActivity implements OnClickListener {
	
	private String sHeadOfHouseholdFirstName = null;
	private String sHeadOfHouseholdLastName = null;
	private String sHeadOfHouseholdUuid = null;
	
	private String sGuardianFirstName = null;
	private String sGuardianLastName = null;
	private String sGuardianUuid = null;
	
	private String sChildFirstName = null;
	private String sChildLastName = null;
	private String sChildUuid = null;
	
	ArrayList<HeadOfHouseholdObj> headOfHouseholdList = null;
	ArrayList<String> attributes = null;
	ArrayList<String> uuids = null;
	
	boolean bHohCensusCompleted = false;
	
	private class HeadOfHouseholdObj{
		String first_name;
		String last_name;
		String uuid;
		ArrayList<GuardianObj> guardianList = null;
		ArrayList<ChildObj> childList = null;
		
		public HeadOfHouseholdObj(String firstName, String lastName, String Uuid)
		{
			this.first_name = firstName;
			this.last_name = lastName;
			this.uuid = Uuid;
			guardianList = new ArrayList<GuardianObj>();
			childList = new ArrayList<ChildObj>();
		}
		private boolean isGuardianExist(String Uuid)
		{
			for (int i = 0; i < guardianList.size(); i++)
			{
				   GuardianObj tmpGuardian = guardianList.get(i);
				   if (tmpGuardian.uuid.equals(Uuid))
					   return true;
			}
			return false;
		}
		public void addGuardian(GuardianObj obj)
		{
			if (!isGuardianExist(obj.uuid))
				guardianList.add(obj);
		}
		public GuardianObj getGuardian(String Uuid)
		{
			for (int i = 0; i < guardianList.size(); i++)
			{
				   GuardianObj tmpGuardian = guardianList.get(i);
				   if (tmpGuardian.uuid.equals(Uuid))
					   return tmpGuardian;
			}
			return null;	
		}
		private boolean isChildExist(String Uuid)
		{
			for (int i = 0; i < childList.size(); i++)
			{
				   ChildObj tmpChild = childList.get(i);
				   if (tmpChild.uuid.equals(Uuid))
					   return true;
			}
			return false;
		}
		public void addChild(ChildObj obj)
		{
			if (!isChildExist(obj.uuid))
				childList.add(obj);
		}
		public ChildObj getChild(String Uuid)
		{
			for (int i = 0; i < guardianList.size(); i++)
			{
				   ChildObj tmpChild = childList.get(i);
				   if (tmpChild.uuid.equals(Uuid))
					   return tmpChild;
			}
			return null;	
		}


	}

	private class GuardianObj{
		String first_name;
		String last_name;
		String uuid;
		ArrayList<ChildObj> childList = null;
		public GuardianObj(String firstName, String lastName, String Uuid)
		{
			this.first_name = firstName;
			this.last_name = lastName;
			this.uuid = Uuid;
			childList = new ArrayList<ChildObj>();
		}
		private boolean isChildExist(String Uuid)
		{
			for (int i = 0; i < childList.size(); i++)
			{
				   ChildObj tmpChild = childList.get(i);
				   if (tmpChild.uuid.equals(Uuid))
					   return true;
			}
			return false;
		}
		public void addChild(ChildObj obj)
		{
			if (!isChildExist(obj.uuid))
				childList.add(obj);
		}
		public ChildObj getChild(String Uuid)
		{
			for (int i = 0; i < childList.size(); i++)
			{
				   ChildObj tmpChild = childList.get(i);
				   if (tmpChild.uuid.equals(Uuid))
					   return tmpChild;
			}
			return null;	
		}
	}

	private class ChildObj{
		String first_name;
		String last_name;
		String uuid;
		String age;
		String age_unit;
		String gender;
		public ChildObj(String firstName, String lastName, String Uuid, String Age, String ageUnit, String Gender)
		{
			this.first_name = firstName;
			this.last_name = lastName;
			this.uuid = Uuid;
			this.age = Age;
			this.age_unit = ageUnit;
			this.gender = Gender;
		}
		
	}

	
	private class ListItem1Adapter extends ArrayAdapter<String> implements OnClickListener{
		String[] items = null;
		public ListItem1Adapter(Context context, int resource,
				int textViewResourceId, String[] objects) {
			super(context, resource, textViewResourceId, objects);
			items = objects;
		}

		

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View row = inflater.inflate(R.layout.household_review_list_item_1, parent, false);

			if ((position % 2) == 0)
				row.setBackgroundColor(0xd3d3d3ff);
			TextView textView = (TextView) row.findViewById(R.id.textViewTitle);
			textView.setPadding(10, 10, 40, 10);
			textView.setTextSize(20);
			
			Button bttnBackground = (Button) row.findViewById(R.id.buttonBackground);
			bttnBackground.setId(position + 1500);
			bttnBackground.setOnClickListener(this);

			Button bttn1 = (Button) row.findViewById(R.id.button1);
			Button bttn2 = (Button) row.findViewById(R.id.button2);
			
			String hohUuidOnTreatment = settings.getString(Constants.SETTINGS_KEY_HOH_UUID_ON_TREATMENT, "");
			boolean hohOnTreatment = false;
			if (!hohUuidOnTreatment.isEmpty() && hohUuidOnTreatment.equals(sHeadOfHouseholdUuid))
				hohOnTreatment = true;
			
			if (attributes.get(position).equals("hoh"))
			{
				bttn2.setVisibility(View.INVISIBLE);

				bttn1 = (Button) row.findViewById(R.id.button1);
				bttn1.setBackgroundResource(R.drawable.heart_pink_48);
				bttn1.setId(position + 1000);
				bttn1.setOnClickListener(this);
				
			}
			else if (attributes.get(position).equals("guardian"))
			{
				bttn2.setVisibility(View.INVISIBLE);
				
				bttn1.setBackgroundResource(R.drawable.cyan_circle_32);
				bttn1.setScaleX((float) 0.7);
				bttn1.setScaleY((float) 0.7);
				bttn1.setId(position + 1000);
				bttn1.setOnClickListener(this);
			}
			else if (attributes.get(position).equals("child"))
			{
				bttn1.setVisibility(View.INVISIBLE);
				if (hohOnTreatment)
				{
					bttn2.setBackgroundResource(R.drawable.blue_circle_32);
					bttn2.setScaleX((float) 0.7);
					bttn2.setScaleY((float) 0.7);
					bttn2.setId(position + 2000);
					bttn2.setOnClickListener(this);
				}
				else if (bHohCensusCompleted)
				{
					bttn2.setBackgroundResource(R.drawable.green_circle_32);
					bttn2.setScaleX((float) 0.7);
					bttn2.setScaleY((float) 0.7);
				}
				else
					bttn2.setVisibility(View.INVISIBLE);
			}
			
			textView.setText(items[position]);
			
			return row;
		}



		@Override
		public void onClick(View v) {
			// For button 1, Left button
			if ((v.getId() >= 1000) && (v.getId() < (1000 + 200))) // 200 entries
			{
				int position = v.getId() - 1000; // offset
				if (attributes.get(position).equals("hoh"))
				{
		            Intent intent = new Intent(getApplicationContext(), GuardianDetails.class);
		            if (sHeadOfHouseholdFirstName != null)
		         	   intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_FIRST_NAME, sHeadOfHouseholdFirstName);
		            if (sHeadOfHouseholdLastName != null)
		          	   intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_LAST_NAME, sHeadOfHouseholdLastName);
		            if (sHeadOfHouseholdUuid != null)
		           	   intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_UUID, sHeadOfHouseholdUuid);
		            startActivity(intent);

				}
				else if (attributes.get(position).equals("guardian"))
				{
					String uuid = uuids.get(position);
		            Intent intent = new Intent(getApplicationContext(), ChildDetails.class);
		            if (sHeadOfHouseholdFirstName != null)
		            	intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_FIRST_NAME, sHeadOfHouseholdFirstName);
		            if (sHeadOfHouseholdLastName != null)
		            	intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_LAST_NAME, sHeadOfHouseholdLastName);
		            if (sHeadOfHouseholdUuid != null)
		            	intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_UUID, sHeadOfHouseholdUuid);
		            
		            intent.putExtra(Constants.INTENT_EXTRA_GUARDIAN_UUID, uuid);
		            startActivity(intent);

				}
				
			}
			// For button Background
			else if ((v.getId() >= 1500) && (v.getId() < (1500 + 200))) 
			{
				int position = v.getId() - 1500; // offset
				if (attributes.get(position).equals("hoh"))
				{
		            Intent intent = new Intent(getApplicationContext(), HeadOfHouseholdDetails.class);
		            if (sHeadOfHouseholdFirstName != null)
		         	   intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_FIRST_NAME, sHeadOfHouseholdFirstName);
		            if (sHeadOfHouseholdLastName != null)
		          	   intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_LAST_NAME, sHeadOfHouseholdLastName);
		            if (sHeadOfHouseholdUuid != null)
		           	   intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_UUID, sHeadOfHouseholdUuid);
		            startActivity(intent);
				}
				else if (attributes.get(position).equals("guardian"))
				{
		            Intent intent = new Intent(getApplicationContext(), GuardianDetails.class);
		            if (sHeadOfHouseholdFirstName != null)
		         	   intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_FIRST_NAME, sHeadOfHouseholdFirstName);
		            if (sHeadOfHouseholdLastName != null)
		          	   intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_LAST_NAME, sHeadOfHouseholdLastName);
		            if (sHeadOfHouseholdUuid != null)
		           	   intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_UUID, sHeadOfHouseholdUuid);
		            
		            intent.putExtra(Constants.INTENT_EXTRA_GUARDIAN_UUID, uuids.get(position));
		            startActivity(intent);
				}
				else if (attributes.get(position).equals("child"))
				{
		            Intent intent = new Intent(getApplicationContext(), ChildDetails.class);
		            if (sHeadOfHouseholdFirstName != null)
		         	   intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_FIRST_NAME, sHeadOfHouseholdFirstName);
		            if (sHeadOfHouseholdLastName != null)
		          	   intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_LAST_NAME, sHeadOfHouseholdLastName);
		            if (sHeadOfHouseholdUuid != null)
		           	   intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_UUID, sHeadOfHouseholdUuid);
		            
		            intent.putExtra(Constants.INTENT_EXTRA_CHILD_UUID, uuids.get(position));
		            startActivity(intent);
				}

			}
			// For blue buttons, Tx buttons
			else if ((v.getId() >= 2000) && (v.getId() < (2000 + 200))) 
			{
				int position = v.getId() - 2000; // offset
				if (attributes.get(position).equals("child"))
				{
		            Intent intent = new Intent(getApplicationContext(), Treatment.class);
		            if (sHeadOfHouseholdFirstName != null)
		         	   intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_FIRST_NAME, sHeadOfHouseholdFirstName);
		            if (sHeadOfHouseholdLastName != null)
		          	   intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_LAST_NAME, sHeadOfHouseholdLastName);
		            if (sHeadOfHouseholdUuid != null)
		           	   intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_UUID, sHeadOfHouseholdUuid);
		            
		            intent.putExtra(Constants.INTENT_EXTRA_CHILD_UUID, uuids.get(position));
		            intent.putExtra(Constants.INTENT_EXTRA_ACTIVITY_CALLER_ID, Constants.ACTIVITY_CALLER_ID_HOUSEHOLD_REVIEW);
		            
		            startActivity(intent);
				}

			}

			
		}
		
	}
	
	private boolean isHeadOfHouseholdExist(String Uuid)
	{
		for (int i = 0; i < this.headOfHouseholdList.size(); i++)
		{
			HeadOfHouseholdObj tmpHeadOfHousehold = headOfHouseholdList.get(i);
			   if (tmpHeadOfHousehold.uuid.equals(Uuid))
				   return true;
		}
		return false;
	}

	private HeadOfHouseholdObj getHeadOfHousehold(String Uuid)
	{
		for (int i = 0; i < headOfHouseholdList.size(); i++)
		{
			HeadOfHouseholdObj tmpHeadOfHousehold = headOfHouseholdList.get(i);
			   if (tmpHeadOfHousehold.uuid.equals(Uuid))
				   return tmpHeadOfHousehold;
		}
		return null;	
	}


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.household_review);
	    if (getIntent().getExtras() != null) 
	    {
	    	sHeadOfHouseholdFirstName = getIntent().getExtras().getString(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_FIRST_NAME);
	    	sHeadOfHouseholdLastName = getIntent().getExtras().getString(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_LAST_NAME);
	    	sHeadOfHouseholdUuid = getIntent().getExtras().getString(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_UUID);
	    	
	    	sGuardianFirstName = getIntent().getExtras().getString(Constants.INTENT_EXTRA_GUARDIAN_FIRST_NAME);
	    	sGuardianLastName = getIntent().getExtras().getString(Constants.INTENT_EXTRA_GUARDIAN_LAST_NAME);
	    	sGuardianUuid = getIntent().getExtras().getString(Constants.INTENT_EXTRA_GUARDIAN_UUID);
	    	
	    	sChildFirstName = getIntent().getExtras().getString(Constants.INTENT_EXTRA_CHILD_FIRST_NAME);
	    	sChildLastName = getIntent().getExtras().getString(Constants.INTENT_EXTRA_CHILD_LAST_NAME);
	    	sChildUuid = getIntent().getExtras().getString(Constants.INTENT_EXTRA_CHILD_UUID);
	    }

		
		Button bttnHousehold = (Button) findViewById(R.id.buttonHousehold);
		bttnHousehold.setOnClickListener(this);

		
		Button bttnGps = (Button) findViewById(R.id.buttonGps);
		bttnGps.setOnClickListener(this);
		
		Button bttnAddLock = (Button) findViewById(R.id.buttonLock);
		bttnAddLock.setOnClickListener(this);

		boolean bNoChild = false;
		boolean bNoGuardian = false;
		SQLiteDatabase db = openOrCreateDatabase(Constants.DATABASE_NAME, MODE_PRIVATE, null);
		String sql = null;
		
		
		sql = String.format("SELECT * FROM census WHERE phase_id = %d AND uuid_hoh = '%s';",
				getPhaseId(),
				sHeadOfHouseholdUuid);
		
		Cursor cursor = db.rawQuery(sql, null);
		int results = cursor.getCount();
		if (results > 0)
			bHohCensusCompleted = true;

		
		// Try find wives and kids - all
		sql = String.format("SELECT h.first_name AS hoh_first_name, h.last_name AS hoh_last_name, " +
				"g.first_name AS guardian_first_name, g.last_name AS guardian_last_name, " +
				"c.first_name AS child_first_name, c.last_name AS child_last_name, " +
				"c.age AS child_age, c.age_unit AS child_age_unit, c.gender AS child_gender, " +
				"c.uuid_child AS child_uuid, h.uuid_hoh AS hoh_uuid, g.uuid_guardian AS guardian_uuid " +
				"from child c, head_of_household h, guardian g " +
				"where c.uuid_hoh = h.uuid_hoh " +
				"AND g.uuid_hoh = h.uuid_hoh " +
				"AND c.uuid_guardian = g.uuid_guardian " +
				"AND c.uuid_hoh = '%s';", 
				sHeadOfHouseholdUuid);

		
		Cursor c = db.rawQuery(sql, null);
		results = c.getCount();
		
		// Nothing, try find wives only 
		if (results == 0)
		{
			bNoChild = true;
			sql = String.format("SELECT h.first_name AS hoh_first_name, h.last_name AS hoh_last_name, " +
					"g.first_name AS guardian_first_name, g.last_name AS guardian_last_name, " +
					"h.uuid_hoh AS hoh_uuid, g.uuid_guardian AS guardian_uuid " +
					"FROM head_of_household h, guardian g " +
					"WHERE g.uuid_hoh = h.uuid_hoh AND h.uuid_hoh = '%s';", 
					sHeadOfHouseholdUuid);
			
			c = db.rawQuery(sql, null);
			results = c.getCount();
		}
		
		// Nothing, try find kids only
		if (results == 0)
		{
			bNoGuardian = true;
			bNoChild = false;
			sql = String.format("SELECT h.first_name AS hoh_first_name, h.last_name AS hoh_last_name, " +
					"c.first_name AS child_first_name, c.last_name AS child_last_name, c.age AS child_age, " +
					"c.age_unit AS child_age_unit, c.gender AS child_gender, " +
					"c.uuid_child AS child_uuid, h.uuid_hoh AS hoh_uuid " +
					"FROM child c, head_of_household h " +
					"WHERE c.uuid_hoh = h.uuid_hoh " +
					"AND c.uuid_hoh = '%s';", 
					sHeadOfHouseholdUuid);
			c = db.rawQuery(sql, null);
			results = c.getCount();
		}
		
		headOfHouseholdList = new ArrayList<HeadOfHouseholdObj>();
		
		if (results > 0)
		{
			int n = 0;
			if (c != null ) 
			{
			    if  (c.moveToFirst()) 
			    {
	
			        do {	
						
			        	String hoh_first_name = c.getString(c.getColumnIndex("hoh_first_name"));
						String hoh_last_name = c.getString(c.getColumnIndex("hoh_last_name"));
						String hoh_uuid = c.getString(c.getColumnIndex("hoh_uuid"));
						HeadOfHouseholdObj headOfHouseholdObj = getHeadOfHousehold(hoh_uuid);
						if (headOfHouseholdObj == null)
						{
							headOfHouseholdObj = new HeadOfHouseholdObj(hoh_first_name, hoh_last_name, hoh_uuid);
							headOfHouseholdList.add(headOfHouseholdObj);
						}
						

						GuardianObj guardianObj = null;
						if (!bNoGuardian) // There is guardian
						{
							String guardian_first_name = c.getString(c.getColumnIndex("guardian_first_name"));
							String guardian_last_name = c.getString(c.getColumnIndex("guardian_last_name"));
							String guardian_uuid = c.getString(c.getColumnIndex("guardian_uuid"));
							guardianObj = headOfHouseholdObj.getGuardian(guardian_uuid);
							if (guardianObj == null)
							{
								guardianObj = new GuardianObj(guardian_first_name, guardian_last_name, guardian_uuid);
								headOfHouseholdObj.addGuardian(guardianObj);
							}
						}
						
						if (!bNoChild) // There is child
						{
							String child_first_name = c.getString(c.getColumnIndex("child_first_name"));
							String child_last_name = c.getString(c.getColumnIndex("child_last_name"));
							String child_age = c.getString(c.getColumnIndex("child_age"));
							String child_age_unit = c.getString(c.getColumnIndex("child_age_unit"));
							String child_gender = c.getString(c.getColumnIndex("child_gender"));
							String child_uuid = c.getString(c.getColumnIndex("child_uuid"));
							ChildObj childObj = null;
							if (!bNoGuardian)
								childObj = guardianObj.getChild(child_uuid);
							else
								childObj = headOfHouseholdObj.getChild(child_uuid);
							if (childObj == null)
							{
								childObj = new ChildObj(child_first_name, child_last_name, child_uuid, child_age, child_age_unit, child_gender);
								
								// If guardian present, add to mother.
								// If guardian absent, add to father.
								if (!bNoGuardian)
									guardianObj.addChild(childObj);
								else
									headOfHouseholdObj.addChild(childObj);
							}
						}
						
			            n++;
	
			        }while (c.moveToNext());
			    } 
			}
		}
		else
		{
        	String hoh_first_name = sHeadOfHouseholdFirstName;
			String hoh_last_name = sHeadOfHouseholdLastName;
			String hoh_uuid = sHeadOfHouseholdUuid;
			HeadOfHouseholdObj headOfHouseholdObj = new HeadOfHouseholdObj(hoh_first_name, hoh_last_name, hoh_uuid);
			headOfHouseholdList.add(headOfHouseholdObj);
		}

		c.close();
		db.close();
		

		ArrayList<String> valueList = new ArrayList<String>();
		attributes = new ArrayList<String>();
		uuids = new ArrayList<String>();
		
		if (headOfHouseholdList != null)
		{
			for (int n = 0; n < headOfHouseholdList.size(); n++)
			{
				HeadOfHouseholdObj hohObj = headOfHouseholdList.get(n);
				String tmpStr = hohObj.first_name + " " + hohObj.last_name;
				valueList.add(tmpStr);
				attributes.add("hoh");
				uuids.add(hohObj.uuid);
				

				if (hohObj.guardianList != null)
				{
					for (int i = 0; i < hohObj.guardianList.size(); i++)
					{
						GuardianObj gObj = hohObj.guardianList.get(i);
						tmpStr = "     " + gObj.first_name + " " + gObj.last_name;
						valueList.add(tmpStr);
						attributes.add("guardian");
						uuids.add(gObj.uuid);
						
						if (gObj.childList == null)
							continue;
						
						for (int j = 0; j < gObj.childList.size(); j++)
						{
							ChildObj cObj = gObj.childList.get(j);
							tmpStr =  "          " + cObj.first_name + " " + cObj.age + " " + cObj.age_unit + " " + cObj.gender;
							valueList.add(tmpStr);
							attributes.add("child");
							uuids.add(cObj.uuid);
						}
					}
				}
				
				if (hohObj.childList != null)
				{
					for (int i = 0; i < hohObj.childList.size(); i++)
					{
						ChildObj cObj = hohObj.childList.get(i);
						tmpStr = "     " + cObj.first_name + " " + cObj.last_name;
						valueList.add(tmpStr);
						attributes.add("child");
						uuids.add(cObj.uuid);
						
					}
				}
			}
		}
		
		String[] values = new String[valueList.size()];
		for (int n = 0; n < values.length; n++)
		{
			values[n] = valueList.get(n);
		}

	
		ListView listView = (ListView) findViewById(R.id.listViewHouseholdReview);
		
		
		// Define a new Adapter
		// First parameter - Context
		// Second parameter - Layout for the row
		// Third parameter - ID of the TextView to which the data is written
		// Forth - the Array of data
		ListItem1Adapter adapter = new ListItem1Adapter(this,
		  android.R.layout.simple_list_item_1, android.R.id.text1, values);

		
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
			{
				Toast.makeText(getApplicationContext(),
					      "Click ListItem Number " + position, Toast.LENGTH_LONG)
					      .show();

				
			}
		});
		


	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.buttonHousehold)
		{
			startActivity(new Intent(this, HeadOfHousehold.class));
		}
		else if (v.getId() == R.id.buttonGps)
		{
			saveGPSLocation();
		}
		else if (v.getId() == R.id.buttonLock)
		{
			onLockClicked();
		}
	}
	
	private void onLockClicked()
	{
	
		ShowDialog("Treatment", "Do you want to treat now?", new DialogResponses() {

			@Override
			public void onNegative() {
				
				if (showGreenButtons()) // only refresh view if lock did cause a new insert
				{
		            Intent intent = new Intent(HouseholdReview.this, HouseholdReview.class);
		            intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_FIRST_NAME, sHeadOfHouseholdFirstName);
		            intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_LAST_NAME, sHeadOfHouseholdLastName);
		            intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_UUID, sHeadOfHouseholdUuid);
		            
		            intent.putExtra(Constants.INTENT_EXTRA_GUARDIAN_FIRST_NAME, sGuardianFirstName);
		            intent.putExtra(Constants.INTENT_EXTRA_GUARDIAN_LAST_NAME, sGuardianLastName);
		            intent.putExtra(Constants.INTENT_EXTRA_GUARDIAN_UUID, sGuardianUuid);
		            
		            intent.putExtra(Constants.INTENT_EXTRA_CHILD_FIRST_NAME, sChildFirstName);
		            intent.putExtra(Constants.INTENT_EXTRA_CHILD_LAST_NAME, sChildLastName);
		            intent.putExtra(Constants.INTENT_EXTRA_CHILD_UUID, sChildUuid);
		            startActivity(intent);
				}

				
			}

			@Override
			public void onPositive() {

				if (showBlueButtons()) // only refresh view if lock did cause a new insert
				{
		            Intent intent = new Intent(HouseholdReview.this, HouseholdReview.class);
		            intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_FIRST_NAME, sHeadOfHouseholdFirstName);
		            intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_LAST_NAME, sHeadOfHouseholdLastName);
		            intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_UUID, sHeadOfHouseholdUuid);
		            
		            intent.putExtra(Constants.INTENT_EXTRA_GUARDIAN_FIRST_NAME, sGuardianFirstName);
		            intent.putExtra(Constants.INTENT_EXTRA_GUARDIAN_LAST_NAME, sGuardianLastName);
		            intent.putExtra(Constants.INTENT_EXTRA_GUARDIAN_UUID, sGuardianUuid);
		            
		            intent.putExtra(Constants.INTENT_EXTRA_CHILD_FIRST_NAME, sChildFirstName);
		            intent.putExtra(Constants.INTENT_EXTRA_CHILD_LAST_NAME, sChildLastName);
		            intent.putExtra(Constants.INTENT_EXTRA_CHILD_UUID, sChildUuid);
		            startActivity(intent);

				}

				
			}});
		
	}
	
	private boolean showGreenButtons()
	{
		boolean succeed = false;
		
		int phase_id = getPhaseId();
		String sUuidAgent = settings.getString(Constants.SETTINGS_KEY_UUID_AGENT, "");
		
		Date date = new Date();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sDateTime = formatter.format(date);

		
		SQLiteDatabase db = openOrCreateDatabase(Constants.DATABASE_NAME, MODE_PRIVATE, null);
		String sql = null;
		
		
		sql = String.format("SELECT * FROM census WHERE phase_id = %d AND uuid_hoh = '%s';",
				phase_id,
				sHeadOfHouseholdUuid);
		
		Cursor cursor = db.rawQuery(sql, null);
		int results = cursor.getCount();
		
		if (results == 0)
		{
			String sCensusUuid = generateUUID();
			
			sql = String.format("INSERT INTO census " +
					" (uuid_census, uuid_hoh, phase_id, date_created, date_last_modified, uuid_agent_created, uuid_agent_modified) " +
					" VALUES " +
					"('%s', '%s', %d, '%s', '%s', '%s', '%s');", 
					sCensusUuid,
					sHeadOfHouseholdUuid,
					phase_id,
					sDateTime,
					sDateTime,
					sUuidAgent,
					sUuidAgent);
			dbExecSQL(db, sql);
			
			succeed = true;

		}
		
		db.close();
		
		//return succeed;
		
		settingsPutKeyValue(Constants.SETTINGS_KEY_HOH_UUID_ON_TREATMENT, "");
		return true;

	}
	
	private boolean showBlueButtons()
	{
		boolean succeed = true;
		settingsPutKeyValue(Constants.SETTINGS_KEY_HOH_UUID_ON_TREATMENT, sHeadOfHouseholdUuid);
		return succeed;
	}
	
	interface DialogResponses {
		void onPositive();
		void onNegative();
	}
	
	void ShowDialog(String title, String message, final DialogResponses responses)
	{

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle(title);
		builder.setMessage(message);

		builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface dialog, int which) {
		        dialog.dismiss();
		        responses.onPositive();
		    }

		});

		builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        dialog.dismiss();
		        responses.onNegative();
		    }
		});

		AlertDialog alert = builder.create();
		alert.show();
	}
	
}

