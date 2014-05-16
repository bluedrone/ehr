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
@Table(name = "patient_medical_test")
public class PatientMedicalTest extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -3974028308118232534L;

  private Integer patientId;
  private MedicalTest medicalTest;
  private MedicalTestStatus status;
  private Date date;
  private Clinician clinician;

  public PatientMedicalTest() {
  }

  @Column(name = "patient_id")
  public Integer getPatientId() {
    return patientId;
  }

  public void setPatientId(Integer patientId) {
    this.patientId = patientId;
  }

  @JoinColumn(name = "medical_test", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public MedicalTest getMedicalTest() {
    return medicalTest;
  }

  public void setMedicalTest(MedicalTest medicalTest) {
    this.medicalTest = medicalTest;
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
  public MedicalTestStatus getStatus() {
    return status;
  }

  public void setStatus(MedicalTestStatus status) {
    this.status = status;
  }

  @JoinColumn(name = "clinician", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public Clinician getClinician() {
    return clinician;
  }

  public void setClinician(Clinician clinician) {
    this.clinician = clinician;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result
        + ((clinician == null) ? 0 : clinician.hashCode());
    result = prime * result + ((date == null) ? 0 : date.hashCode());
    result = prime * result
        + ((medicalTest == null) ? 0 : medicalTest.hashCode());
    result = prime * result
        + ((patientId == null) ? 0 : patientId.hashCode());
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
    PatientMedicalTest other = (PatientMedicalTest) obj;
    if (clinician == null) {
      if (other.clinician != null)
        {return false;}
    } else if (!clinician.equals(other.clinician))
      {return false;}
    if (date == null) {
      if (other.date != null)
        {return false;}
    } else if (!date.equals(other.date))
      {return false;}
    if (medicalTest == null) {
      if (other.medicalTest != null)
        {return false;}
    } else if (!medicalTest.equals(other.medicalTest))
      {return false;}
    if (patientId == null) {
      if (other.patientId != null)
        {return false;}
    } else if (!patientId.equals(other.patientId))
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
    return "PatientMedicalTest [patientId=" + patientId + ", medicalTest="
        + medicalTest + ", status=" + status + ", date=" + date
        + ", clinician=" + clinician + "]";
  }

}
