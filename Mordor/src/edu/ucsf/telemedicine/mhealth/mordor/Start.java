package edu.ucsf.telemedicine.mhealth.mordor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Start extends RootActivity implements OnClickListener {
	private String sHeadOfHouseholdFirstName = null, sHeadOfHouseholdLastName = null, sHeadOfHouseholdUuid = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.setTitle(R.string.title_start);
		setContentView(R.layout.start);
		
	    if (getIntent().getExtras() != null) 
	    {
	    	sHeadOfHouseholdFirstName = getIntent().getExtras().getString(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_FIRST_NAME);
	    	sHeadOfHouseholdLastName = getIntent().getExtras().getString(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_LAST_NAME);
	    	sHeadOfHouseholdUuid = getIntent().getExtras().getString(Constants.INTENT_EXTRA_HEAD_OF_HOUSEHOLD_UUID);
	    }

				
		Button bttnAddHeadOfHousehold = (Button) findViewById(R.id.buttonAddHeadOfHousehold);
		bttnAddHeadOfHousehold.setOnClickListener(this);
		
		Button bttnEditHeadOfHousehold = (Button) findViewById(R.id.buttonEditHeadOfHousehold);
		bttnEditHeadOfHousehold.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.buttonAddHeadOfHousehold)
		{
			startActivity(new Intent(this,  HeadOfHouseholdDetails.class));
		}
		else if (v.getId() == R.id.buttonEditHeadOfHousehold)
		{
            Intent intent = new Intent(this, HeadOfHousehold.class);
            startActivity(intent);

		}
			
		
	}

}

