package edu.ucsf.telemedicine.mhealth.mordor;


import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class SyncSettings extends RootActivity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sync_settings);
		
		Button bttnSyncInterval = (Button) findViewById(R.id.buttonSyncInterval);
		bttnSyncInterval.setOnClickListener(this);
		
		Button bttnSyncRetryInterval = (Button) findViewById(R.id.buttonSyncRetryInterval);
		bttnSyncRetryInterval.setOnClickListener(this);
		
		Button bttnSyncNow = (Button) findViewById(R.id.buttonSyncNow);
		bttnSyncNow.setOnClickListener(this);

		Button bttnHeadOfHousehold = (Button) findViewById(R.id.buttonHousehold);
		bttnHeadOfHousehold.setOnClickListener(this);

		Button bttnAddCoordinates = (Button) findViewById(R.id.buttonAddCoordinates);
		bttnAddCoordinates.setOnClickListener(this);

		Button bttnSettings = (Button) findViewById(R.id.buttonSettings);
		bttnSettings.setOnClickListener(this);


	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.buttonSyncInterval) {
		}
		else if (v.getId() == R.id.buttonSyncRetryInterval) {
		}
		else if (v.getId() == R.id.buttonSyncNow)
		{
			onBttnSyncNowClicked();
		}
		else if (v.getId() == R.id.buttonHousehold){
			startActivity(new Intent(SyncSettings.this, HeadOfHousehold.class));
		}
		
	}
	
	private void onBttnSyncNowClicked()
	{
		if (!isOnline())
		{
			showToast("Device is not online. Please check device Settings");
			return;
		}
		
		// On fresh installed app, clear the online device image table.
		// This triggers retrieving all data records from all tables, and setting up new image
		// on the device image table.
	    String sDatetimeLastSyn = settings.getString(Constants.SETTINGS_KEY_DATETIME_LAST_SYN, "");
	    if (sDatetimeLastSyn.isEmpty())
	    	doClearSerialTable();
	    
		doSyncHeadOfHousehold();
		doSyncGuardian();
		doSyncChild();
		doSyncTreatment();
		doSyncAgent();
		doSyncCensus();
		
		Date date = new Date();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sNow = formatter.format(date);
	    
	    settingsPutKeyValue(Constants.SETTINGS_KEY_DATETIME_LAST_SYN, sNow);
	    
	    showToast("Sync completed");

	}
	
	private void doClearSerialTable()
	{
		String serial = getDeviceSerialId();
		Dictionary d = new Hashtable();  
		 
	    d.put("table", "serial");
	    d.put("serial", serial);
	    d.put("message", "clear_table" );
	    
	    new HttpPostTask().execute(d);		
	}

	
	private void doSyncHeadOfHousehold()
	{
		String xmlHeadOfHousehold = getXmlTableHeadOfHousehold();
		if (xmlHeadOfHousehold == null)
			xmlHeadOfHousehold = "null";
		String serial = getDeviceSerialId(); 
		
		Dictionary d = new Hashtable();  
		 
	    d.put("table", "head_of_household");
	    d.put("serial", serial);
	    d.put("message", xmlHeadOfHousehold );
	    	    
	    
	    new HttpPostTask().execute(d);
	    
	    
	}
	
	private void doSyncGuardian()
	{
		String xmlGuardian = getXmlTableGuardian();
		if (xmlGuardian == null)
			xmlGuardian = "null";
		String serial = getDeviceSerialId(); 
		
		Dictionary d = new Hashtable();  
		 
	    d.put("table", "guardian");
	    d.put("serial", serial);
	    d.put("message", xmlGuardian );
	    
	    
	    new HttpPostTask().execute(d);
	    
	}

	private void doSyncChild()
	{
		String xmlChild = getXmlTableChild();
		if (xmlChild == null)
			xmlChild = "null";
		String serial = getDeviceSerialId(); 
		
		Dictionary d = new Hashtable();  
		 
	    d.put("table", "child");
	    d.put("serial", serial);
	    d.put("message", xmlChild );
	    
	    
	    new HttpPostTask().execute(d);
	    
	}
	
	private void doSyncTreatment()
	{
		String xmlTreatment = getXmlTableTreatment();
		if (xmlTreatment == null)
			xmlTreatment = "null";
		String serial = getDeviceSerialId(); 
		
		Dictionary d = new Hashtable();  
		 
	    d.put("table", "treatment");
	    d.put("serial", serial);
	    d.put("message", xmlTreatment );
	    
	    
	    new HttpPostTask().execute(d);
	    
	}

	private void doSyncAgent()
	{
		
		String xmlAgent = getXmlTableAgent();
		if (xmlAgent == null)
			xmlAgent = "null";
		String serial = getDeviceSerialId(); 
		
		Dictionary d = new Hashtable();  
		 
	    d.put("table", "agent");
	    d.put("serial", serial);
	    d.put("message", xmlAgent );
	    
	    
	    new HttpPostTask().execute(d);
	    
	}

	private void doSyncCensus()
	{
		String xmlCensus = getXmlTableCensus();
		if (xmlCensus == null)
			xmlCensus = "null";
		String serial = getDeviceSerialId(); 
		
		Dictionary d = new Hashtable();  
		 
	    d.put("table", "census");
	    d.put("serial", serial);
	    d.put("message", xmlCensus );
	    
	    
	    new HttpPostTask().execute(d);
	    
	}


	
	private String getXmlTableHeadOfHousehold()
	{
		String uuidHeadOfHousehold = null;
		String firstName = null, lastName = null, age = null, gender = null, note = null;
		String gps = null, village = null, country = null;
		String dateCreated = null, dateLastModified = null;
		
		String uuidAgentCreated = null, uuidAgentModified = null;
		String agentCreatedFirstName = null, agentCreatedLastName = null;
		String agentModifiedFirstName = null, agentModifiedLastName = null;
		
		Date date = new Date();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sNow = formatter.format(date);

		String sDatetimeLastSyn = settings.getString(Constants.SETTINGS_KEY_DATETIME_LAST_SYN, "");
		
		SQLiteDatabase db = openOrCreateDatabase(Constants.DATABASE_NAME, MODE_PRIVATE, null);
		String sql = null;
		
		if (sDatetimeLastSyn.isEmpty())
			sql = String.format("SELECT * from head_of_household;");
		else
			sql = String.format("SELECT * from head_of_household WHERE date_last_modified > '%s';", sDatetimeLastSyn);
		
		Cursor c = db.rawQuery(sql, null);
		int results = c.getCount();
		if (results == 0)
			return null;
		
		StringBuilder xml = new StringBuilder();
		xml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		xml.append("<HeadOfHouseholds xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
				+ "DateTimeStamp=\"" + sNow + "\">");


		
		if (c != null ) 
		{
		    if  (c.moveToFirst()) 
		    {

		        do {
		        	xml.append("<HeadOfHousehold ");
		        	
		        	uuidHeadOfHousehold = c.getString(c.getColumnIndex("uuid_hoh"));
		        	firstName = c.getString(c.getColumnIndex("first_name"));
		            lastName = c.getString(c.getColumnIndex("last_name"));
		            age = c.getString(c.getColumnIndex("age"));
		            gender = c.getString(c.getColumnIndex("gender"));
		            note = c.getString(c.getColumnIndex("note"));
		            gps = c.getString(c.getColumnIndex("gps"));
		            village = c.getString(c.getColumnIndex("village"));
		            country = c.getString(c.getColumnIndex("country"));
		            dateCreated = c.getString(c.getColumnIndex("date_created"));
		            dateLastModified = c.getString(c.getColumnIndex("date_last_modified"));
		            uuidAgentCreated = c.getString(c.getColumnIndex("uuid_agent_created"));
		            uuidAgentModified = c.getString(c.getColumnIndex("uuid_agent_modified"));
		            
		            xml.append(" uuid_hoh=" + "\"" + uuidHeadOfHousehold + "\"");
		            xml.append(" first_name=" + "\"" + firstName + "\"");
		            xml.append(" last_name=" + "\"" + lastName + "\"");
		            xml.append(" age=" + "\"" + age + "\"");
		            xml.append(" gender=" + "\"" + gender + "\"");
		            xml.append(" note=" + "\"" + note + "\"");
		            xml.append(" gps=" + "\"" + gps + "\"");
		            xml.append(" village=" + "\"" + village + "\"");
		            xml.append(" country=" + "\"" + country + "\"");
		            xml.append(" date_created=" + "\"" + dateCreated + "\"");
		            xml.append(" date_last_modified=" + "\"" + dateLastModified + "\"");
		            xml.append(" uuid_agent_created=" + "\"" + uuidAgentCreated + "\"");
		            xml.append(" uuid_agent_modified=" + "\"" + uuidAgentModified + "\"");
		            
		            xml.append(" />");

		        }while (c.moveToNext());
		    } 
		}
		
		c.close();
		db.close();
		
		xml.append("</HeadOfHouseholds>");
		
		return xml.toString();
		

	}
	
	private String getXmlTableGuardian()
	{
		String uuidGuardian = null;
		String firstName = null, lastName = null, age = null, gender = null, pregnant = null, note = null;
		String uuid_hoh = null;
		String dateCreated = null, dateLastModified = null;
		
		String uuidAgentCreated = null, uuidAgentModified = null;
		String agentCreatedFirstName = null, agentCreatedLastName = null;
		String agentModifiedFirstName = null, agentModifiedLastName = null;
		
		Date date = new Date();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sNow = formatter.format(date);

		String sDatetimeLastSyn = settings.getString(Constants.SETTINGS_KEY_DATETIME_LAST_SYN, "");
		
		SQLiteDatabase db = openOrCreateDatabase(Constants.DATABASE_NAME, MODE_PRIVATE, null);
		String sql = null;
		
		if (sDatetimeLastSyn.isEmpty())
			sql = String.format("SELECT * from guardian;");
		else
			sql = String.format("SELECT * from guardian WHERE date_last_modified > '%s';", sDatetimeLastSyn);
		
		Cursor c = db.rawQuery(sql, null);
		int results = c.getCount();
		if (results == 0)
			return null;
		
		StringBuilder xml = new StringBuilder();
		xml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		xml.append("<Guardians xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
				+ "DateTimeStamp=\"" + sNow + "\">");


		
		if (c != null ) 
		{
		    if  (c.moveToFirst()) 
		    {

		        do {
		        	xml.append("<Guardian ");
		        	
		        	uuidGuardian = c.getString(c.getColumnIndex("uuid_guardian"));
		        	firstName = c.getString(c.getColumnIndex("first_name"));
		            lastName = c.getString(c.getColumnIndex("last_name"));
		            age = c.getString(c.getColumnIndex("age"));
		            gender = c.getString(c.getColumnIndex("gender"));
		            pregnant = c.getString(c.getColumnIndex("pregnant"));
		            note = c.getString(c.getColumnIndex("note"));
		            uuid_hoh = c.getString(c.getColumnIndex("uuid_hoh"));
		            dateCreated = c.getString(c.getColumnIndex("date_created"));
		            dateLastModified = c.getString(c.getColumnIndex("date_last_modified"));
		            uuidAgentCreated = c.getString(c.getColumnIndex("uuid_agent_created"));
		            uuidAgentModified = c.getString(c.getColumnIndex("uuid_agent_modified"));
		            
		            xml.append(" uuid_guardian=" + "\"" + uuidGuardian + "\"");
		            xml.append(" first_name=" + "\"" + firstName + "\"");
		            xml.append(" last_name=" + "\"" + lastName + "\"");
		            xml.append(" age=" + "\"" + age + "\"");
		            xml.append(" gender=" + "\"" + gender + "\"");
		            xml.append(" pregnant=" + "\"" + pregnant + "\"");
		            xml.append(" note=" + "\"" + note + "\"");
		            xml.append(" uuid_hoh=" + "\"" + uuid_hoh + "\"");
		            xml.append(" date_created=" + "\"" + dateCreated + "\"");
		            xml.append(" date_last_modified=" + "\"" + dateLastModified + "\"");
		            xml.append(" uuid_agent_created=" + "\"" + uuidAgentCreated + "\"");
		            xml.append(" uuid_agent_modified=" + "\"" + uuidAgentModified + "\"");
		            
		            xml.append(" />");

		        }while (c.moveToNext());
		    } 
		}
		
		c.close();
		db.close();
		
		xml.append("</Guardians>");
		
		return xml.toString();
		

	}

	private String getXmlTableChild()
	{
		String uuidChild = null;
		String firstName = null, lastName = null, nickname = null, age = null, age_unit = null, gender = null, status = null, note = null;
		String uuid_hoh = null, uuid_guardian = null;;
		String dateCreated = null, dateLastModified = null;
		
		String uuidAgentCreated = null, uuidAgentModified = null;
		String agentCreatedFirstName = null, agentCreatedLastName = null;
		String agentModifiedFirstName = null, agentModifiedLastName = null;
		
		Date date = new Date();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sNow = formatter.format(date);

		String sDatetimeLastSyn = settings.getString(Constants.SETTINGS_KEY_DATETIME_LAST_SYN, "");
		
		SQLiteDatabase db = openOrCreateDatabase(Constants.DATABASE_NAME, MODE_PRIVATE, null);
		String sql = null;
		
		if (sDatetimeLastSyn.isEmpty())
			sql = String.format("SELECT * from child;");
		else
			sql = String.format("SELECT * from child WHERE date_last_modified > '%s';", sDatetimeLastSyn);
		
		Cursor c = db.rawQuery(sql, null);
		int results = c.getCount();
		if (results == 0)
			return null;
		
		StringBuilder xml = new StringBuilder();
		xml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		xml.append("<Children xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
				+ "DateTimeStamp=\"" + sNow + "\">");


		
		if (c != null ) 
		{
		    if  (c.moveToFirst()) 
		    {

		        do {
		        	xml.append("<Child ");
		        	
		        	uuidChild = c.getString(c.getColumnIndex("uuid_child"));
		        	firstName = c.getString(c.getColumnIndex("first_name"));
		            lastName = c.getString(c.getColumnIndex("last_name"));
		            nickname = c.getString(c.getColumnIndex("nickname"));
		            age = c.getString(c.getColumnIndex("age"));
		            age_unit = c.getString(c.getColumnIndex("age_unit"));
		            gender = c.getString(c.getColumnIndex("gender"));
		            status = c.getString(c.getColumnIndex("status"));
		            note = c.getString(c.getColumnIndex("note"));
		            uuid_hoh = c.getString(c.getColumnIndex("uuid_hoh"));
		            uuid_guardian = c.getString(c.getColumnIndex("uuid_guardian"));
		            dateCreated = c.getString(c.getColumnIndex("date_created"));
		            dateLastModified = c.getString(c.getColumnIndex("date_last_modified"));
		            uuidAgentCreated = c.getString(c.getColumnIndex("uuid_agent_created"));
		            uuidAgentModified = c.getString(c.getColumnIndex("uuid_agent_modified"));
		            
		            xml.append(" uuid_child=" + "\"" + uuidChild + "\"");
		            xml.append(" first_name=" + "\"" + firstName + "\"");
		            xml.append(" last_name=" + "\"" + lastName + "\"");
		            xml.append(" nickname=" + "\"" + nickname + "\"");
		            xml.append(" age=" + "\"" + age + "\"");
		            xml.append(" age_unit=" + "\"" + age_unit + "\"");
		            xml.append(" gender=" + "\"" + gender + "\"");
		            xml.append(" status=" + "\"" + status + "\"");
		            xml.append(" note=" + "\"" + note + "\"");
		            xml.append(" uuid_hoh=" + "\"" + uuid_hoh + "\"");
		            xml.append(" uuid_guardian=" + "\"" + uuid_guardian + "\"");
		            xml.append(" date_created=" + "\"" + dateCreated + "\"");
		            xml.append(" date_last_modified=" + "\"" + dateLastModified + "\"");
		            xml.append(" uuid_agent_created=" + "\"" + uuidAgentCreated + "\"");
		            xml.append(" uuid_agent_modified=" + "\"" + uuidAgentModified + "\"");
		            
		            xml.append(" />");

		        }while (c.moveToNext());
		    } 
		}
		
		c.close();
		db.close();
		
		xml.append("</Children>");
		
		return xml.toString();
		

	}
	
	private String getXmlTableTreatment()
	{
		String uuidTreatment = null;
		String obtained_consent = null, status = null, dose = null, note = null;
		String uuid_child = null;
		String dateCreated = null, dateLastModified = null;
		
		String uuidAgentCreated = null, uuidAgentModified = null;
		String agentCreatedFirstName = null, agentCreatedLastName = null;
		String agentModifiedFirstName = null, agentModifiedLastName = null;
		
		Date date = new Date();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sNow = formatter.format(date);

		String sDatetimeLastSyn = settings.getString(Constants.SETTINGS_KEY_DATETIME_LAST_SYN, "");
		
		SQLiteDatabase db = openOrCreateDatabase(Constants.DATABASE_NAME, MODE_PRIVATE, null);
		String sql = null;
		
		if (sDatetimeLastSyn.isEmpty())
			sql = String.format("SELECT * from treatment;");
		else
			sql = String.format("SELECT * from treatment WHERE date_last_modified > '%s';", sDatetimeLastSyn);
		
		Cursor c = db.rawQuery(sql, null);
		int results = c.getCount();
		if (results == 0)
			return null;
		
		StringBuilder xml = new StringBuilder();
		xml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		xml.append("<Treatments xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
				+ "DateTimeStamp=\"" + sNow + "\">");


		
		if (c != null ) 
		{
		    if  (c.moveToFirst()) 
		    {

		        do {
		        	xml.append("<Treatment ");
		        	
		        	uuidTreatment = c.getString(c.getColumnIndex("uuid_treatment"));
		        	obtained_consent = c.getString(c.getColumnIndex("obtained_consent"));
		            status = c.getString(c.getColumnIndex("status"));
		            dose = c.getString(c.getColumnIndex("dose"));
		            note = c.getString(c.getColumnIndex("note"));
		            uuid_child = c.getString(c.getColumnIndex("uuid_child"));
		            dateCreated = c.getString(c.getColumnIndex("date_created"));
		            dateLastModified = c.getString(c.getColumnIndex("date_last_modified"));
		            uuidAgentCreated = c.getString(c.getColumnIndex("uuid_agent_created"));
		            uuidAgentModified = c.getString(c.getColumnIndex("uuid_agent_modified"));
		            
		            xml.append(" uuid_treatment=" + "\"" + uuidTreatment + "\"");
		            xml.append(" obtained_consent=" + "\"" + obtained_consent + "\"");
		            xml.append(" status=" + "\"" + status + "\"");
		            xml.append(" dose=" + "\"" + dose + "\"");
		            xml.append(" note=" + "\"" + note + "\"");
		            xml.append(" uuid_child=" + "\"" + uuid_child + "\"");
		            xml.append(" date_created=" + "\"" + dateCreated + "\"");
		            xml.append(" date_last_modified=" + "\"" + dateLastModified + "\"");
		            xml.append(" uuid_agent_created=" + "\"" + uuidAgentCreated + "\"");
		            xml.append(" uuid_agent_modified=" + "\"" + uuidAgentModified + "\"");
		            
		            xml.append(" />");

		        }while (c.moveToNext());
		    } 
		}
		
		c.close();
		db.close();
		
		xml.append("</Treatments>");
		
		return xml.toString();
		

	}

	private String getXmlTableAgent()
	{
		String uuidAgent = null;
		String firstName = null, lastName = null, email = null, mobile_1 = null, mobile_2 = null, photo = null;
		String dateCreated = null, dateLastModified = null;
		
		String uuidAgentCreated = null, uuidAgentModified = null;
		String agentCreatedFirstName = null, agentCreatedLastName = null;
		String agentModifiedFirstName = null, agentModifiedLastName = null;
		
		Date date = new Date();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sNow = formatter.format(date);

		String sDatetimeLastSyn = settings.getString(Constants.SETTINGS_KEY_DATETIME_LAST_SYN, "");
		
		SQLiteDatabase db = openOrCreateDatabase(Constants.DATABASE_NAME, MODE_PRIVATE, null);
		String sql = null;
		
		if (sDatetimeLastSyn.isEmpty())
			sql = String.format("SELECT * from agent;");
		else
			sql = String.format("SELECT * from agent WHERE date_last_modified > '%s';", sDatetimeLastSyn);
		
		Cursor c = db.rawQuery(sql, null);
		int results = c.getCount();
		if (results == 0)
			return null;
		
		StringBuilder xml = new StringBuilder();
		xml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		xml.append("<Agents xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
				+ "DateTimeStamp=\"" + sNow + "\">");


		
		if (c != null ) 
		{
		    if  (c.moveToFirst()) 
		    {

		        do {
		        	xml.append("<Agent ");
		        	
		        	uuidAgent = c.getString(c.getColumnIndex("uuid_agent"));
		        	firstName = c.getString(c.getColumnIndex("first_name"));
		            lastName = c.getString(c.getColumnIndex("last_name"));
		            email = c.getString(c.getColumnIndex("email"));
		            mobile_1 = c.getString(c.getColumnIndex("mobile_1"));
		            mobile_2 = c.getString(c.getColumnIndex("mobile_2"));
		            photo = ""; // c.getString(c.getColumnIndex("photo"));
		            dateCreated = c.getString(c.getColumnIndex("date_created"));
		            dateLastModified = c.getString(c.getColumnIndex("date_last_modified"));
		            
		            xml.append(" uuid_agent=" + "\"" + uuidAgent + "\"");
		            xml.append(" first_name=" + "\"" + firstName + "\"");
		            xml.append(" last_name=" + "\"" + lastName + "\"");
		            xml.append(" email=" + "\"" + email + "\"");
		            xml.append(" mobile_1=" + "\"" + mobile_1 + "\"");
		            xml.append(" mobile_2=" + "\"" + mobile_2 + "\"");
		            xml.append(" photo=" + "\"" + photo + "\"");
		            xml.append(" date_created=" + "\"" + dateCreated + "\"");
		            xml.append(" date_last_modified=" + "\"" + dateLastModified + "\"");;
		            
		            xml.append(" />");

		        }while (c.moveToNext());
		    } 
		}
		
		c.close();
		db.close();
		
		xml.append("</Agents>");
		
		return xml.toString();
		

	}
	
	private String getXmlTableCensus()
	{
		String uuidCensus = null;
		String uuid_hoh = null, phase_id = null;
		String dateCreated = null, dateLastModified = null;
		
		String uuidAgentCreated = null, uuidAgentModified = null;
		String agentCreatedFirstName = null, agentCreatedLastName = null;
		String agentModifiedFirstName = null, agentModifiedLastName = null;
		
		Date date = new Date();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sNow = formatter.format(date);

		String sDatetimeLastSyn = settings.getString(Constants.SETTINGS_KEY_DATETIME_LAST_SYN, "");
		
		SQLiteDatabase db = openOrCreateDatabase(Constants.DATABASE_NAME, MODE_PRIVATE, null);
		String sql = null;
		
		if (sDatetimeLastSyn.isEmpty())
			sql = String.format("SELECT * from census;");
		else
			sql = String.format("SELECT * from census WHERE date_last_modified > '%s';", sDatetimeLastSyn);
		
		Cursor c = db.rawQuery(sql, null);
		int results = c.getCount();
		if (results == 0)
			return null;
		
		StringBuilder xml = new StringBuilder();
		xml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		xml.append("<Censuses xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
				+ "DateTimeStamp=\"" + sNow + "\">");


		
		if (c != null ) 
		{
		    if  (c.moveToFirst()) 
		    {

		        do {
		        	xml.append("<Census ");
		        	
		        	uuidCensus = c.getString(c.getColumnIndex("uuid_census"));
		            uuid_hoh = c.getString(c.getColumnIndex("uuid_hoh"));
		            phase_id = c.getString(c.getColumnIndex("phase_id"));
		            dateCreated = c.getString(c.getColumnIndex("date_created"));
		            dateLastModified = c.getString(c.getColumnIndex("date_last_modified"));
		            uuidAgentCreated = c.getString(c.getColumnIndex("uuid_agent_created"));
		            uuidAgentModified = c.getString(c.getColumnIndex("uuid_agent_modified"));
		            
		            xml.append(" uuid_census=" + "\"" + uuidCensus + "\"");
		            xml.append(" uuid_hoh=" + "\"" + uuid_hoh + "\"");
		            xml.append(" phase_id=" + "\"" + phase_id + "\"");
		            xml.append(" date_created=" + "\"" + dateCreated + "\"");
		            xml.append(" date_last_modified=" + "\"" + dateLastModified + "\"");
		            xml.append(" uuid_agent_created=" + "\"" + uuidAgentCreated + "\"");
		            xml.append(" uuid_agent_modified=" + "\"" + uuidAgentModified + "\"");
		            
		            xml.append(" />");

		        }while (c.moveToNext());
		    } 
		}
		
		c.close();
		db.close();
		
		xml.append("</Censuses>");
		
		return xml.toString();
		

	}



}
