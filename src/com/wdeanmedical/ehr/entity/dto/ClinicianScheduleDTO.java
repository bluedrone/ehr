/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

package com.wdeanmedical.ehr.entity.dto;

import java.util.Date;

public class ClinicianScheduleDTO {

  private PatientDTO patient;
  private ClinicianDTO clinician;
  private Date date;
  private String length;
  private float age;
  private GenderDTO gender;
  private String reason;
  private String comments;
  private String status;
  private String patientLocation;
  private String room;
  private boolean checkedIn;
  private float waitTime;
  private String progressNoteStatus;

  public ClinicianScheduleDTO() {
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getLength() {
    return length;
  }

  public void setLength(String length) {
    this.length = length;
  }

  public float getAge() {
    return age;
  }

  public void setAge(float age) {
    this.age = age;
  }

  public GenderDTO getGender() {
    return gender;
  }

  public void setGender(GenderDTO gender) {
    this.gender = gender;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getPatientLocation() {
    return patientLocation;
  }

  public void setPatientLocation(String patientLocation) {
    this.patientLocation = patientLocation;
  }

  public String getRoom() {
    return room;
  }

  public void setRoom(String room) {
    this.room = room;
  }

  public boolean isCheckedIn() {
    return checkedIn;
  }

  public void setCheckedIn(boolean checkedIn) {
    this.checkedIn = checkedIn;
  }

  public float getWaitTime() {
    return waitTime;
  }

  public void setWaitTime(float waitTime) {
    this.waitTime = waitTime;
  }

  public String getProgressNoteStatus() {
    return progressNoteStatus;
  }

  public void setProgressNoteStatus(String progressNoteStatus) {
    this.progressNoteStatus = progressNoteStatus;
  }

  public PatientDTO getPatient() {
    return patient;
  }

  public void setPatient(PatientDTO patient) {
    this.patient = patient;
  }

  public ClinicianDTO getClinician() {
    return clinician;
  }

  public void setClinician(ClinicianDTO clinician) {
    this.clinician = clinician;
  }

  @Override
  public String toString() {
    return "ClinicianScheduleDTO [patient=" + patient + ", clinician=" + clinician + ", date=" + date + ", length="
        + length + ", age=" + age + ", gender=" + gender + ", reason=" + reason + ", comments=" + comments
        + ", status=" + status + ", patientLocation=" + patientLocation + ", room=" + room + ", checkedIn=" + checkedIn
        + ", waitTime=" + waitTime + ", progressNoteStatus=" + progressNoteStatus + "]";
  }

}
