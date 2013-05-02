package edu.ucsf.telemedicine.mhealth.mordor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends RootActivity implements OnClickListener {
	private EditText editTextUsername, editTextPassword;
	private CheckBox checkBoxRememberMe;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.setTitle(R.string.title_login);
		setContentView(R.layout.login);
		
		dbCreateAllDBTables();
		
		Button bttnLogin = (Button) findViewById(R.id.buttonLogin);
		bttnLogin.setOnClickListener(this);
		
		SharedPreferences settings = this.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, 0);
		
		editTextUsername = (EditText) findViewById(R.id.editTextUsername);
		editTextPassword = (EditText) findViewById(R.id.editTextPassword);
		checkBoxRememberMe = (CheckBox) findViewById(R.id.checkBoxRememberMe);
		
		checkBoxRememberMe.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				SharedPreferences settings = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				if (isChecked)
					editor.putBoolean("remember_me", true);
				else
					editor.putBoolean("remember_me", false);
				editor.commit();
			}
		});
		
		editTextUsername.setText(settings.getString("username", ""));
		editTextPassword.setText(settings.getString("password", ""));
		checkBoxRememberMe.setChecked(settings.getBoolean("remember_me", false));
		
		

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.buttonLogin)
		{
			String username = editTextUsername.getText().toString();
			String password = editTextPassword.getText().toString();
			
			if (username.equals("ucsf") && password.equals("password"))
			{
				startActivity(new Intent(Login.this, LocationDetails.class));
			}
			else 
			{
				Toast toast = Toast.makeText(getApplicationContext(), "Incorrect username or password!", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();

			}
			
			SharedPreferences settings = this.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, 0);
			if (settings.getBoolean("remember_me", false))
			{
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("username", username);
				editor.putString("password", password);
				editor.commit();
			}
		}
			
		
	}

}
