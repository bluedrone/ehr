package com.wdeanmedical.ehr.dto;

import java.util.Date;

import com.wdeanmedical.ehr.entity.Patient;

public class MessageDTO extends AuthorizedDTO {
  private int id;
  private int patientId;
  private Patient patient;
  private String subject;
  private String content;
  private Date date;
  private int clinicianId;
  private boolean fromClinician;
  private int patientMessageTypeId;
  private String wouldSee;
  private String preferredTimes;
  private String apptFrom;
  private String apptTo;
  private String visitReason;
  

  public MessageDTO() {
  }

  public int getId() { return id; }
  public void setId(int id) { this.id = id; }

  public int getPatientId() { return patientId; }
  public void setPatientId(int patientId) { this.patientId = patientId; }

  public String getSubject() { return subject; }
  public void setSubject(String subject) { this.subject = subject; }

  public String getContent() { return content; }
  public void setContent(String content) { this.content = content; }

  public Date getDate() { return date; }
  public void setDate(Date date) { this.date = date; }

  public int getClinicianId() { return clinicianId; }
  public void setClinicianId(int clinicianId) { this.clinicianId = clinicianId; }

  public boolean isFromClinician() { return fromClinician; }
  public void setFromClinician(boolean fromClinician) { this.fromClinician = fromClinician; }

  public int getPatientMessageTypeId() { return patientMessageTypeId; }
  public void setPatientMessageTypeId(int patientMessageTypeId) { this.patientMessageTypeId = patientMessageTypeId; }

  public String getWouldSee() { return wouldSee; }
  public void setWouldSee(String wouldSee) { this.wouldSee = wouldSee; }

  public String getPreferredTimes() { return preferredTimes; }
  public void setPreferredTimes(String preferredTimes) { this.preferredTimes = preferredTimes; }

  public String getApptFrom() { return apptFrom; }
  public void setApptFrom(String apptFrom) { this.apptFrom = apptFrom; }

  public String getApptTo() { return apptTo; }
  public void setApptTo(String apptTo) { this.apptTo = apptTo; }

  public String getVisitReason() { return visitReason; }
  public void setVisitReason(String visitReason) { this.visitReason = visitReason; }
  
  public Patient getPatient() { return patient; }
  public void setPatient(Patient patient) { this.patient = patient; }

}
