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
@Table(name = "patient_medical_procedure")
public class PatientMedicalProcedure extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 2224217532626243605L;
  private Patient patient;
  private MedicalProcedure medicalProcedure;
  private MedicalTestStatus status;
  private Date dueDate;
  private Date lastDone;

  public PatientMedicalProcedure() {
  }

  @JoinColumn(name = "patient", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public Patient getPatient() {
    return patient;
  }

  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  @JoinColumn(name = "medical_procedure", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public MedicalProcedure getMedicalProcedure() {
    return medicalProcedure;
  }

  public void setMedicalProcedure(MedicalProcedure medicalProcedure) {
    this.medicalProcedure = medicalProcedure;
  }

  @Column(name = "due_date")
  public Date getDueDate() {
    return dueDate;
  }

  public void setDueDate(Date dueDate) {
    this.dueDate = dueDate;
  }

  @JoinColumn(name = "status", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public MedicalTestStatus getStatus() {
    return status;
  }

  public void setStatus(MedicalTestStatus status) {
    this.status = status;
  }

  @Column(name = "last_done")
  public Date getLastDone() {
    return lastDone;
  }

  public void setLastDone(Date lastDone) {
    this.lastDone = lastDone;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((dueDate == null) ? 0 : dueDate.hashCode());
    result = prime * result
        + ((lastDone == null) ? 0 : lastDone.hashCode());
    result = prime
        * result
        + ((medicalProcedure == null) ? 0 : medicalProcedure.hashCode());
    result = prime * result + ((patient == null) ? 0 : patient.hashCode());
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
    PatientMedicalProcedure other = (PatientMedicalProcedure) obj;
    if (dueDate == null) {
      if (other.dueDate != null)
        {return false;}
    } else if (!dueDate.equals(other.dueDate))
      {return false;}
    if (lastDone == null) {
      if (other.lastDone != null)
        {return false;}
    } else if (!lastDone.equals(other.lastDone))
      {return false;}
    if (medicalProcedure == null) {
      if (other.medicalProcedure != null)
        {return false;}
    } else if (!medicalProcedure.equals(other.medicalProcedure))
      {return false;}
    if (patient == null) {
      if (other.patient != null)
        {return false;}
    } else if (!patient.equals(other.patient))
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
    return "PatientMedicalProcedure [patient=" + patient
        + ", medicalProcedure=" + medicalProcedure + ", status="
        + status + ", dueDate=" + dueDate + ", lastDone=" + lastDone
        + "]";
  }

}
