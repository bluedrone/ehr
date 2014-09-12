/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */
 
package com.wdeanmedical.ehr.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "patient_history_medication")
public class PatientHistoryMedication extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 548926604494760704L;

  private String medication;
  private String dose;
  private String frequency;
  private int patientId;

  public PatientHistoryMedication() {
  }

  @Column(name = "medication")
  public String getMedication() {
    return medication;
  }

  public void setMedication(String medication) {
    this.medication = medication;
  }

  @Column(name = "dose")
  public String getDose() {
    return dose;
  }

  public void setDose(String dose) {
    this.dose = dose;
  }

  @Column(name = "frequency")
  public String getFrequency() {
    return frequency;
  }

  public void setFrequency(String frequency) {
    this.frequency = frequency;
  }

  @Column(name = "patient_id")
  public int getPatientId() {
    return patientId;
  }

  public void setPatientId(int patientId) {
    this.patientId = patientId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((dose == null) ? 0 : dose.hashCode());
    result = prime * result
        + ((frequency == null) ? 0 : frequency.hashCode());
    result = prime * result
        + ((medication == null) ? 0 : medication.hashCode());
    result = prime * result + patientId;
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
    PatientHistoryMedication other = (PatientHistoryMedication) obj;
    if (dose == null) {
      if (other.dose != null)
        {return false;}
    } else if (!dose.equals(other.dose))
      {return false;}
    if (frequency == null) {
      if (other.frequency != null)
        {return false;}
    } else if (!frequency.equals(other.frequency))
      {return false;}
    if (medication == null) {
      if (other.medication != null)
        {return false;}
    } else if (!medication.equals(other.medication))
      {return false;}
    if (patientId != other.patientId)
      {return false;}
    return true;
  }

  @Override
  public String toString() {
    return "PatientHistoryMedication [medication=" + medication + ", dose="
        + dose + ", frequency=" + frequency + ", patientId="
        + patientId + "]";
  }

}
