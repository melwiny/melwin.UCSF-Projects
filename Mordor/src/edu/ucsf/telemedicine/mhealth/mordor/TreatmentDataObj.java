package edu.ucsf.telemedicine.mhealth.mordor;

public class TreatmentDataObj {
	
	String uuid_treatment;
	String obtained_consent;
	String status;
	String dose;
	String note;
	String uuid_child;
	String date_created;
	String date_last_modified;
	String uuid_agent_created;
	String uuid_agent_modified;
	public String getUuid_treatment() {
		return uuid_treatment;
	}
	public String getObtained_consent() {
		return obtained_consent;
	}
	public String getStatus() {
		return status;
	}
	public String getDose() {
		return dose;
	}
	public String getNote() {
		return note;
	}
	public String getUuid_child() {
		return uuid_child;
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
	public void setUuid_treatment(String uuid_treatment) {
		this.uuid_treatment = uuid_treatment;
	}
	public void setObtained_consent(String obtained_consent) {
		this.obtained_consent = obtained_consent;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setDose(String dose) {
		this.dose = dose;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public void setUuid_child(String uuid_child) {
		this.uuid_child = uuid_child;
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
