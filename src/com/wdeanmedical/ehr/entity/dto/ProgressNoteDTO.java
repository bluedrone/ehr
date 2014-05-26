/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

package com.wdeanmedical.ehr.entity.dto;

import java.util.Date;

public class ProgressNoteDTO {

  private PatientDTO patient;
  private ClinicianDTO clinician;
  private Date date;
  private String subject;
  private String content;
  private Boolean completed = false;

  public ProgressNoteDTO() {
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public Boolean getCompleted() {
    return completed;
  }

  public void setCompleted(Boolean completed) {
    this.completed = completed;
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
    return "ProgressNoteDTO [patient=" + patient + ", clinician=" + clinician + ", date=" + date + ", subject="
        + subject + ", content=" + content + ", completed=" + completed + "]";
  }

}
