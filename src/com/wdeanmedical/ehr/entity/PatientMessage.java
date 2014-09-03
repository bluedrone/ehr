/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */
 
package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "patient_message")
public class PatientMessage extends BaseEntity implements Serializable {
  private static final long serialVersionUID = -568848644986949555L;
  private Patient patient;
  private String subject;
  private Date date;
  private String from;
  private Clinician clinician;
  private Boolean fromClinician;
  private Boolean readByRecipient;
  private String content;
  private PatientMessageType patientMessageType;

  public PatientMessage() {
  }

  @JoinColumn(name = "patient", referencedColumnName = "id")
  @ManyToOne(optional = true)
  public Patient getPatient() {
    return patient;
  }

  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  @Column(name = "subject")
  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  @Column(name = "date")
  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  @JoinColumn(name = "clinician", referencedColumnName = "id")
  @ManyToOne(optional = true)
  public Clinician getClinician() {
    return clinician;
  }

  public void setClinician(Clinician clinician) {
    this.clinician = clinician;
  }

  @Column(name = "sent_from")
  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  @Column(name = "content")
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Column(name = "from_clinician")
  public Boolean getFromClinician() {
    return fromClinician;
  }

  public void setFromClinician(Boolean fromClinician) {
    this.fromClinician = fromClinician;
  }

  @JoinColumn(name = "patient_message_type", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public PatientMessageType getPatientMessageType() {
    return patientMessageType;
  }

  public void setPatientMessageType(PatientMessageType patientMessageType) {
    this.patientMessageType = patientMessageType;
  }

  @Column(name = "read_by_recipient")
  public Boolean getReadByRecipient() {
    return readByRecipient;
  }

  public void setReadByRecipient(Boolean readByRecipient) {
    this.readByRecipient = readByRecipient;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result
        + ((clinician == null) ? 0 : clinician.hashCode());
    result = prime * result + ((content == null) ? 0 : content.hashCode());
    result = prime * result + ((date == null) ? 0 : date.hashCode());
    result = prime * result + ((from == null) ? 0 : from.hashCode());
    result = prime * result
        + ((fromClinician == null) ? 0 : fromClinician.hashCode());
    result = prime * result + ((patient == null) ? 0 : patient.hashCode());
    result = prime
        * result
        + ((patientMessageType == null) ? 0 : patientMessageType
            .hashCode());
    result = prime * result
        + ((readByRecipient == null) ? 0 : readByRecipient.hashCode());
    result = prime * result + ((subject == null) ? 0 : subject.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      {return false;}
    if (getClass() != obj.getClass())
      {return false;}
    PatientMessage other = (PatientMessage) obj;
    if (clinician == null) {
      if (other.clinician != null)
        {return false;}
    } else if (!clinician.equals(other.clinician))
      {return false;}
    if (content == null) {
      if (other.content != null)
        {return false;}
    } else if (!content.equals(other.content))
      {return false;}
    if (date == null) {
      if (other.date != null)
        {return false;}
    } else if (!date.equals(other.date))
      {return false;}
    if (from == null) {
      if (other.from != null)
        {return false;}
    } else if (!from.equals(other.from))
      {return false;}
    if (fromClinician == null) {
      if (other.fromClinician != null)
        {return false;}
    } else if (!fromClinician.equals(other.fromClinician))
      {return false;}
    if (patient == null) {
      if (other.patient != null)
        {return false;}
    } else if (!patient.equals(other.patient))
      {return false;}
    if (patientMessageType == null) {
      if (other.patientMessageType != null)
        {return false;}
    } else if (!patientMessageType.equals(other.patientMessageType))
      {return false;}
    if (readByRecipient == null) {
      if (other.readByRecipient != null)
        {return false;}
    } else if (!readByRecipient.equals(other.readByRecipient))
      {return false;}
    if (subject == null) {
      if (other.subject != null)
        {return false;}
    } else if (!subject.equals(other.subject))
      {return false;}
    return true;
  }

  @Override
  public String toString() {
    return "PatientMessage [patient=" + patient + ", subject=" + subject
        + ", date=" + date + ", from=" + from + ", clinician="
        + clinician + ", fromClinician=" + fromClinician
        + ", readByRecipient=" + readByRecipient + ", content="
        + content + ", patientMessageType=" + patientMessageType + "]";
  }

}
