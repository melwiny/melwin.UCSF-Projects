package edu.ucsf.telemedicine.mhealth.mordor;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;


public class GuardianDetails extends RootActivity implements CompoundButton.OnCheckedChangeListener, OnClickListener {
	
	private String sHeadOfHouseholdFirstName = null;
	private String sHeadOfHouseholdLastName = null;
	private String sHeadOfHouseholdUuid = null;
	private String sGuardianFirstName = null;
	private String sGuardianLastName = null;
	private String sGuardianUuid = null;
	EditText editTextFirstName, editTextLastName, editTextAge, editTextNote, editTextPregnantMonthCount;
	CheckBox cbHasHeadOfHousehold;
	Switch switchMaleFemale, switchPregnant;
	String sGender = "Female";
	int nPregnantMonths = 0;
	TextView textViewPregnantMonthCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.guardian_details);
		
	    if (getIntent().getExtras() != null) {
	    	sHeadOfHouseholdFirstName = getIntent().getExtras().getString(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_FIRST_NAME);
	    	sHeadOfHouseholdLastName = getIntent().getExtras().getString(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_LAST_NAME);
	    	sHeadOfHouseholdUuid = getIntent().getExtras().getString(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_UUID);
	    	sGuardianFirstName = getIntent().getExtras().getString(Constants.INTENT_EXTRA_GUARDIAN_FIRST_NAME);
	    	sGuardianLastName = getIntent().getExtras().getString(Constants.INTENT_EXTRA_GUARDIAN_LAST_NAME);
	    	sGuardianUuid = getIntent().getExtras().getString(Constants.INTENT_EXTRA_GUARDIAN_UUID);
	    }
	    
		TextView textViewLocation = (TextView) findViewById(R.id.textViewLocation);
		textViewLocation.setText(settings.getString("village", ""));
	    
	    cbHasHeadOfHousehold = (CheckBox) findViewById(R.id.checkBoxHasHeadOfHousehold);
	    if (!sHeadOfHouseholdFirstName.isEmpty() || !sHeadOfHouseholdLastName.isEmpty())
	    	cbHasHeadOfHousehold.setChecked(true);
	    else
	    	cbHasHeadOfHousehold.setChecked(false);
	    
	    editTextPregnantMonthCount = (EditText) findViewById(R.id.EditTextPregnantMonthCount);
		textViewPregnantMonthCount = (TextView) findViewById(R.id.TextViewPregnantMonthCount);
	    
        switchMaleFemale = (Switch) findViewById(R.id.switch_male_female);
        if (switchMaleFemale != null)
        {
        	switchMaleFemale.setOnCheckedChangeListener(this);
        	setPregnantSwitchVisible(!switchMaleFemale.isChecked());
        }
        
        switchPregnant = (Switch) findViewById(R.id.switch_pregnant_yes_no);
        if (switchPregnant != null)
        {
        	switchPregnant.setOnCheckedChangeListener(this);
        	setPregnantMonthCountVisible(switchPregnant.isChecked());
        		
        }
        
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
        
        editTextLastName.setText(sHeadOfHouseholdLastName);
        
        if (sGuardianUuid != null)
        	initFields();
        
        cbHasHeadOfHousehold.setVisibility(View.INVISIBLE);


        
    }
 
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    	if (buttonView.getId() == R.id.switch_male_female)
    	{
    	       if (isChecked)
    	       {
    	    	   // Male
    	    	   setPregnantSwitchVisible(false);
    	    	   setPregnantMonthCountVisible(false);
    	    	   sGender = "Male";
    	       }
    	       else
    	       {
    	    	   setPregnantSwitchVisible(true);
    	    	   sGender = "Female";
    	       }
    	}
    	else if (buttonView.getId() == R.id.switch_pregnant_yes_no)
    	{
    	       setPregnantMonthCountVisible(isChecked);
    	       if (!isChecked)
    	       {
    	    	   nPregnantMonths = 0; 
    	       }

    	}
    }

    private void setPregnantMonthCountVisible(boolean setVisible)
    {
		editTextPregnantMonthCount.setText(null);
		
        if (setVisible) 
        {
        	editTextPregnantMonthCount.setVisibility(View.VISIBLE);
        	textViewPregnantMonthCount.setVisibility(View.VISIBLE);
        }
        else
        {
        	editTextPregnantMonthCount.setVisibility(View.INVISIBLE);
        	textViewPregnantMonthCount.setVisibility(View.INVISIBLE);       		
        }
   	
    }
    
    private void setPregnantSwitchVisible(boolean setVisible)
    {
		Switch switchPregnant = (Switch) findViewById(R.id.switch_pregnant_yes_no);
		switchPregnant.setChecked(false);

        if (setVisible) 
        {
        	switchPregnant.setVisibility(View.VISIBLE);
        }
        else
        {
        	switchPregnant.setVisibility(View.INVISIBLE);
        }
   	
    }
    
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.buttonSave)
		{
			if (onSaveClicked() == true)
			{
	            Intent intent = new Intent(this, ChildDetails.class);
	            intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_FIRST_NAME, sHeadOfHouseholdFirstName);
	            intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_LAST_NAME, sHeadOfHouseholdLastName);
	            intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_UUID, sHeadOfHouseholdUuid);
	            
	            intent.putExtra(Constants.INTENT_EXTRA_GUARDIAN_FIRST_NAME, sGuardianFirstName);
	            intent.putExtra(Constants.INTENT_EXTRA_GUARDIAN_LAST_NAME, sGuardianLastName);
	            intent.putExtra(Constants.INTENT_EXTRA_GUARDIAN_UUID, sGuardianUuid);
	            startActivity(intent);
			}
			else
			{
	            Intent intent = new Intent(this, HouseholdReview.class);
	            intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_FIRST_NAME, sHeadOfHouseholdFirstName);
	            intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_LAST_NAME, sHeadOfHouseholdLastName);
	            intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_UUID, sHeadOfHouseholdUuid);	            
	            startActivity(intent);
			}

		}
		else if (v.getId() == R.id.buttonHousehold)
		{
			Intent intent = new Intent(GuardianDetails.this, HeadOfHousehold.class);
			startActivity(intent);
		}
		else if (v.getId() == R.id.buttonAddCoordinates)
		{
			saveGPSLocation();
		}
		else if (v.getId() == R.id.buttonSettings)
		{
			startActivity(new Intent(GuardianDetails.this, UserSettings.class));
		}

	}
	
	private boolean onSaveClicked()
	{
		String sFirstName = editTextFirstName.getText().toString();
		String sLastName = editTextLastName.getText().toString();
		String sPregnantMonthCount = editTextPregnantMonthCount.getText().toString();
		String sAge = editTextAge.getText().toString();
		String sUuidAgent = settings.getString(Constants.SETTINGS_KEY_UUID_AGENT, "");
		int age = 0;
		
		if (sFirstName.isEmpty() || sLastName.isEmpty())
		{
			showToast("Name is missing. No record is saved.");
			return false;
		}
		
		if (sHeadOfHouseholdUuid == null || sHeadOfHouseholdUuid.isEmpty())
		{
			showToast("Head of household identity is not given");
			return false;
		}

		
		if (sPregnantMonthCount.isEmpty() || sPregnantMonthCount.equals(""))
			nPregnantMonths = 0;
		else
			nPregnantMonths = Integer.parseInt(sPregnantMonthCount);
		
		if (sAge.isEmpty() || sAge.equals(""))
			age = 0;
		else
			age = Integer.parseInt(sAge);
				
		Date date = new Date();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sDateTime = formatter.format(date);

		
		SQLiteDatabase db = openOrCreateDatabase(Constants.DATABASE_NAME, MODE_PRIVATE, null);
		String sql = null;
		
		
		sql = String.format("SELECT * FROM guardian WHERE first_name = '%s' AND last_name = '%s' AND uuid_hoh = '%s';",
				sFirstName,
				sLastName,
				sHeadOfHouseholdUuid);
		
		Cursor cursor = db.rawQuery(sql, null);
		int results = cursor.getCount();
		//cursor.moveToFirst();
		//Log.d("Mordor", cursor.getString(cursor.getColumnIndex("first_name"))));

		if (results == 0)
		{
			sGuardianUuid = generateUUID();
			sGuardianFirstName = sFirstName;
			sGuardianLastName = sLastName;

			sql = String.format("INSERT INTO guardian " +
					" (uuid_guardian, first_name, last_name, age, gender, pregnant, note, uuid_hoh, date_created, date_last_modified, uuid_agent_created, uuid_agent_modified) " +
					" VALUES " +
					"('%s', '%s', '%s', %d, '%s', %d, '%s', '%s', '%s', '%s', '%s', '%s');", 
					sGuardianUuid,
					sFirstName, 
					sLastName, 
					age, 
					sGender, 
					nPregnantMonths,
					editTextNote.getText().toString(),
					sHeadOfHouseholdUuid,
					sDateTime,
					sDateTime,
					sUuidAgent,
					sUuidAgent);
			dbExecSQL(db, sql);
			
			showToast("Added new Guardian: " + sFirstName + " " + sLastName);
		}
		else
		{
			cursor.moveToFirst();
			String subjectUuid = cursor.getString(cursor.getColumnIndex("uuid_guardian"));

			sql = String.format("UPDATE guardian " +
					"SET age = %d, " +
					"gender = '%s', " +
					"pregnant = %d, " +
					"note = '%s', " +
					"date_last_modified = '%s', " +
					"uuid_agent_modified = '%s' " +
					"WHERE uuid_guardian = '%s' ;",
					age, 
					sGender, 
					nPregnantMonths,
					editTextNote.getText().toString(),
					sDateTime,
					sUuidAgent,
					subjectUuid);
			
			dbExecSQL(db, sql);
			
			showToast("Updated Guardian: " + sFirstName + " " + sLastName);
		}
		db.close();
		
		return true;

	}
	
	private void initFields()
	{
		String firstName = null, lastName = null, age = null, gender = null, pregnant = null, note = null;
		String dateCreated = null, dateLastModified = null;
		String uuidAgentCreated = null, uuidAgentModified = null;
		
		
		SQLiteDatabase db = openOrCreateDatabase(Constants.DATABASE_NAME, MODE_PRIVATE, null);
		String sql = null;
		
		sql = String.format("SELECT * from guardian WHERE uuid_guardian = '%s';", sGuardianUuid);
		
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
		            pregnant = c.getString(c.getColumnIndex("pregnant"));
		            note = c.getString(c.getColumnIndex("note"));
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
		
		if (gender.equals("Female"))
		{
			switchMaleFemale.setChecked(false);
			setPregnantSwitchVisible(true);
		}
		else
		{
			switchMaleFemale.setChecked(true);
			setPregnantSwitchVisible(false);
		}
		
		int month = Integer.parseInt(pregnant);
		
		if (month > 0)
		{
			switchPregnant.setChecked(true);
			setPregnantMonthCountVisible(true);
			editTextPregnantMonthCount.setText(pregnant);
		}
		else
		{
			switchPregnant.setChecked(false);
			setPregnantMonthCountVisible(false);
		}

	}


		

}
