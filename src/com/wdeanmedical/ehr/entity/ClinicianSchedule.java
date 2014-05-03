package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "clinician_schedule")
public class ClinicianSchedule extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -3809829904313485829L;
  
  private Patient patient;
  private Clinician clinician;
  private Date date;
  private String length;
  private float age;
  private Gender gender;
  private String reason;
  private String comments;
  private String status;
  private String patientLocation;
  private String room;
  private boolean checkedIn;
  private float waitTime;
  private String progressNoteStatus;
  

  public ClinicianSchedule() {
  }
  
  @JoinColumn(name = "patient", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public Patient getPatient() { return patient; }
  public void setPatient(Patient patient) { this.patient = patient; }
  
  @Column(name = "date")
  public Date getDate() { return date; }
  public void setDate(Date date) { this.date = date; }

  @JoinColumn(name = "clinician", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public Clinician getClinician() { return clinician; }
  public void setClinician(Clinician clinician) { this.clinician = clinician; }

  @Column(name = "length")
  public String getLength() { return length; }
  public void setLength(String length) { this.length = length; }

  @Column(name = "age")
  public float getAge() { return age; }
  public void setAge(float age) { this.age = age; }
  
  @JoinColumn(name = "gender", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public Gender getGender() { return gender; }
  public void setGender(Gender gender) { this.gender = gender; }

  @Column(name = "reason")
  public String getReason() { return reason; }
  public void setReason(String reason) { this.reason = reason; }

  @Column(name = "comments")
  public String getComments() { return comments; }
  public void setComments(String comments) { this.comments = comments; }

  @Column(name = "status")
  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }

  @Column(name = "patient_location")
  public String getPatientLocation() { return patientLocation; }
  public void setPatientLocation(String patientLocation) { this.patientLocation = patientLocation; }

  @Column(name = "room")
  public String getRoom() { return room; }
  public void setRoom(String room) { this.room = room; }

  @Column(name = "checked_in")
  public boolean isCheckedIn() { return checkedIn; }
  public void setCheckedIn(boolean checkedIn) { this.checkedIn = checkedIn; }

  @Column(name = "wait_time")
  public float getWaitTime() { return waitTime; }
  public void setWaitTime(float waitTime) { this.waitTime = waitTime; }

  @Column(name = "progress_note_status")
  public String getProgressNoteStatus() { return progressNoteStatus; }
  public void setProgressNoteStatus(String progressNoteStatus) { this.progressNoteStatus = progressNoteStatus; }

}
