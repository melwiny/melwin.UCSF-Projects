package edu.ucsf.telemedicine.mhealth.mordor;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;

public class Treatment extends RootActivity implements OnClickListener, TextWatcher, OnFocusChangeListener {
	
	private String sHeadOfHouseholdFirstName = null;
	private String sHeadOfHouseholdLastName = null;
	private String sHeadOfHouseholdUuid = null;
	
	private String sGuardianFirstName = null;
	private String sGuardianLastName = null;
	private String sGuardianUuid = null;
	
	private String sChildFirstName = null;
	private String sChildLastName = null;
	private String sChildUuid = null;
	
	private String sLocation = null;
	private String sTreatmentUuid = null;
	
	private String sActivityCallerId = null;
	
	EditText editTextDose, editTextNote;
	TextView textViewHeadOfHousehold, textViewGuardian, textViewChild, textViewLocation;
	CheckBox cbHasObtainedConsent;

	
	AutoCompleteTextView autoCompleteTextViewStatus;
	String status[]={
	  "Administered", 
	  "Unable", 
	  "Guardian refused", 
	  "To Mop Up"
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.treatment);
		
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
	    	
	    	sLocation = getIntent().getExtras().getString(Constants.INTENT_EXTRA_LOCATION);
	    	sTreatmentUuid = getIntent().getExtras().getString(Constants.INTENT_EXTRA_TREATMENT_UUID);
	    	
	    	sActivityCallerId = getIntent().getExtras().getString(Constants.INTENT_EXTRA_ACTIVITY_CALLER_ID);
	    }
	    
		autoCompleteTextViewStatus = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextViewStatus);
		autoCompleteTextViewStatus.addTextChangedListener(this);
		autoCompleteTextViewStatus.setOnFocusChangeListener(this);
		autoCompleteTextViewStatus.setOnClickListener(this);
		autoCompleteTextViewStatus.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, status));

	    cbHasObtainedConsent = (CheckBox) findViewById(R.id.checkBoxHasObtainedConsent);
		
        Button bttnSave = (Button) findViewById(R.id.buttonSave);
        bttnSave.setOnClickListener(this);
                
        Button bttnHousehold = (Button) findViewById(R.id.buttonHousehold);
        bttnHousehold.setOnClickListener(this);

		Button bttnAddCoordinates = (Button) findViewById(R.id.buttonAddCoordinates);
		bttnAddCoordinates.setOnClickListener(this);

        Button bttnSettings = (Button) findViewById(R.id.buttonSettings);
        bttnSettings.setOnClickListener(this);
        
        editTextNote = (EditText) findViewById(R.id.editTextNote);
        editTextDose = (EditText) findViewById(R.id.editTextDose);
        
        textViewHeadOfHousehold = (TextView) findViewById(R.id.textViewHeadOfHousehold);
        textViewGuardian = (TextView) findViewById(R.id.textViewGuardian);
        textViewChild = (TextView) findViewById(R.id.textViewChild);
        
        textViewLocation = (TextView) findViewById(R.id.textViewLocation);
        
        if (sChildUuid != null)
        	initFieldsWithChildUuid();
        
        textViewLocation.setText(sLocation);
        textViewHeadOfHousehold.setText(sHeadOfHouseholdFirstName + " " + sHeadOfHouseholdLastName);
        textViewGuardian.setText(sGuardianFirstName + " " + sGuardianLastName);
        textViewChild.setText(sChildFirstName + " " + sChildLastName);
        
        if (sTreatmentUuid != null)
        	initFields();



	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.autoCompleteTextViewStatus)
			autoCompleteTextViewStatus.showDropDown();
		else if (v.getId() == R.id.buttonSave)
		{
			if (onSaveClicked() ==  true)
			{
				Intent intent = null;
				
				if (sActivityCallerId != null && !sActivityCallerId.isEmpty() && sActivityCallerId.equals(Constants.ACTIVITY_CALLER_ID_HOUSEHOLD_REVIEW))
				{
					intent = new Intent(this, HouseholdReview.class);
					intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_FIRST_NAME, sHeadOfHouseholdFirstName);
					intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_LAST_NAME, sHeadOfHouseholdLastName);
					intent.putExtra(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_UUID, sHeadOfHouseholdUuid);
					startActivity(intent);
				}
				else
				{
				
					intent = new Intent(this, ChildDetails.class);
	
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
		}
		else if (v.getId() == R.id.buttonHousehold)
		{
			startActivity(new Intent(Treatment.this, HeadOfHousehold.class));
		}
		else if (v.getId() == R.id.buttonAddCoordinates)
		{
			saveGPSLocation();
		}
		else if (v.getId() == R.id.buttonSettings)
		{
			startActivity(new Intent(Treatment.this, UserSettings.class));
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
	
	@Override
	public void onFocusChange(View v, boolean isFocused) 
	{
		InputMethodManager inputMethodManager = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);

		if ((v.getId() == R.id.autoCompleteTextViewStatus) && (isFocused == true))
		{
			autoCompleteTextViewStatus.setText("");
			autoCompleteTextViewStatus.showDropDown();
			autoCompleteTextViewStatus.setInputType(InputType.TYPE_NULL);
			inputMethodManager.hideSoftInputFromWindow(autoCompleteTextViewStatus.getWindowToken(),0);
		}
	}
	
	private boolean onSaveClicked()
	{
		int hasObtainedConsent = 0;
		if (cbHasObtainedConsent.isChecked())
			hasObtainedConsent = 1;
		
		String sStatus = autoCompleteTextViewStatus.getText().toString();
		String sDose = editTextDose.getText().toString();
		int dose = 0;
		if (!sDose.isEmpty())
			dose = Integer.parseInt(sDose);
		String sNote = editTextNote.getText().toString();
		String sUuidAgent = settings.getString(Constants.SETTINGS_KEY_UUID_AGENT, "");

		
		if ((sChildUuid == null || sChildUuid.isEmpty()))
		{
			showToast("Child's ID is absent!");
			return false;
		}
		else if (sStatus.isEmpty())
		{
			showToast("Please enter status");
			return false;			
		}
		else if (hasObtainedConsent == 0)
		{
			showToast("Please obtain consent");
			return false;			
		}

						
		Date date = new Date();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sDateTime = formatter.format(date);

		
		SQLiteDatabase db = openOrCreateDatabase(Constants.DATABASE_NAME, MODE_PRIVATE, null);
		String sql = null;
		
		boolean newTreatment = false;
		
		if (sTreatmentUuid == null)
			newTreatment = true;
		
		if (!newTreatment)
		{
			sql = String.format("SELECT * FROM treatment WHERE uuid_treatment = '%s';",
					sTreatmentUuid);
			
			Cursor cursor = db.rawQuery(sql, null);
			int results = cursor.getCount();
			if (results == 0)
				newTreatment = true;
			else
				newTreatment = false;
		}

		if (newTreatment)
		{
			sql = String.format("INSERT INTO treatment " +
					" (uuid_treatment, obtained_consent, status, dose, note, uuid_child, date_created, date_last_modified, uuid_agent_created, uuid_agent_modified) " +
					" VALUES " +
					"('%s', %d, '%s', %d, '%s', '%s', '%s', '%s', '%s', '%s');", 
					generateUUID(),
					hasObtainedConsent, 
					sStatus,
					dose,
					sNote,
					sChildUuid,
					sDateTime,
					sDateTime,
					sUuidAgent,
					sUuidAgent);
			dbExecSQL(db, sql);
			
			showToast("Added new Treatment: " + sDateTime);
		}
		else
		{
			sql = String.format("UPDATE treatment " +
					"SET obtained_consent = %d, " +
					"status = '%s', " +
					"dose = %d, " +
					"note = '%s', " +
					"date_last_modified = '%s', " +
					"uuid_agent_modified = '%s' " +
					"WHERE uuid_treatment = '%s';",
					hasObtainedConsent,
					sStatus, 
					dose,
					sNote, 
					sDateTime,
					sUuidAgent,
					sTreatmentUuid);
			
			
			dbExecSQL(db, sql);
			
			showToast("Updated Treatment: " + sDateTime);
		}
		db.close();
		return true;

	}
	
	private void initFields()
	{
		String status = null, note = null, uuidChild = null;
		String dateCreated = null, dateLastModified = null;
		String uuidAgentCreated = null, uuidAgentModified = null;
		int obtainedConsent = 0, dose = 0;

		SQLiteDatabase db = openOrCreateDatabase(Constants.DATABASE_NAME, MODE_PRIVATE, null);
		String sql = null;
		sql = String.format("SELECT * from treatment WHERE uuid_treatment = '%s';", sTreatmentUuid);

		
		
		Cursor c = db.rawQuery(sql, null);
		int results = c.getCount();
		if (results == 0)
			return;
		
		
		if (c != null ) 
		{
		    if  (c.moveToFirst()) 
		    {

		        do {
		        	obtainedConsent = c.getInt(c.getColumnIndex("obtained_consent"));
		            status = c.getString(c.getColumnIndex("status"));
		            dose = c.getInt(c.getColumnIndex("dose"));
		            note = c.getString(c.getColumnIndex("note"));
		            uuidChild = c.getString(c.getColumnIndex("uuid_child"));
		            uuidAgentCreated = c.getString(c.getColumnIndex("uuid_agent_created"));
		            uuidAgentModified = c.getString(c.getColumnIndex("uuid_agent_modified"));
		            dateCreated = c.getString(c.getColumnIndex("date_created"));
		            dateLastModified = c.getString(c.getColumnIndex("date_last_modified"));
		            
		            break;
		            

		        }while (c.moveToNext());
		    } 
		}
		
		
		
		c.close();
		db.close();
		
		
		autoCompleteTextViewStatus.setText(status);
		editTextDose.setText(Integer.toString(dose));
		editTextNote.setText(note);
		
		if (obtainedConsent == 1)
			cbHasObtainedConsent.setChecked(true);
		else
			cbHasObtainedConsent.setChecked(false);
		
	}
	
	private void initFieldsWithChildUuid()
	{
		String child_first_name = null, child_last_name = null, child_age = null, child_age_unit = null, child_gender = null;
		String guardian_first_name = null, guardian_last_name = null, hoh_first_name = null, hoh_last_name = null;
		String hoh_uuid = null, guardian_uuid = null;
		
		
		SQLiteDatabase db = openOrCreateDatabase(Constants.DATABASE_NAME, MODE_PRIVATE, null);
		String sql = null;
		
		sql = String.format("SELECT h.first_name AS hoh_first_name, h.last_name AS hoh_last_name, " +
				"g.first_name AS guardian_first_name, g.last_name AS guardian_last_name, " +
				"c.first_name AS child_first_name, c.last_name AS child_last_name, c.age AS child_age, " +
				"c.age_unit AS child_age_unit, c.gender AS child_gender, c.uuid_child AS child_uuid, " +
				"h.uuid_hoh AS hoh_uuid, g.uuid_guardian AS guardian_uuid " +
				"FROM child c, head_of_household h, guardian g " +
				"WHERE c.uuid_hoh = h.uuid_hoh " +
				"AND c.uuid_guardian = g.uuid_guardian " +
				"AND c.uuid_child = '%s';", sChildUuid);
		
		Cursor c = db.rawQuery(sql, null);
		int results = c.getCount();
		if (results == 0)
			return;
		
		
		if (c != null ) 
		{
		    if  (c.moveToFirst()) 
		    {

		        do {
		        	hoh_first_name = c.getString(c.getColumnIndex("hoh_first_name"));
		            hoh_last_name = c.getString(c.getColumnIndex("hoh_last_name"));
		        	guardian_first_name = c.getString(c.getColumnIndex("guardian_first_name"));
		            guardian_last_name = c.getString(c.getColumnIndex("guardian_last_name"));
		        	child_first_name = c.getString(c.getColumnIndex("child_first_name"));
		            child_last_name = c.getString(c.getColumnIndex("child_last_name"));
		            child_age = c.getString(c.getColumnIndex("child_age"));
		            child_age_unit = c.getString(c.getColumnIndex("child_age_unit"));
		            child_gender = c.getString(c.getColumnIndex("child_gender"));
		            hoh_uuid = c.getString(c.getColumnIndex("hoh_uuid"));
		            guardian_uuid = c.getString(c.getColumnIndex("guardian_uuid"));
		            
		            
		            break;
		            

		        }while (c.moveToNext());
		    } 
		}
		c.close();
		db.close();
		
		if (sHeadOfHouseholdFirstName == null || sHeadOfHouseholdFirstName.isEmpty())
			sHeadOfHouseholdFirstName = hoh_first_name;
		if (sHeadOfHouseholdLastName == null || sHeadOfHouseholdLastName.isEmpty())
			sHeadOfHouseholdLastName = hoh_last_name;
		if (sHeadOfHouseholdUuid == null || sHeadOfHouseholdUuid.isEmpty())
			sHeadOfHouseholdUuid = hoh_uuid;

		if (sGuardianFirstName == null || sGuardianFirstName.isEmpty())
			sGuardianFirstName = guardian_first_name;
		if (sGuardianLastName == null || sGuardianLastName.isEmpty())
			sGuardianLastName = guardian_last_name;
		if (sGuardianUuid == null || sGuardianUuid.isEmpty())
			sGuardianUuid = guardian_uuid;

		if (sChildFirstName == null || sChildFirstName.isEmpty())
			sChildFirstName = child_first_name;
		if (sChildLastName == null || sChildLastName.isEmpty())
			sChildLastName = child_last_name;
		if (sLocation == null || sLocation.isEmpty())
			sLocation = settings.getString(Constants.SETTINGS_KEY_VILLAGE, "");


	}




}
