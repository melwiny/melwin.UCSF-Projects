package edu.ucsf.telemedicine.mhealth.mordor;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;


public class ChildDetails extends RootActivity implements OnItemClickListener, CompoundButton.OnCheckedChangeListener, OnClickListener, TextWatcher, OnFocusChangeListener {
	
	private String sHeadOfHouseholdFirstName = null;
	private String sHeadOfHouseholdLastName = null;
	private String sHeadOfHouseholdUuid = null;
	
	private String sGuardianFirstName = null;
	private String sGuardianLastName = null;
	private String sGuardianUuid = null;
	
	private String sChildFirstName = null;
	private String sChildLastName = null;
	private String sChildUuid = null;

	EditText editTextFirstName, editTextLastName, editTextNickname, editTextNote, editTextAge;
	TextView textViewLocation;
	Switch switchMaleFemale;
	String sGender = "Female";
	AutoCompleteTextView autoCompleteTextViewStatus, autoCompleteTextViewTreatment;
	private RadioGroup radioGroupAgeUnit;
	private RadioButton radioButtonAgeUnit;
	String sTreatmentUuids[] = null;
	String sTreatments[] = null;
	int indexAutoCompleteTextViewTreatment = 0;
	
	String item[]={
	  "Alive", 
	  "Dead", 
	  "Missing", 
	  "Moved",
	  "Unknown"
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.child_details);
		
		
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
	    
		autoCompleteTextViewStatus = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextViewStatus);
		autoCompleteTextViewStatus.addTextChangedListener(this);
		autoCompleteTextViewStatus.setOnFocusChangeListener(this);
		autoCompleteTextViewStatus.setOnClickListener(this);
		autoCompleteTextViewStatus.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, item));
		autoCompleteTextViewStatus.setText("Alive");
		
		autoCompleteTextViewTreatment = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextViewTreatment);
		autoCompleteTextViewTreatment.addTextChangedListener(this);
		autoCompleteTextViewTreatment.setOnFocusChangeListener(this);
		autoCompleteTextViewTreatment.setOnClickListener(this);
		autoCompleteTextViewTreatment.setOnItemClickListener(this);

		
        switchMaleFemale = (Switch) findViewById(R.id.switch_male_female);
        if (switchMaleFemale != null)
        {
        	switchMaleFemale.setOnCheckedChangeListener(this);
        	switchMaleFemale.setOnClickListener(this);
        }

	    Button bttnTreatment = (Button) findViewById(R.id.buttonTreatment);
	    bttnTreatment.setOnClickListener(this);

        Button bttnSave = (Button) findViewById(R.id.buttonSave);
        bttnSave.setOnClickListener(this);
        
        Button bttnAddChild = (Button) findViewById(R.id.buttonAddChild);
        bttnAddChild.setOnClickListener(this);

                
        Button bttnHousehold = (Button) findViewById(R.id.buttonHousehold);
        bttnHousehold.setOnClickListener(this);

		Button bttnAddCoordinates = (Button) findViewById(R.id.buttonAddCoordinates);
		bttnAddCoordinates.setOnClickListener(this);

        Button bttnSettings = (Button) findViewById(R.id.buttonSettings);
        bttnSettings.setOnClickListener(this);

        editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        editTextLastName = (EditText) findViewById(R.id.editTextLastName);
        editTextNickname = (EditText) findViewById(R.id.editTextNickname);
        editTextAge = (EditText) findViewById(R.id.editTextAge);
        editTextNote = (EditText) findViewById(R.id.editTextNote);
        //textViewTreatment = (TextView) findViewById(R.id.textViewTreatment);
        textViewLocation = (TextView) findViewById(R.id.textViewLocation);
        
        editTextLastName.setText(sHeadOfHouseholdLastName);
        
        radioGroupAgeUnit = (RadioGroup) findViewById(R.id.radioAgeUnit);

        if (sChildUuid != null)
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
		else if (v.getId() == R.id.autoCompleteTextViewTreatment)
			autoCompleteTextViewTreatment.showDropDown();
		else if (v.getId() == R.id.switch_male_female)
		{
			
		}
		else if (v.getId() == R.id.buttonTreatment)
		{
			Intent intent = new Intent(ChildDetails.this, Treatment.class);
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
             
             String sLocation = textViewLocation.getText().toString();
             if (!sLocation.isEmpty())
            	 intent.putExtra(Constants.INTENT_EXTRA_LOCATION, sLocation);
             
             if (indexAutoCompleteTextViewTreatment > 0)
            	 intent.putExtra(Constants.INTENT_EXTRA_TREATMENT_UUID, sTreatmentUuids[indexAutoCompleteTextViewTreatment]);

            startActivity(intent);
		}
		else if (v.getId() == R.id.buttonSave)
		{
			if (onSaveClicked() == true)
			{
	            Intent intent = new Intent(this, HouseholdReview.class);
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
		else if (v.getId() == R.id.buttonAddChild)
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
		}
		else if (v.getId() == R.id.buttonHousehold)
		{
			Intent intent = new Intent(ChildDetails.this, HeadOfHousehold.class);
			startActivity(intent);

		}
		else if (v.getId() == R.id.buttonAddCoordinates)
		{
			saveGPSLocation();
		}
		else if (v.getId() == R.id.buttonSettings)
		{
			startActivity(new Intent(ChildDetails.this, UserSettings.class));
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
		else if ((v.getId() == R.id.autoCompleteTextViewTreatment) && (isFocused == true))
		{
			autoCompleteTextViewTreatment.showDropDown();
			autoCompleteTextViewTreatment.setInputType(InputType.TYPE_NULL);
			inputMethodManager.hideSoftInputFromWindow(autoCompleteTextViewTreatment.getWindowToken(),0);
		}
		
	}
	
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    	if (buttonView.getId() == R.id.switch_male_female)
    	{
    	       if (isChecked)
    	       {
    	    	   sGender = "Male";
    	       }
    	       else
    	       {
    	    	   sGender = "Female";
    	       }
    	}
    }
    
	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int pos, long id) 
	{
		indexAutoCompleteTextViewTreatment = pos;

	}

    
	private boolean onSaveClicked()
	{
		String sFirstName = editTextFirstName.getText().toString();
		String sLastName = editTextLastName.getText().toString();
		String sNickname = editTextNickname.getText().toString();
		String sAge = editTextAge.getText().toString();
		String sStatus = autoCompleteTextViewStatus.getText().toString();
		int age = 0;
		int selectedId = radioGroupAgeUnit.getCheckedRadioButtonId();
		radioButtonAgeUnit = (RadioButton) findViewById(selectedId);
		String sAgeUnit = radioButtonAgeUnit.getText().toString();
		String sUuidAgent = settings.getString(Constants.SETTINGS_KEY_UUID_AGENT, "");

		
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
		
		if (sAge.isEmpty())
		{
			showToast("Age is missing. No record is saved.");
			return false;
		}
		else
			age = Integer.parseInt(sAge);
		
		if (sStatus.isEmpty())
		{
			showToast("Status is missing. No record is saved.");
			return false;
		}

		
		if (sNickname.isEmpty())
			sNickname = sFirstName;


				
		Date date = new Date();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sDateTime = formatter.format(date);

		
		SQLiteDatabase db = openOrCreateDatabase(Constants.DATABASE_NAME, MODE_PRIVATE, null);
		String sql = null;
		
		
		sql = String.format("SELECT * FROM child WHERE first_name = '%s' AND last_name = '%s' AND uuid_hoh = '%s';",
				sFirstName,
				sLastName,
				sHeadOfHouseholdUuid);
		
		Cursor cursor = db.rawQuery(sql, null);
		int results = cursor.getCount();

		if (results == 0)
		{
			sChildUuid = generateUUID();
			sChildFirstName = sFirstName;
			sChildLastName = sLastName;

			sql = String.format("INSERT INTO child " +
					" (uuid_child, first_name, last_name, nickname, age, age_unit, gender, status, note, uuid_hoh, uuid_guardian, date_created, date_last_modified, uuid_agent_created, uuid_agent_modified) " +
					" VALUES " +
					"('%s', '%s', '%s', '%s', %d, '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');", 
					sChildUuid,
					sFirstName, 
					sLastName,
					sNickname,
					age,
					sAgeUnit,
					sGender, 
					sStatus,
					editTextNote.getText().toString(),
					sHeadOfHouseholdUuid,
					sGuardianUuid,
					sDateTime,
					sDateTime,
					sUuidAgent,
					sUuidAgent);
			
			dbExecSQL(db, sql);
			
			showToast("Added new Child: " + sFirstName + " " + sLastName);
		}
		else
		{
			cursor.moveToFirst();
			String subjectUuid = cursor.getString(cursor.getColumnIndex("uuid_child"));

			sql = String.format("UPDATE child " +
					"SET nickname = '%s', " +
					"age = %d, " +
					"age_unit = '%s', " +
					"gender = '%s', " +
					"status = '%s', " +
					"note = '%s', " +
					"date_last_modified = '%s', " +
					"uuid_agent_modified = '%s' " +
					"WHERE uuid_child = '%s';",
					sNickname,
					age, 
					sAgeUnit,
					sGender, 
					sStatus,
					editTextNote.getText().toString(),
					sDateTime,
					sUuidAgent,
					subjectUuid);
						
			dbExecSQL(db, sql);
			
			showToast("Updated Child: " + sFirstName + " " + sLastName);
		}
		db.close();
		return true;

	}
	
	private void initFields()
	{
		String firstName = null, lastName = null, nickname = null, ageUnit = null, gender = null, status = null, note = null, dateCreated = null, dateLastModified = null;
		String hohFirstName = null, hohLastName = null, gps = null, village = null, country = null;
		String guardianFirstName = null, guardianLastName = null;
		String uuidAgentCreated = null, uuidAgentModified = null;
		String agentCreatedFirstName = null, agentCreatedLastName = null;
		String agentModifiedFirstName = null, agentModifiedLastName = null;;
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
		
		sql = String.format("SELECT * from treatment WHERE uuid_child = '%s';", sChildUuid);
		
		c = db.rawQuery(sql, null);
		results = c.getCount();
		
		if (results > 0)
		{
			sTreatments = new String[results + 1];
			sTreatments[0] = "";
			sTreatmentUuids = new String[results + 1];
			sTreatmentUuids[0] = "";
			int n = 1;
			if (c != null ) 
			{
			    if  (c.moveToFirst()) 
			    {
	
			        do {
			        	String treatment = c.getString(c.getColumnIndex("date_created"));
			            String uuid = c.getString(c.getColumnIndex("uuid_treatment"));
			            sTreatments[n] = treatment;
			            sTreatmentUuids[n] = uuid;
			            n++;
	
			        }while (c.moveToNext());
			    } 
			}
		}


		
		c.close();
		db.close();
		
		if (sTreatments != null)
			autoCompleteTextViewTreatment.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, sTreatments));
		
		textViewLocation.setText(village);
		
		editTextFirstName.setText(firstName);
		editTextLastName.setText(lastName);
		editTextNickname.setText(nickname);
		editTextAge.setText(Integer.toString(age));
		autoCompleteTextViewStatus.setText(status);
		editTextNote.setText(note);
		
		if (gender.equals("Male"))
			switchMaleFemale.setChecked(true);
		else
			switchMaleFemale.setChecked(false);
				
		RadioButton radioYear = (RadioButton) findViewById(R.id.radioYear);
		RadioButton radioMonth = (RadioButton) findViewById(R.id.radioMonth);
		RadioButton radioWeek = (RadioButton) findViewById(R.id.radioWeek);
		
		if (ageUnit.equals("Year"))
		{
			radioYear.setChecked(true);
			radioMonth.setChecked(false);
			radioWeek.setChecked(false);
		}
		else if (ageUnit.equals("Month"))
		{
			radioYear.setChecked(false);
			radioMonth.setChecked(true);
			radioWeek.setChecked(false);
		}
		else if (ageUnit.equals("Week"))
		{
			radioYear.setChecked(false);
			radioMonth.setChecked(false);
			radioWeek.setChecked(true);
		}

		
	}


}
