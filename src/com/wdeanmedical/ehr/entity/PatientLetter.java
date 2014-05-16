/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */
 
package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "patient_letter")
public class PatientLetter extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 7809888187633850594L;
  private Patient patient;
  private Date date;
  private PatientLetterStatus status;
  private PatientLetterReason reason;
  private Clinician clinician;
  private String content;

  public PatientLetter() {
  }

  @JoinColumn(name = "patient", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public Patient getPatient() {
    return patient;
  }

  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  @Column(name = "date")
  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  @JoinColumn(name = "status", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public PatientLetterStatus getStatus() {
    return status;
  }

  public void setStatus(PatientLetterStatus status) {
    this.status = status;
  }

  @JoinColumn(name = "reason", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public PatientLetterReason getReason() {
    return reason;
  }

  public void setReason(PatientLetterReason reason) {
    this.reason = reason;
  }

  @JoinColumn(name = "clinician", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public Clinician getClinician() {
    return clinician;
  }

  public void setClinician(Clinician clinician) {
    this.clinician = clinician;
  }

  @Column(name = "content")
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result
        + ((clinician == null) ? 0 : clinician.hashCode());
    result = prime * result + ((content == null) ? 0 : content.hashCode());
    result = prime * result + ((date == null) ? 0 : date.hashCode());
    result = prime * result + ((patient == null) ? 0 : patient.hashCode());
    result = prime * result + ((reason == null) ? 0 : reason.hashCode());
    result = prime * result + ((status == null) ? 0 : status.hashCode());
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
    PatientLetter other = (PatientLetter) obj;
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
    if (patient == null) {
      if (other.patient != null)
        {return false;}
    } else if (!patient.equals(other.patient))
      {return false;}
    if (reason == null) {
      if (other.reason != null)
        {return false;}
    } else if (!reason.equals(other.reason))
      {return false;}
    if (status == null) {
      if (other.status != null)
        {return false;}
    } else if (!status.equals(other.status))
      {return false;}
    return true;
  }

  @Override
  public String toString() {
    return "PatientLetter [patient=" + patient + ", date=" + date
        + ", status=" + status + ", reason=" + reason + ", clinician="
        + clinician + ", content=" + content + "]";
  }

}
