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
@Table(name = "patient_medication")
public class PatientMedication extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 3194585819435273679L;

  private Patient patient;
  private Medication medication;
  private String unit;
  private String instructions;
  private Date date;
  private Clinician prescriber;

  public PatientMedication() {
  }

  @JoinColumn(name = "patient", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public Patient getPatient() {
    return patient;
  }

  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  @JoinColumn(name = "medication", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public Medication getMedication() {
    return medication;
  }

  public void setMedication(Medication medication) {
    this.medication = medication;
  }

  @Column(name = "unit")
  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  @Column(name = "instructions")
  public String getInstructions() {
    return instructions;
  }

  public void setInstructions(String instructions) {
    this.instructions = instructions;
  }

  @Column(name = "date")
  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  @JoinColumn(name = "prescriber", referencedColumnName = "id")
  @ManyToOne(optional = true)
  public Clinician getPrescriber() {
    return prescriber;
  }

  public void setPrescriber(Clinician prescriber) {
    this.prescriber = prescriber;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((date == null) ? 0 : date.hashCode());
    result = prime * result
        + ((instructions == null) ? 0 : instructions.hashCode());
    result = prime * result
        + ((medication == null) ? 0 : medication.hashCode());
    result = prime * result + ((patient == null) ? 0 : patient.hashCode());
    result = prime * result
        + ((prescriber == null) ? 0 : prescriber.hashCode());
    result = prime * result + ((unit == null) ? 0 : unit.hashCode());
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
    PatientMedication other = (PatientMedication) obj;
    if (date == null) {
      if (other.date != null)
        {return false;}
    } else if (!date.equals(other.date))
      {return false;}
    if (instructions == null) {
      if (other.instructions != null)
        {return false;}
    } else if (!instructions.equals(other.instructions))
      {return false;}
    if (medication == null) {
      if (other.medication != null)
        {return false;}
    } else if (!medication.equals(other.medication))
      {return false;}
    if (patient == null) {
      if (other.patient != null)
        {return false;}
    } else if (!patient.equals(other.patient))
      {return false;}
    if (prescriber == null) {
      if (other.prescriber != null)
        {return false;}
    } else if (!prescriber.equals(other.prescriber))
      {return false;}
    if (unit == null) {
      if (other.unit != null)
        {return false;}
    } else if (!unit.equals(other.unit))
      {return false;}
    return true;
  }

  @Override
  public String toString() {
    return "PatientMedication [patient=" + patient + ", medication="
        + medication + ", unit=" + unit + ", instructions="
        + instructions + ", date=" + date + ", prescriber="
        + prescriber + "]";
  }

}
