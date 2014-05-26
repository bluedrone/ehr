/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

package com.wdeanmedical.ehr.entity.dto;

import java.util.Date;

public class PatientMessageDTO {
  private PatientDTO patientDTO;
  private String subject;
  private Date date;
  private String from;
  private ClinicianDTO clinicianDTO;
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

  public PatientDTO getPatientDTO() {
    return patientDTO;
  }

  public void setPatientDTO(PatientDTO patientDTO) {
    this.patientDTO = patientDTO;
  }

  public ClinicianDTO getClinicianDTO() {
    return clinicianDTO;
  }

  public void setClinicianDTO(ClinicianDTO clinicianDTO) {
    this.clinicianDTO = clinicianDTO;
  }

  @Override
  public String toString() {
    return "PatientMessageDTO [patientDTO=" + patientDTO + ", subject=" + subject + ", date=" + date + ", from=" + from
        + ", clinicianDTO=" + clinicianDTO + ", fromClinician=" + fromClinician + ", readByRecipient="
        + readByRecipient + ", content=" + content + ", patientMessageTypeDTO=" + patientMessageTypeDTO + "]";
  }

}
