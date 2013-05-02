package edu.ucsf.telemedicine.mhealth.mordor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class RootActivity extends Activity {
	
	protected ProgressDialog pDialog = null;
	protected LocationManager locationManager = null;
	protected SharedPreferences settings = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		pDialog = new ProgressDialog(this);
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setMessage("Please wait...");
		pDialog.setIndeterminate(true);
		pDialog.setCancelable(true);
		
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		settings = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, 0);


	}
	
	@Override
	public void onPause()
	{
		locationManager.removeUpdates(onLocationChange);
		super.onPause();
	}
	  
	  
	LocationListener onLocationChange = new LocationListener()
	{
	    public void onLocationChanged(Location fix) 
	    {
			SharedPreferences settings = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("latitude", String.valueOf(fix.getLatitude()));
			editor.putString("longitude", String.valueOf(fix.getLongitude()));
			editor.commit();
			
		    String text = "Location saved: " + String.valueOf(fix.getLatitude()) + " :: " + String.valueOf(fix.getLongitude());

	    	
	      
		    locationManager.removeUpdates(onLocationChange);
		    
		    pDialog.dismiss();
			Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();

	    }
	    
	    public void onProviderDisabled(String provider) {
	      // required for interface, not used
	    	pDialog.dismiss();
	    }
	    
	    public void onProviderEnabled(String provider) {
	      // required for interface, not used
	    }
	    
	    public void onStatusChanged(String provider, int status,
	                                  Bundle extras) {
	      // required for interface, not used
	    }
	};
	
	protected void showToast(String msg)
	{
		Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();

	}
	
	protected void settingsPutKeyValue(String key, String value)
	{
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);
		editor.commit();

	}
	
	protected void initSettingsLatLong()
	{
		SharedPreferences settings = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("latitude", String.valueOf(0.0));
		editor.putString("longitude", String.valueOf(0.0));
		editor.commit();
	}
	
	public boolean isOnline()
	{
	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting())
	        return true;

	    return false;
	}
	
	public int getPhaseId()
	{
		String phase = settings.getString(Constants.SETTINGS_KEY_PHASE, "");
		if (phase.isEmpty())
			return -1;
		for (int n =  0; n < Constants.PROJECT_PHASES.length; n++)
		{
			if (Constants.PROJECT_PHASES[n].equals(phase))
				return n;
		}
		return -1;
	}
			
	protected void saveGPSLocation()
	{
		pDialog.show();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, onLocationChange);

	}
	
	protected String getDeviceSerialId()
	{
		String serial = "0000000000000000"; 
		try 
		{
		    Class<?> c = Class.forName("android.os.SystemProperties");
		    Method get = c.getMethod("get", String.class);
		    serial = (String) get.invoke(c, "ro.serialno");
		} catch (Exception ignored) {
		}
		
		return serial;
	}
	
	protected String generateUUID()
	{
		String uuid = UUID.randomUUID().toString().toUpperCase();
		return uuid;
	}

	protected void dbExecSQL(SQLiteDatabase db, String sql)
	{
		try
		{
			db.execSQL(sql);
		}
		catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	protected void dbRebuildAllDBTables()
	{
		SQLiteDatabase db = openOrCreateDatabase(Constants.DATABASE_NAME, MODE_PRIVATE, null);
		String sql = null;
		
		sql = Constants.SQL_DROP_TABLE_HEAD_OF_HOUSEHOLD;
		dbExecSQL(db, sql);
		sql = Constants.SQL_CREATE_TABLE_HEAD_OF_HOUSEHOLD;
		dbExecSQL(db, sql);
		sql = Constants.SQL_DROP_TABLE_GUARDIAN;
		dbExecSQL(db, sql);
		sql = Constants.SQL_CREATE_TABLE_GUARDIAN;
		dbExecSQL(db, sql);
		sql = Constants.SQL_DROP_TABLE_CHILD;
		dbExecSQL(db, sql);
		sql = Constants.SQL_CREATE_TABLE_CHILD;
		dbExecSQL(db, sql);
		sql = Constants.SQL_DROP_TABLE_TREATMENT;
		dbExecSQL(db, sql);
		sql = Constants.SQL_CREATE_TABLE_TREATMENT;
		dbExecSQL(db, sql);
		sql = Constants.SQL_DROP_TABLE_AGENT;
		dbExecSQL(db, sql);
		sql = Constants.SQL_CREATE_TABLE_AGENT;
		dbExecSQL(db, sql);
		sql = Constants.SQL_DROP_TABLE_CENSUS;
		dbExecSQL(db, sql);
		sql = Constants.SQL_CREATE_TABLE_CENSUS;
		dbExecSQL(db, sql);
		
		
		db.close();
	}
	
	protected void dbCreateAllDBTables()
	{
		SQLiteDatabase db = openOrCreateDatabase(Constants.DATABASE_NAME, MODE_PRIVATE, null);
		String sql = null;
		
		sql = Constants.SQL_CREATE_TABLE_HEAD_OF_HOUSEHOLD;
		dbExecSQL(db, sql);
		sql = Constants.SQL_CREATE_TABLE_GUARDIAN;
		dbExecSQL(db, sql);
		sql = Constants.SQL_CREATE_TABLE_CHILD;
		dbExecSQL(db, sql);
		sql = Constants.SQL_CREATE_TABLE_TREATMENT;
		dbExecSQL(db, sql);
		sql = Constants.SQL_CREATE_TABLE_AGENT;
		dbExecSQL(db, sql);
		sql = Constants.SQL_CREATE_TABLE_CENSUS;
		dbExecSQL(db, sql);
		
		
		db.close();
	}
	
	private void setSSLUntrustedCert()
	{
	    /*
	     *  fix for
	     *    Exception in thread "main" javax.net.ssl.SSLHandshakeException:
	     *       sun.security.validator.ValidatorException:
	     *           PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException:
	     *               unable to find valid certification path to requested target
	     */
	    TrustManager[] trustAllCerts = new TrustManager[] {
	       new X509TrustManager() {
	          public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	            return null;
	          }

	          public void checkClientTrusted(X509Certificate[] certs, String authType) {  }

	          public void checkServerTrusted(X509Certificate[] certs, String authType) {  }

	       }
	    };

	    SSLContext sc = null;
		try {
			sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch (KeyManagementException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	    

	    // Create all-trusting host name verifier
	    HostnameVerifier allHostsValid = new HostnameVerifier() {
	    	@Override
	        public boolean verify(String hostname, SSLSession session) {
	          return true;
	        }

	    };
	    // Install the all-trusting host verifier
	    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	    /*
	     * end of the fix
	     */

	}


	private void httpPost(Dictionary d)
	{
		
		setSSLUntrustedCert();
		
	    try 
	    {
	    	
	        URL url = new URL(Constants.URL_HTTP_POST);
	
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.setDoOutput(true);
	        //connection.setChunkedStreamingMode(0);
	        connection.setRequestMethod("POST");
	        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
	        //connection.setRequestProperty("Content-Type", "text/xml"); 
	        //connection.setRequestProperty("charset", "utf-8");
	        connection.setUseCaches (false);

	        String sTableName = "";
	        List<NameValuePair> params = new ArrayList<NameValuePair>();
	        
	    	for (Enumeration e = d.keys(); e.hasMoreElements();)
	    	{
	    		String key = (String) e.nextElement();
	    		String value = (String) d.get(key);
	    		params.add(new BasicNameValuePair(key, value));
	    		
	    		if (key.equals("table"))
	    			sTableName = value;
	    	}


	        OutputStream os = connection.getOutputStream();
	        BufferedWriter writer = new BufferedWriter(
	                new OutputStreamWriter(os, "UTF-8"));
	        writer.write(getQuery(params));
	        	        
	        writer.close();
	        os.close();
	        
	        //build the string to store the response text from the server
	        String response= "";

	        //start listening to the stream
	        Scanner inStream = new Scanner(connection.getInputStream());

	        //process the stream and store it in StringBuilder
	        while (inStream.hasNextLine())
	        	response += (inStream.nextLine());


	
	        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
	        {
	        	// OK
	        	Log.d("Mordor", response);
	        	if (sTableName.equals("head_of_household"))
	        	{
		        	saxParserHeadOfHousehold sax = new saxParserHeadOfHousehold(response);
		        	List<HeadOfHouseholdDataObj> list =  sax.getHeadOfHouseholdList();
		        	//Log.d("Mordor", list.toString());
		        	for (HeadOfHouseholdDataObj tmpObj : list) {
		        		updateTableHeadOfHousehold(tmpObj);
		            }
	        	}
	        	else if (sTableName.equals("guardian"))
	        	{
		        	saxParserGuardian sax = new saxParserGuardian(response);
		        	List<GuardianDataObj> list =  sax.getGuardianList();
		        	for (GuardianDataObj tmpObj : list) {
		        		updateTableGuardian(tmpObj);
		            }
	        	}
	        	else if (sTableName.equals("child"))
	        	{
		        	saxParserChild sax = new saxParserChild(response);
		        	List<ChildDataObj> list =  sax.getChildList();
		        	for (ChildDataObj tmpObj : list) {
		        		updateTableChild(tmpObj);
		            }
	        	}
	        	else if (sTableName.equals("treatment"))
	        	{
		        	saxParserTreatment sax = new saxParserTreatment(response);
		        	List<TreatmentDataObj> list =  sax.getTreatmentList();
		        	for (TreatmentDataObj tmpObj : list) {
		        		updateTableTreatment(tmpObj);
		            }
	        	}
	        	else if (sTableName.equals("agent"))
	        	{
		        	saxParserAgent sax = new saxParserAgent(response);
		        	List<AgentDataObj> list =  sax.getAgentList();
		        	for (AgentDataObj tmpObj : list) {
		        		updateTableAgent(tmpObj);
		            }
	        	}
	        	else if (sTableName.equals("census"))
	        	{
		        	saxParserCensus sax = new saxParserCensus(response);
		        	List<CensusDataObj> list =  sax.getCensusList();
		        	for (CensusDataObj tmpObj : list) {
		        		updateTableCensus(tmpObj);
		            }
	        	}
	        }
	        else
	        {
	            // Server returned HTTP error code.
	        	Log.d("Mordor", connection.getResponseMessage());
	        }
		} catch (MalformedURLException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	protected class HttpPostTask extends AsyncTask {

		@Override
		protected Object doInBackground(Object... arg0) {
			Dictionary d = (Dictionary) arg0[0];
			httpPost(d);
			return null;
		}
	}
	
	private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
	{
	    StringBuilder result = new StringBuilder();
	    boolean first = true;

	    for (NameValuePair pair : params)
	    {
	        if (first)
	            first = false;
	        else
	            result.append("&");

	        result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
	        result.append("=");
	        result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
	    }

	    return result.toString();
	}
	
	
	private void updateTableHeadOfHousehold(HeadOfHouseholdDataObj hohObj)
	{
		String sUuidHoh = hohObj.uuid_hoh;
		String sFirstName = hohObj.first_name;
		String sLastName = hohObj.last_name;
		String sAge = hohObj.age;
		String sGender = hohObj.gender;
		String sNote = hohObj.note;
		String sGps = hohObj.gps;
		String sVillage = hohObj.village;
		String sCountry = hohObj.country;
		String sDateCreated = hohObj.date_created;
		String sDateLastModified = hohObj.date_last_modified;
		String sUuidAgentCreated = hohObj.uuid_agent_created;
		String sUuidAgentModified = hohObj.uuid_agent_modified;

		int age = 0;
		if (sAge.isEmpty() || sAge.equals(""))
			age = 0;
		else
			age = Integer.parseInt(sAge);
		
		SQLiteDatabase db = openOrCreateDatabase(Constants.DATABASE_NAME, MODE_PRIVATE, null);
		String sql = null;
		
		
		sql = String.format("SELECT date_last_modified FROM head_of_household WHERE uuid_hoh = '%s' ;",
				sUuidHoh);

		
		Cursor c = db.rawQuery(sql, null);
		int results = c.getCount();
		
		boolean needUpdate = false;
		
		if (results > 0)
		{
			// A record is found, check if it's older or newer than the inserting record
			String sDateLastModifiedOnDevice = null;
			if (c != null ) 
			{
			    if  (c.moveToFirst()) 
			    {
			        do {
			        	sDateLastModifiedOnDevice = c.getString(c.getColumnIndex("date_last_modified"));
			        	break;
			        }while (c.moveToNext());
			    }
			}
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dateLastModifiedOnDevice = null;
			try {  
			    dateLastModifiedOnDevice = format.parse(sDateLastModifiedOnDevice);    
			} catch (ParseException e) {  
			    e.printStackTrace();  
			}
			Date dateLastModifieldInObj = null;
			try {  
				dateLastModifieldInObj = format.parse(sDateLastModified);    
			} catch (ParseException e) {  
			    e.printStackTrace();  
			}
			
			dateLastModifieldInObj.compareTo(dateLastModifiedOnDevice);
			dateLastModifiedOnDevice.compareTo(dateLastModifieldInObj);
			if (dateLastModifieldInObj.compareTo(dateLastModifiedOnDevice) > 0)
			{
				// Inserting record is newer than the one in DB
				// Update the DB record with new data
				needUpdate = true;
			}
			
		}
		else
		{
			// No record is found, insert a new record
			needUpdate = true;
		}
		
		if (needUpdate)
		{
			sql = String.format("DELETE FROM head_of_household WHERE uuid_hoh = '%s'; ", sUuidHoh);
			dbExecSQL(db, sql);
			
			sql = String.format("INSERT INTO head_of_household " +
					" (uuid_hoh, first_name, last_name, age, gender, note, gps, village, country, date_created, date_last_modified, uuid_agent_created, uuid_agent_modified) " +
					" VALUES " +
					"('%s', '%s', '%s', %d, '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');", 
					sUuidHoh,
					sFirstName, 
					sLastName, 
					age, 
					sGender, 
					sNote,
					sGps,
					sVillage,
					sCountry,
					sDateCreated,
					sDateLastModified,
					sUuidAgentCreated,
					sUuidAgentModified);
			dbExecSQL(db, sql);
		}
		
		db.close();

	}

	private void updateTableGuardian(GuardianDataObj guardianObj)
	{
		String sUuidGuardian = guardianObj.uuid_guardian;
		String sFirstName = guardianObj.first_name;
		String sLastName = guardianObj.last_name;
		String sAge = guardianObj.age;
		String sGender = guardianObj.gender;
		String sPregnant = guardianObj.pregnant;
		String sNote = guardianObj.note;
		String sUuidHoh = guardianObj.uuid_hoh;
		String sDateCreated = guardianObj.date_created;
		String sDateLastModified = guardianObj.date_last_modified;
		String sUuidAgentCreated = guardianObj.uuid_agent_created;
		String sUuidAgentModified = guardianObj.uuid_agent_modified;

		int age = 0;
		if (sAge.isEmpty() || sAge.equals(""))
			age = 0;
		else
			age = Integer.parseInt(sAge);

		int pregnant = 0;
		if (sPregnant.isEmpty() || sPregnant.equals(""))
			pregnant = 0;
		else
			pregnant = Integer.parseInt(sPregnant);

		SQLiteDatabase db = openOrCreateDatabase(Constants.DATABASE_NAME, MODE_PRIVATE, null);
		String sql = null;
		
		
		sql = String.format("SELECT date_last_modified FROM guardian WHERE uuid_guardian = '%s' ;",
				sUuidGuardian);

		
		Cursor c = db.rawQuery(sql, null);
		int results = c.getCount();
		
		boolean needUpdate = false;
		
		if (results > 0)
		{
			// A record is found, check if it's older or newer than the inserting record
			String sDateLastModifiedOnDevice = null;
			if (c != null ) 
			{
			    if  (c.moveToFirst()) 
			    {
			        do {
			        	sDateLastModifiedOnDevice = c.getString(c.getColumnIndex("date_last_modified"));
			        	break;
			        }while (c.moveToNext());
			    }
			}
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dateLastModifiedOnDevice = null;
			try {  
			    dateLastModifiedOnDevice = format.parse(sDateLastModifiedOnDevice);    
			} catch (ParseException e) {  
			    e.printStackTrace();  
			}
			Date dateLastModifieldInObj = null;
			try {  
				dateLastModifieldInObj = format.parse(sDateLastModified);    
			} catch (ParseException e) {  
			    e.printStackTrace();  
			}
			
			dateLastModifieldInObj.compareTo(dateLastModifiedOnDevice);
			dateLastModifiedOnDevice.compareTo(dateLastModifieldInObj);
			if (dateLastModifieldInObj.compareTo(dateLastModifiedOnDevice) > 0)
			{
				// Inserting record is newer than the one in DB
				// Update the DB record with new data
				needUpdate = true;
			}
			
		}
		else
		{
			// No record is found, insert a new record
			needUpdate = true;
		}
		
		if (needUpdate)
		{
			sql = String.format("DELETE FROM guardian WHERE uuid_guardian = '%s'; ", sUuidGuardian);
			dbExecSQL(db, sql);
			
			sql = String.format("INSERT INTO guardian " +
					" (uuid_guardian, first_name, last_name, age, gender, pregnant, note, uuid_hoh, date_created, date_last_modified, uuid_agent_created, uuid_agent_modified) " +
					" VALUES " +
					"('%s', '%s', '%s', %d, '%s', %d, '%s', '%s', '%s', '%s', '%s', '%s');", 
					sUuidGuardian,
					sFirstName, 
					sLastName, 
					age, 
					sGender,
					pregnant,
					sNote,
					sUuidHoh,
					sDateCreated,
					sDateLastModified,
					sUuidAgentCreated,
					sUuidAgentModified);
			dbExecSQL(db, sql);
		}
		
		db.close();

	}

	private void updateTableChild(ChildDataObj childObj)
	{
		String sUuidChild = childObj.uuid_child;
		String sFirstName = childObj.first_name;
		String sLastName = childObj.last_name;
		String sNickname = childObj.nickname;
		String sAge = childObj.age;
		String sAgeUnit = childObj.age_unit;
		String sGender = childObj.gender;
		String sStatus = childObj.status;
		String sNote = childObj.note;
		String sUuidHoh = childObj.uuid_hoh;
		String sUuidGuardian = childObj.uuid_guardian;
		String sDateCreated = childObj.date_created;
		String sDateLastModified = childObj.date_last_modified;
		String sUuidAgentCreated = childObj.uuid_agent_created;
		String sUuidAgentModified = childObj.uuid_agent_modified;

		int age = 0;
		if (sAge.isEmpty() || sAge.equals(""))
			age = 0;
		else
			age = Integer.parseInt(sAge);

		SQLiteDatabase db = openOrCreateDatabase(Constants.DATABASE_NAME, MODE_PRIVATE, null);
		String sql = null;
		
		
		sql = String.format("SELECT date_last_modified FROM child WHERE uuid_child = '%s' ;",
				sUuidChild);

		
		Cursor c = db.rawQuery(sql, null);
		int results = c.getCount();
		
		boolean needUpdate = false;
		
		if (results > 0)
		{
			// A record is found, check if it's older or newer than the inserting record
			String sDateLastModifiedOnDevice = null;
			if (c != null ) 
			{
			    if  (c.moveToFirst()) 
			    {
			        do {
			        	sDateLastModifiedOnDevice = c.getString(c.getColumnIndex("date_last_modified"));
			        	break;
			        }while (c.moveToNext());
			    }
			}
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dateLastModifiedOnDevice = null;
			try {  
			    dateLastModifiedOnDevice = format.parse(sDateLastModifiedOnDevice);    
			} catch (ParseException e) {  
			    e.printStackTrace();  
			}
			Date dateLastModifieldInObj = null;
			try {  
				dateLastModifieldInObj = format.parse(sDateLastModified);    
			} catch (ParseException e) {  
			    e.printStackTrace();  
			}
			
			dateLastModifieldInObj.compareTo(dateLastModifiedOnDevice);
			dateLastModifiedOnDevice.compareTo(dateLastModifieldInObj);
			if (dateLastModifieldInObj.compareTo(dateLastModifiedOnDevice) > 0)
			{
				// Inserting record is newer than the one in DB
				// Update the DB record with new data
				needUpdate = true;
			}
			
		}
		else
		{
			// No record is found, insert a new record
			needUpdate = true;
		}
		
		if (needUpdate)
		{
			sql = String.format("DELETE FROM child WHERE uuid_child = '%s'; ", sUuidChild);
			dbExecSQL(db, sql);
			
			sql = String.format("INSERT INTO child " +
					" (uuid_child, first_name, last_name, nickname, age, age_unit, gender, status, note, uuid_hoh, uuid_guardian, date_created, date_last_modified, uuid_agent_created, uuid_agent_modified) " +
					" VALUES " +
					"('%s', '%s', '%s', '%s', %d, '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');", 
					sUuidChild,
					sFirstName, 
					sLastName,
					sNickname,
					age,
					sAgeUnit,
					sGender,
					sStatus,
					sNote,
					sUuidHoh,
					sUuidGuardian,
					sDateCreated,
					sDateLastModified,
					sUuidAgentCreated,
					sUuidAgentModified);
			dbExecSQL(db, sql);
		}
		
		db.close();

	}
	
	private void updateTableTreatment(TreatmentDataObj treatmentObj)
	{
		String sUuidTreatment = treatmentObj.uuid_treatment;
		String sObtainedConsent = treatmentObj.obtained_consent;
		String sStatus = treatmentObj.status;
		String sDose = treatmentObj.dose;
		String sNote = treatmentObj.note;
		String sUuidChild = treatmentObj.uuid_child;
		String sDateCreated = treatmentObj.date_created;
		String sDateLastModified = treatmentObj.date_last_modified;
		String sUuidAgentCreated = treatmentObj.uuid_agent_created;
		String sUuidAgentModified = treatmentObj.uuid_agent_modified;

		int obtained_consent = 0;
		if (sObtainedConsent.isEmpty() || sObtainedConsent.equals(""))
			obtained_consent = 0;
		else
			obtained_consent = Integer.parseInt(sObtainedConsent);

		int dose = 0;
		if (sDose.isEmpty() || sDose.equals(""))
			dose = 0;
		else
			dose = Integer.parseInt(sDose);

		SQLiteDatabase db = openOrCreateDatabase(Constants.DATABASE_NAME, MODE_PRIVATE, null);
		String sql = null;
		
		
		sql = String.format("SELECT date_last_modified FROM treatment WHERE uuid_treatment = '%s' ;",
				sUuidTreatment);

		
		Cursor c = db.rawQuery(sql, null);
		int results = c.getCount();
		
		boolean needUpdate = false;
		
		if (results > 0)
		{
			// A record is found, check if it's older or newer than the inserting record
			String sDateLastModifiedOnDevice = null;
			if (c != null ) 
			{
			    if  (c.moveToFirst()) 
			    {
			        do {
			        	sDateLastModifiedOnDevice = c.getString(c.getColumnIndex("date_last_modified"));
			        	break;
			        }while (c.moveToNext());
			    }
			}
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dateLastModifiedOnDevice = null;
			try {  
			    dateLastModifiedOnDevice = format.parse(sDateLastModifiedOnDevice);    
			} catch (ParseException e) {  
			    e.printStackTrace();  
			}
			Date dateLastModifieldInObj = null;
			try {  
				dateLastModifieldInObj = format.parse(sDateLastModified);    
			} catch (ParseException e) {  
			    e.printStackTrace();  
			}
			
			dateLastModifieldInObj.compareTo(dateLastModifiedOnDevice);
			dateLastModifiedOnDevice.compareTo(dateLastModifieldInObj);
			if (dateLastModifieldInObj.compareTo(dateLastModifiedOnDevice) > 0)
			{
				// Inserting record is newer than the one in DB
				// Update the DB record with new data
				needUpdate = true;
			}
			
		}
		else
		{
			// No record is found, insert a new record
			needUpdate = true;
		}
		
		if (needUpdate)
		{
			sql = String.format("DELETE FROM treatment WHERE uuid_treatment = '%s'; ", sUuidTreatment);
			dbExecSQL(db, sql);
			
			sql = String.format("INSERT INTO treatment " +
					" (uuid_treatment, obtained_consent, status, dose, note, uuid_child, date_created, date_last_modified, uuid_agent_created, uuid_agent_modified) " +
					" VALUES " +
					"('%s', %d, '%s', %d, '%s', '%s', '%s', '%s', '%s', '%s');", 
					sUuidTreatment,
					obtained_consent, 
					sStatus, 
					dose, 
					sNote,
					sUuidChild,
					sDateCreated,
					sDateLastModified,
					sUuidAgentCreated,
					sUuidAgentModified);
			dbExecSQL(db, sql);
		}
		
		db.close();

	}

	private void updateTableAgent(AgentDataObj agentObj)
	{
		String sUuidAgent = agentObj.uuid_agent;
		String sFirstName = agentObj.first_name;
		String sLastName = agentObj.last_name;
		String sEmail = agentObj.email;
		String sMobile_1 = agentObj.mobile_1;
		String sMobile_2 = agentObj.mobile_2;
		String sPhoto = agentObj.photo;
		String sDateCreated = agentObj.date_created;
		String sDateLastModified = agentObj.date_last_modified;


		SQLiteDatabase db = openOrCreateDatabase(Constants.DATABASE_NAME, MODE_PRIVATE, null);
		String sql = null;
		
		
		sql = String.format("SELECT date_last_modified FROM agent WHERE uuid_agent = '%s' ;",
				sUuidAgent);

		
		Cursor c = db.rawQuery(sql, null);
		int results = c.getCount();
		
		boolean needUpdate = false;
		
		if (results > 0)
		{
			// A record is found, check if it's older or newer than the inserting record
			String sDateLastModifiedOnDevice = null;
			if (c != null ) 
			{
			    if  (c.moveToFirst()) 
			    {
			        do {
			        	sDateLastModifiedOnDevice = c.getString(c.getColumnIndex("date_last_modified"));
			        	break;
			        }while (c.moveToNext());
			    }
			}
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dateLastModifiedOnDevice = null;
			try {  
			    dateLastModifiedOnDevice = format.parse(sDateLastModifiedOnDevice);    
			} catch (ParseException e) {  
			    e.printStackTrace();  
			}
			Date dateLastModifieldInObj = null;
			try {  
				dateLastModifieldInObj = format.parse(sDateLastModified);    
			} catch (ParseException e) {  
			    e.printStackTrace();  
			}
			
			dateLastModifieldInObj.compareTo(dateLastModifiedOnDevice);
			dateLastModifiedOnDevice.compareTo(dateLastModifieldInObj);
			if (dateLastModifieldInObj.compareTo(dateLastModifiedOnDevice) > 0)
			{
				// Inserting record is newer than the one in DB
				// Update the DB record with new data
				needUpdate = true;
			}
			
		}
		else
		{
			// No record is found, insert a new record
			needUpdate = true;
		}
		
		if (needUpdate)
		{
			sql = String.format("DELETE FROM agent WHERE uuid_agent = '%s'; ", sUuidAgent);
			dbExecSQL(db, sql);
			
			sql = String.format("INSERT INTO agent " +
					" (uuid_agent, first_name, last_name, email, mobile_1, mobile_2, photo, date_created, date_last_modified) " +
					" VALUES " +
					"('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');", 
					sUuidAgent,
					sFirstName, 
					sLastName, 
					sEmail, 
					sMobile_1,
					sMobile_2,
					sPhoto,
					sDateCreated,
					sDateLastModified);
			dbExecSQL(db, sql);
		}
		
		db.close();

	}

	private void updateTableCensus(CensusDataObj censusObj)
	{
		String sUuidCensus = censusObj.uuid_census;
		String sUuidHoh = censusObj.uuid_hoh;
		String sPhaseId = censusObj.phase_id;
		String sDateCreated = censusObj.date_created;
		String sDateLastModified = censusObj.date_last_modified;
		String sUuidAgentCreated = censusObj.uuid_agent_created;
		String sUuidAgentModified = censusObj.uuid_agent_modified;

		int phase_id = 0;
		if (sPhaseId.isEmpty() || sPhaseId.equals(""))
			phase_id = 0;
		else
			phase_id = Integer.parseInt(sPhaseId);

		SQLiteDatabase db = openOrCreateDatabase(Constants.DATABASE_NAME, MODE_PRIVATE, null);
		String sql = null;
		
		
		sql = String.format("SELECT date_last_modified FROM census WHERE uuid_census = '%s' ;",
				sUuidCensus);

		
		Cursor c = db.rawQuery(sql, null);
		int results = c.getCount();
		
		boolean needUpdate = false;
		
		if (results > 0)
		{
			// A record is found, check if it's older or newer than the inserting record
			String sDateLastModifiedOnDevice = null;
			if (c != null ) 
			{
			    if  (c.moveToFirst()) 
			    {
			        do {
			        	sDateLastModifiedOnDevice = c.getString(c.getColumnIndex("date_last_modified"));
			        	break;
			        }while (c.moveToNext());
			    }
			}
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dateLastModifiedOnDevice = null;
			try {  
			    dateLastModifiedOnDevice = format.parse(sDateLastModifiedOnDevice);    
			} catch (ParseException e) {  
			    e.printStackTrace();  
			}
			Date dateLastModifieldInObj = null;
			try {  
				dateLastModifieldInObj = format.parse(sDateLastModified);    
			} catch (ParseException e) {  
			    e.printStackTrace();  
			}
			
			dateLastModifieldInObj.compareTo(dateLastModifiedOnDevice);
			dateLastModifiedOnDevice.compareTo(dateLastModifieldInObj);
			if (dateLastModifieldInObj.compareTo(dateLastModifiedOnDevice) > 0)
			{
				// Inserting record is newer than the one in DB
				// Update the DB record with new data
				needUpdate = true;
			}
			
		}
		else
		{
			// No record is found, insert a new record
			needUpdate = true;
		}
		
		if (needUpdate)
		{
			sql = String.format("DELETE FROM census WHERE uuid_census = '%s'; ", sUuidCensus);
			dbExecSQL(db, sql);
			
			sql = String.format("INSERT INTO census " +
					" (uuid_census, uuid_hoh, phase_id, date_created, date_last_modified, uuid_agent_created, uuid_agent_modified) " +
					" VALUES " +
					"('%s', '%s', %d, '%s', '%s', '%s', '%s');", 
					sUuidCensus,
					sUuidHoh,
					phase_id,
					sDateCreated,
					sDateLastModified,
					sUuidAgentCreated,
					sUuidAgentModified);
			dbExecSQL(db, sql);
		}
		
		db.close();

	}


}


