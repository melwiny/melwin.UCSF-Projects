package edu.ucsf.telemedicine.mhealth.mordor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

public class UserSettings extends RootActivity implements OnClickListener, TextWatcher, OnFocusChangeListener {
	
	private EditText editTextFirstName, editTextLastName, 
		editTextEmail, 
		editTextMobile1, editTextMobile2;
	private String sAgentUuid = null;
	private byte[] bytesPhoto = null;
	
	ImageView imageViewPhoto = null;
	private static final int CAMERA_PIC_REQUEST = 1313;
	File file;
	private int hitCount = 0;
	AutoCompleteTextView autoCompleteTextViewPhase, autoCompleteTextViewVillage;
	String item[]=Constants.PROJECT_PHASES;
	String village[]=Constants.getVillages();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_settings);
		
		hitCount = 0;
		
		editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);

		editTextLastName = (EditText) findViewById(R.id.editTextLastName);

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
		
		String village = settings.getString(Constants.SETTINGS_KEY_VILLAGE, "");
		autoCompleteTextViewVillage.setText(village);

		String phase = settings.getString(Constants.SETTINGS_KEY_PHASE, "");
		autoCompleteTextViewPhase.setText(phase);
		
		editTextEmail = (EditText) findViewById(R.id.editTextEmail);
		
		editTextMobile1 = (EditText) findViewById(R.id.editTextMobile1);

		editTextMobile2 = (EditText) findViewById(R.id.editTextMobile2);


		Button bttnSyncSettings = (Button) findViewById(R.id.buttonSyncSettings);
		bttnSyncSettings.setOnClickListener(this);
		
		Button bttnHousehold = (Button) findViewById(R.id.buttonHousehold);
		bttnHousehold.setOnClickListener(this);

		Button bttnAddCoordinates = (Button) findViewById(R.id.buttonAddCoordinates);
		bttnAddCoordinates.setOnClickListener(this);
		
		Button bttnSave = (Button) findViewById(R.id.buttonSave);
		bttnSave.setOnClickListener(this);

		Button bttnAddPhoto = (Button) findViewById(R.id.buttonAddPhoto);
		bttnAddPhoto.setOnClickListener(this);
		
		Button bttnSecret = (Button) findViewById(R.id.buttonSecret);
		bttnSecret.setOnClickListener(this);

		
		imageViewPhoto = (ImageView) findViewById(R.id.imageViewPhoto);

		sAgentUuid = settings.getString(Constants.SETTINGS_KEY_UUID_AGENT, "");
		if (sAgentUuid != null && !sAgentUuid.isEmpty())
			initFields();


	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.buttonSyncSettings) {
			startActivity(new Intent(UserSettings.this, SyncSettings.class));
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
				showToast("Please save user settings!");
			}

			if (passed)
				startActivity(new Intent(UserSettings.this, HeadOfHousehold.class));
		}
		else if (v.getId() == R.id.buttonAddCoordinates)
		{
			saveGPSLocation();
		}
		else if (v.getId() == R.id.buttonSave)
		{
			onSaveClicked();
			
			
		}
		else if (v.getId() == R.id.buttonAddPhoto)
		{
			file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/myImage.jpg");
			Uri outputFileUri = Uri.fromFile(file);
			
			Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, outputFileUri);
			startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
		}
		else if (v.getId() == R.id.buttonSecret)
		{
			hitCount++;
			if (hitCount > 9)
			{
				dbRebuildAllDBTables();
				showToast("All tables are rebuilt!!");
				hitCount = 0;
			}
		}
		else if (v.getId() == R.id.autoCompleteTextViewPhase)
		{
			this.autoCompleteTextViewPhase.showDropDown();
		}
		else if (v.getId() == R.id.autoCompleteTextViewVillage)
		{
			this.autoCompleteTextViewVillage.showDropDown();
		}
		
	}
		
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
	     super.onActivityResult(requestCode, resultCode, data);
	     
	     Bitmap b;
	     
	     try 
	     {
	        b = Media.getBitmap(getContentResolver(), Uri.fromFile(file));
	        
	        int width = b.getWidth();
	        int height = b.getHeight();
	         
	        int newWidth = 250; // width in pixels
	        int newHeight = newWidth * height / width; // height in pixels
	            
	        Bitmap scaledBmp = Bitmap.createScaledBitmap(b, newWidth, newHeight, true);

	        Matrix matrix = new Matrix();
	        matrix.preScale(1.0f, -1.0f);
	        matrix.postRotate(180);
	        Bitmap newBmp = Bitmap.createBitmap(scaledBmp , 0, 0, scaledBmp.getWidth(), scaledBmp.getHeight(), matrix, true);
	        
	        ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        newBmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
	        bytesPhoto = stream.toByteArray();
	        
	        imageViewPhoto.setImageBitmap(newBmp);
 
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
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

	
	private void onSaveClicked()
	{		
		String sFirstName = editTextFirstName.getText().toString();
		String sLastName = editTextLastName.getText().toString();
		String sVillage = autoCompleteTextViewVillage.getText().toString();
		String sPhase = autoCompleteTextViewPhase.getText().toString();
		String sEmail = editTextEmail.getText().toString();
		String sMobile1 = editTextMobile1.getText().toString();
		String sMobile2 = editTextMobile2.getText().toString();
		//byte[] bytesPhoto = getByteArrayFromImageFile("user_photo");
		
		String sCountry = Constants.findCountryOfVillage(sVillage);


		
		if (sFirstName.isEmpty() || sLastName.isEmpty())
		{
			showToast("Please enter First and Last name");
			return;
		}
		else if (sEmail.isEmpty())
		{
			showToast("Please enter Email");
			return;
		}
		else if (sMobile1.isEmpty())
		{
			showToast("Please enter Mobile 1");
			return;
		}
		else if (sVillage.isEmpty())
		{
			showToast("Please select a village");
			return;
		}
		else if (sPhase.isEmpty())
		{
			showToast("Please select a phase");
			return;
		}
		else if (bytesPhoto == null)
		{
			bytesPhoto = "".getBytes();
		}

		
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(Constants.SETTINGS_KEY_VILLAGE, sVillage);
		editor.putString(Constants.SETTINGS_KEY_COUNTRY, sCountry);
		editor.putString(Constants.SETTINGS_KEY_PHASE, sPhase);
		editor.commit();


						
		Date date = new Date();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sDateTime = formatter.format(date);
		

		
		SQLiteDatabase db = openOrCreateDatabase(Constants.DATABASE_NAME, MODE_PRIVATE, null);
		String sql = null;
		Boolean newAgent = true;
		Cursor cursor = null;
		int results = 0;
		
		// Check if this is a new agent or just updating an old agent's record
		// If first and last names, and email, and mobile number all match, this is an update.
		// if first and last names, and email match, update.
		// If first and last names, and mobile match, update.
		// This allows the agent to change his email or mobile number without creating a new agent ID.
		// This also prevents agents with same first and last names.
		// If all checks fails, this is a new agent and needs to assign a new agent id.
		sql = String.format("SELECT * FROM agent WHERE first_name = '%s' AND last_name = '%s' AND email = '%s' AND mobile_1 = '%s';",
				sFirstName,
				sLastName,
				sEmail, 
				sMobile1);
		
		cursor = db.rawQuery(sql, null);
		results = cursor.getCount();
		
		if (results == 0)
		{
			sql = String.format("SELECT * FROM agent WHERE first_name = '%s' AND last_name = '%s' AND email = '%s' ;",
					sFirstName,
					sLastName,
					sEmail);
			
			cursor = db.rawQuery(sql, null);
			results = cursor.getCount();
			
			if (results == 0)
			{
				sql = String.format("SELECT * FROM agent WHERE first_name = '%s' AND last_name = '%s' AND email = '%s' ;",
						sFirstName,
						sLastName,
						sEmail);
				
				cursor = db.rawQuery(sql, null);
				results = cursor.getCount();
				
				if (results > 0)
					newAgent = false;

			}
			else
				newAgent = false;
		}
		else
			newAgent = false;

		if (newAgent)
		{
			String sUuidAgent = generateUUID();
			settingsPutKeyValue(Constants.SETTINGS_KEY_UUID_AGENT, sUuidAgent);


			sql = String.format("INSERT INTO agent " +
					" (uuid_agent, first_name, last_name, email, mobile_1, mobile_2, photo, date_created, date_last_modified) " +
					" VALUES " +
					"('%s', '%s', '%s', '%s', '%s', '%s', ?, '%s', '%s');", 
					sUuidAgent,
					sFirstName, 
					sLastName,
					sEmail,
					sMobile1,
					sMobile2,
					//sPhoto, 
					sDateTime,
					sDateTime);
			
			SQLiteStatement sqlStatement = db.compileStatement(sql);
			sqlStatement.clearBindings();
			sqlStatement.bindBlob(1, bytesPhoto);
			sqlStatement.executeInsert();


			showToast("Added new Agent: " + sFirstName + " " + sLastName);
		}
		else
		{
			sql = String.format("UPDATE agent " +
					"SET email = '%s', " +
					"mobile_1 = '%s', " +
					"mobile_2 = '%s', " +
					"photo = ?, " +
					"date_last_modified = '%s' " +
					"WHERE first_name = '%s' AND last_name = '%s' ;",
					sEmail,
					sMobile1, 
					sMobile2,
					sDateTime,
					sFirstName,
					sLastName);
			
			SQLiteStatement sqlStatement = db.compileStatement(sql);
			sqlStatement.clearBindings();
			sqlStatement.bindBlob(1, bytesPhoto);
			sqlStatement.executeUpdateDelete();
						
			showToast("Updated Agent: " + sFirstName + " " + sLastName);
		}
		db.close();
		
	}
	
	private void initFields()
	{
		String uuidAgent = null;
		String firstName = null, lastName = null, email = null, mobile1 = null, mobile2 = null, dateCreated = null, dateLastModified = null;
		
		uuidAgent = settings.getString(Constants.SETTINGS_KEY_UUID_AGENT, "");
		if (uuidAgent.isEmpty())
			return;
		
		SQLiteDatabase db = openOrCreateDatabase(Constants.DATABASE_NAME, MODE_PRIVATE, null);
		String sql = null;
		sql = String.format("SELECT * FROM agent WHERE uuid_agent = '%s' ; ", uuidAgent);

		
		
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
		            email = c.getString(c.getColumnIndex("email"));
		            mobile1 = c.getString(c.getColumnIndex("mobile_1"));
		            mobile2 = c.getString(c.getColumnIndex("mobile_2"));
		            bytesPhoto = c.getBlob(c.getColumnIndex("photo"));
		            dateCreated = c.getString(c.getColumnIndex("date_created"));
		            dateLastModified = c.getString(c.getColumnIndex("date_last_modified"));
		            
		            
		            break;
		            

		        }while (c.moveToNext());
		    } 
		}
		
		c.close();
		db.close();
		
		settingsPutKeyValue(Constants.SETTINGS_KEY_UUID_AGENT, uuidAgent);
		
		editTextFirstName.setText(firstName);
		editTextLastName.setText(lastName);
		editTextEmail.setText(email);
		editTextMobile1.setText(mobile1);
		editTextMobile2.setText(mobile2);
		
		Bitmap photo = BitmapFactory.decodeByteArray(bytesPhoto , 0, bytesPhoto.length);
		if (photo != null)
			imageViewPhoto.setImageBitmap(photo);
		


		
	}

	

}
