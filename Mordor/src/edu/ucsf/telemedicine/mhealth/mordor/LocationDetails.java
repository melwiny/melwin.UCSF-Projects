package edu.ucsf.telemedicine.mhealth.mordor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

public class LocationDetails extends RootActivity  implements OnClickListener, TextWatcher, OnFocusChangeListener {
	
	AutoCompleteTextView autoCompleteTextViewPhase, autoCompleteTextViewVillage;
	
	String item[]= Constants.PROJECT_PHASES;
	String village[] = Constants.getVillages();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_details);
		
		initSettingsLatLong();
		
		autoCompleteTextViewVillage = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextViewVillage);
		autoCompleteTextViewVillage.addTextChangedListener(this);
		autoCompleteTextViewVillage.setOnFocusChangeListener(this);
		autoCompleteTextViewVillage.setOnClickListener(this);
		autoCompleteTextViewVillage.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, village));

		
		autoCompleteTextViewPhase = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextViewPhase);
		autoCompleteTextViewPhase.addTextChangedListener(this);
		autoCompleteTextViewPhase.setOnFocusChangeListener(this);
		autoCompleteTextViewPhase.setOnClickListener(this);
		autoCompleteTextViewPhase.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, item));

		Button bttnConfirm = (Button) findViewById(R.id.buttonConfirm);
		bttnConfirm.setOnClickListener(this);

		Button bttnHousehold = (Button) findViewById(R.id.buttonHousehold);
		bttnHousehold.setOnClickListener(this);

		Button bttnAddCoordinates = (Button) findViewById(R.id.buttonAddCoordinates);
		bttnAddCoordinates.setOnClickListener(this);

		Button bttnSettings = (Button) findViewById(R.id.buttonSettings);
		bttnSettings.setOnClickListener(this);
				
		
		autoCompleteTextViewVillage.setText(settings.getString(Constants.SETTINGS_KEY_VILLAGE, ""));
		autoCompleteTextViewPhase.setText(settings.getString(Constants.SETTINGS_KEY_PHASE, ""));

		


	}
	



	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.buttonSettings) 
		{
			startActivity(new Intent(LocationDetails.this, UserSettings.class));
		}
		else if (v.getId() == R.id.buttonConfirm) 
		{
			String village = autoCompleteTextViewVillage.getText().toString();
			String country = Constants.findCountryOfVillage(village);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString(Constants.SETTINGS_KEY_VILLAGE, village);
			editor.putString(Constants.SETTINGS_KEY_COUNTRY, country);
			editor.putString(Constants.SETTINGS_KEY_PHASE, autoCompleteTextViewPhase.getText().toString());
			editor.commit();
			
			showToast("Location information is confirmed!");
			
			startActivity(new Intent(this,  Start.class));

		}
		else if (v.getId() == R.id.buttonAddCoordinates)
		{
			saveGPSLocation();
		}
		else if (v.getId() == R.id.autoCompleteTextViewVillage)
		{
			this.autoCompleteTextViewVillage.showDropDown();
		}
		else if (v.getId() == R.id.autoCompleteTextViewPhase)
		{
			this.autoCompleteTextViewPhase.showDropDown();
		}
		else if (v.getId() == R.id.buttonHousehold)
		{
			boolean passed = true;
			String curLatitude = settings.getString("latitude", "");
			String curLongitude = settings.getString("longitude", "");
			if (curLatitude == null || curLatitude.equals("") || curLongitude == null || curLongitude.equals(""))
			{
				passed = false;
				showToast("Please add coordinates!");
			}
			
			String uuidAgent = settings.getString(Constants.SETTINGS_KEY_UUID_AGENT, "");
			if (uuidAgent.isEmpty())
			{
				passed = false;
				showToast("Please setup user settings!");
			}

			if (passed)
			{
				startActivity(new Intent(LocationDetails.this, HeadOfHousehold.class));
			}
		}
	}


	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
		
	}


	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onFocusChange(View v, boolean isFocused) {
		if ((v.getId() == R.id.autoCompleteTextViewPhase) && (isFocused == true))
		{
			this.autoCompleteTextViewPhase.setText("");
			this.autoCompleteTextViewPhase.showDropDown();
			this.autoCompleteTextViewPhase.setInputType(InputType.TYPE_NULL);
			InputMethodManager inputMethodManager = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(this.autoCompleteTextViewPhase.getWindowToken(),0);
		}
		else if ((v.getId() == R.id.autoCompleteTextViewVillage) && (isFocused == true))
		{
			this.autoCompleteTextViewVillage.setText("");
			this.autoCompleteTextViewVillage.showDropDown();
			this.autoCompleteTextViewVillage.setInputType(InputType.TYPE_NULL);
			InputMethodManager inputMethodManager = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(this.autoCompleteTextViewVillage.getWindowToken(),0);
		}
		
	}
	

}
