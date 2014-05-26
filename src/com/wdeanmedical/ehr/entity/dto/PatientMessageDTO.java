/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

package com.wdeanmedical.ehr.entity.dto;

import java.util.Date;

public class PatientMessageDTO {
  private PatientDTO patient;
  private String subject;
  private Date date;
  private String from;
  private ClinicianDTO clinician;
  private Boolean fromClinician;
  private Boolean readByRecipient;
  private String content;
  private PatientMessageTypeDTO patientMessageTypeDTO;

  public PatientMessageDTO() {
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Boolean getFromClinician() {
    return fromClinician;
  }

  public void setFromClinician(Boolean fromClinician) {
    this.fromClinician = fromClinician;
  }

  public PatientMessageTypeDTO getPatientMessageTypeDTO() {
    return patientMessageTypeDTO;
  }

  public void setPatientMessageTypeDTO(PatientMessageTypeDTO patientMessageTypeDTO) {
    this.patientMessageTypeDTO = patientMessageTypeDTO;
  }

  public Boolean getReadByRecipient() {
    return readByRecipient;
  }

  public void setReadByRecipient(Boolean readByRecipient) {
    this.readByRecipient = readByRecipient;
  }

  public PatientDTO getPatient() {
    return patient;
  }

  public void setPatientDTO(PatientDTO patient) {
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
    return "PatientMessageDTO [patientDTO=" + patient + ", subject=" + subject + ", date=" + date + ", from=" + from
        + ", clinicianDTO=" + clinician + ", fromClinician=" + fromClinician + ", readByRecipient=" + readByRecipient
        + ", content=" + content + ", patientMessageTypeDTO=" + patientMessageTypeDTO + "]";
  }

}
