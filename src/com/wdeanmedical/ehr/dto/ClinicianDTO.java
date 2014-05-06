package com.wdeanmedical.ehr.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.wdeanmedical.ehr.entity.Appointment;
import com.wdeanmedical.ehr.entity.Clinician;
import com.wdeanmedical.ehr.entity.PatientMessage;

public class ClinicianDTO extends AuthorizedDTO {
  private int id;
  private List<Clinician> clinicians;
  private List<PatientMessage> patientMessages;
  private List<Appointment> appointments;
  public Map<String,List> dashboard = new HashMap<String,List>();
  public Map<String,Set> patientSearchTypeAheads = new HashMap<String,Set>();

  public ClinicianDTO() {
  }

  public int getId() { return id; }
  public void setId(int id) { this.id = id; }
  
  public List<Clinician> getClinicians() { return clinicians; }
  public void setClinicians(List<Clinician> clinicians) { this.clinicians = clinicians; }

  public List<PatientMessage> getPatientMessages() { return patientMessages; }
  public void setPatientMessages(List<PatientMessage> patientMessages) { this.patientMessages = patientMessages; }
  
  public List<Appointment> getAppointments() { return appointments; }
  public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }
  
}
