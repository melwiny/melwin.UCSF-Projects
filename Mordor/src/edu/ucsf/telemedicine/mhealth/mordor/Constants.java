package edu.ucsf.telemedicine.mhealth.mordor;

final public class Constants {
	//private constructor to prevent instantiation/inheritance
	private Constants() { }

    public static final String INTENT_EXTRA_HEAD_OF_HOUSEHOLD_NAME = "HeadOfHouseholdName";
    public static final String INTENT_EXTRA_GUARDIAN_NAME = "GuardianName";
    public static final String INTENT_EXTRA_HEAD_OF_HOUSEHOLD_FIRST_NAME = "HeadOfHouseholdFirstName";
    public static final String INTENT_EXTRA_HEAD_OF_HOUSEHOLD_LAST_NAME = "HeadOfHouseholdLastName";
    public static final String INTENT_EXTRA_HEAD_OF_HOUSEHOLD_UUID = "HeadOfHouseholdUuid";
    public static final String INTENT_EXTRA_GUARDIAN_FIRST_NAME = "GuardianFirstName";
    public static final String INTENT_EXTRA_GUARDIAN_LAST_NAME = "GuardianLastName";
    public static final String INTENT_EXTRA_GUARDIAN_UUID = "GuardianUuid";
    public static final String INTENT_EXTRA_CHILD_FIRST_NAME = "ChildFirstName";
    public static final String INTENT_EXTRA_CHILD_LAST_NAME = "ChildLastName";
    public static final String INTENT_EXTRA_CHILD_UUID = "ChildUuid";
    public static final String INTENT_EXTRA_LOCATION = "Location";
    public static final String INTENT_EXTRA_TREATMENT_UUID = "TreatmentUuid";
    public static final String INTENT_EXTRA_ACTIVITY_CALLER_ID = "ActivityCallerId";
    public static final String DATABASE_NAME = "mordor";
    public static final String SHARED_PREFERENCES_NAME = "edu.ucsf.telemedicine.mhealth.mordor.settings";
    public static final String SETTINGS_KEY_UUID_AGENT = "uuid_agent";
    public static final String SETTINGS_KEY_VILLAGE = "village";
    public static final String SETTINGS_KEY_COUNTRY = "country";
    public static final String SETTINGS_KEY_PHASE = "phase";
    public static final String SETTINGS_KEY_LATITUDE = "latitude";
    public static final String SETTINGS_KEY_LONGITUDE = "longitude";
    public static final String SETTINGS_KEY_DATETIME_LAST_SYN = "datetime_last_syn";
    public static final String SETTINGS_KEY_HOH_UUID_ON_TREATMENT = "hoh_uuid_on_treatment";
    
    
    public static final String URL_WEB_SERVER = "https://54.235.205.247/mordor/";
    public static final String URL_HTTP_POST = URL_WEB_SERVER + "http_post.php";
    public static final String URL_HTTP_GET = URL_WEB_SERVER + "http_get.php";
    
    public static final String ACTIVITY_CALLER_ID_HOUSEHOLD_REVIEW = "id_household_review";
    
    
    public static final String PROJECT_VILLAGES[] = {
    	// Niger
					"Koba Koira",
					"Koubangou Samsou",
					"Kourou Kaina",
					"Deytagui Beri",
					"Gardi Banda",
					"Bellandé peulh",
					"Bangario Moussa",
					"Yanfaré",
					"Fouhinza",
					"Djibo Dey",
		// Tanzanila
					"Chanzuru",
					"Kondoa",
					"Malai"
				};
    public static final String PROJECT_VILLAGES_COUNTRY[][] = {
			    	{"Koba Koira", "Niger"},
			    	{"Koubangou Samsou", "Niger"},
			    	{"Kourou Kaina", "Niger"},
			    	{"Deytagui Beri", "Niger"},
			    	{"Gardi Banda", "Niger"},
			    	{"Bellandé peulh", "Niger"},
			    	{"Bangario Moussa", "Niger"},
			    	{"Yanfaré", "Niger"},
			    	{"Fouhinza", "Niger"},
			    	{"Djibo Dey", "Niger"},
			    	{"Chanzuru", "Tanzanila"},
			    	{"Kondoa", "Tanzanila"},
			    	{"Malai", "Tanzanila"}
			    };
    
    public static final int INDEX_VILLAGE = 0;
    public static final int INDEX_COUNTRY = 1;
    public static String findCountryOfVillage(String village)
    {
    	String country = "";
    	for (int n = 0; n < PROJECT_VILLAGES.length; n++)
    	{
    		if (PROJECT_VILLAGES_COUNTRY[n][INDEX_VILLAGE].equals(village))
    		{
    			country = PROJECT_VILLAGES_COUNTRY[n][INDEX_COUNTRY];
    			break;
    		}
    			
    	}
    	return country;
    }
    public static String[] getVillages()
    {
    	String[] villages = new String[PROJECT_VILLAGES_COUNTRY.length];
    	for (int n = 0; n < villages.length; n++)
    		villages[n] = PROJECT_VILLAGES_COUNTRY[n][INDEX_VILLAGE];
    	
    	return villages;
    }
    
    public static final String PROJECT_PHASES[] = {
					"MORDOR 0 Census"
//					"MORDOR 6 Census",
//					"MORDOR 12 Census",
//					"MORDOR 18 Census",
//					"MORDOR 24 Census",
//					"MORDOR 0 Exam",
//					"MORDOR 12 Exam",
//					"MORDOR 14 Exam",
//					"MORDOR 24 Exam",
//					"MORDOR 0 Treatment",
//					"MORDOR 6 Treatment",
//					"MORDOR 12 Treatment",
//					"MORDOR 18 Treatment",
//					"MORDOR 24 Treatment"
				};

    
    public static final String SQL_CREATE_TABLE_HEAD_OF_HOUSEHOLD = "CREATE TABLE IF NOT EXISTS head_of_household"
		+	"("
		+	"uuid_hoh				VARCHAR(50) PRIMARY KEY,"
		+	"first_name				VARCHAR(50),"
		+	"last_name				VARCHAR(50),"
		+	"age					INT,"
		+	"gender					VARCHAR(10),"
		+	"note 					VARCHAR(255),"
		+	"gps					VARCHAR(100),"
		+	"village				VARCHAR(50),"
		+	"country				VARCHAR(50),"
		+	"date_created 			TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
		+	"date_last_modified 	TIMESTAMP,"
		+	"uuid_agent_created		VARCHAR(50),"
		+	"uuid_agent_modified	VARCHAR(50)"
		+	");";
    public static final String SQL_DROP_TABLE_HEAD_OF_HOUSEHOLD = "DROP TABLE IF EXISTS head_of_household";
    
    public static final String SQL_CREATE_TABLE_GUARDIAN = "CREATE TABLE IF NOT EXISTS guardian"
    	+	"("
    	+	"uuid_guardian			VARCHAR(50) PRIMARY KEY,"
    	+	"first_name				VARCHAR(50),"
    	+	"last_name				VARCHAR(50),"
    	+	"age					INT,"
    	+	"gender					VARCHAR(10),"
    	+	"pregnant				TINYINT,"
    	+	"note 					VARCHAR(255),"
    	+	"uuid_hoh				VARCHAR(50),"
    	+	"date_created 			TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
    	+	"date_last_modified 	TIMESTAMP,"
		+	"uuid_agent_created		VARCHAR(50),"
		+	"uuid_agent_modified	VARCHAR(50)"
    	+	");";
    public static final String SQL_DROP_TABLE_GUARDIAN = "DROP TABLE IF EXISTS guardian";

    public static final String SQL_CREATE_TABLE_CHILD = "CREATE TABLE IF NOT EXISTS child"
        +	"("
        +	"uuid_child				VARCHAR(50) PRIMARY KEY,"
        +	"first_name				VARCHAR(50),"
		+	"last_name				VARCHAR(50),"
		+	"nickname				VARCHAR(50),"
		+	"age					INT,"
		+	"age_unit				VARCHAR(10),"
		+	"gender					VARCHAR(16),"
		+	"status					VARCHAR(25),"
		+	"note 					VARCHAR(255),"
		+	"uuid_hoh				VARCHAR(50),"
		+	"uuid_guardian			VARCHAR(50),"
		+	"date_created 			TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
		+	"date_last_modified 	TIMESTAMP,"
		+	"uuid_agent_created		VARCHAR(50),"
		+	"uuid_agent_modified	VARCHAR(50)"
        +	");";
    public static final String SQL_DROP_TABLE_CHILD = "DROP TABLE IF EXISTS child";

    public static final String SQL_CREATE_TABLE_TREATMENT = "CREATE TABLE IF NOT EXISTS treatment"
    	+	"("
    	+	"uuid_treatment			VARCHAR(50) PRIMARY KEY,"
    	+	"obtained_consent		TINYINT,"
		+	"status					VARCHAR(25),"
		+	"dose					INT,"
		+	"note 					VARCHAR(255),"
		+	"uuid_child				VARCHAR(50),"
		+	"date_created 			TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
		+	"date_last_modified 	TIMESTAMP,"
		+	"uuid_agent_created		VARCHAR(50),"
		+	"uuid_agent_modified	VARCHAR(50)"
		+	");";
    public static final String SQL_DROP_TABLE_TREATMENT = "DROP TABLE IF EXISTS treatment";

    public static final String SQL_CREATE_TABLE_AGENT = "CREATE TABLE IF NOT EXISTS agent"
        +	"("
		+	"uuid_agent			VARCHAR(50) PRIMARY KEY,"
		+	"first_name			VARCHAR(50),"
		+	"last_name			VARCHAR(50),"
		+	"email				VARCHAR(50),"
		+	"mobile_1 			VARCHAR(50),"
		+	"mobile_2			VARCHAR(50),"
		+	"photo				BLOB,"
    	+	"date_created 		TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
    	+	"date_last_modified TIMESTAMP"
    	+	");";
    public static final String SQL_DROP_TABLE_AGENT = "DROP TABLE IF EXISTS agent";
    
    public static final String SQL_CREATE_TABLE_CENSUS = "CREATE TABLE IF NOT EXISTS census"
            +	"("
            +	"uuid_census		VARCHAR(50) PRIMARY KEY,"
            +	"uuid_hoh			VARCHAR(50),"
			+	"phase_id			INT,"
			+	"date_created 		TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
			+	"date_last_modified TIMESTAMP,"
			+	"uuid_agent_created		VARCHAR(50),"
			+	"uuid_agent_modified	VARCHAR(50)"
        	+	");";
    public static final String SQL_DROP_TABLE_CENSUS = "DROP TABLE IF EXISTS census";
    

}
