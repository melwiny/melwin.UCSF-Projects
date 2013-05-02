package edu.ucsf.telemedicine.mhealth.mordor;

public class CensusDataObj {
	String uuid_census;
	String uuid_hoh;
	String phase_id;
	String date_created;
	String date_last_modified;
	String uuid_agent_created;
	String uuid_agent_modified;
	public String getUuid_census() {
		return uuid_census;
	}
	public String getUuid_hoh() {
		return uuid_hoh;
	}
	public String getPhase_id() {
		return phase_id;
	}
	public String getDate_created() {
		return date_created;
	}
	public String getDate_last_modified() {
		return date_last_modified;
	}
	public String getUuid_agent_created() {
		return uuid_agent_created;
	}
	public String getUuid_agent_modified() {
		return uuid_agent_modified;
	}
	public void setUuid_census(String uuid_census) {
		this.uuid_census = uuid_census;
	}
	public void setUuid_hoh(String uuid_hoh) {
		this.uuid_hoh = uuid_hoh;
	}
	public void setPhase_id(String phase_id) {
		this.phase_id = phase_id;
	}
	public void setDate_created(String date_created) {
		this.date_created = date_created;
	}
	public void setDate_last_modified(String date_last_modified) {
		this.date_last_modified = date_last_modified;
	}
	public void setUuid_agent_created(String uuid_agent_created) {
		this.uuid_agent_created = uuid_agent_created;
	}
	public void setUuid_agent_modified(String uuid_agent_modified) {
		this.uuid_agent_modified = uuid_agent_modified;
	}

}
