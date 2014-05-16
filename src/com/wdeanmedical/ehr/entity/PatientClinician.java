/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */
 
package com.wdeanmedical.ehr.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "patient_clinician")
public class PatientClinician extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1835725779170301037L;

  private Patient patient;
  private Clinician clinician;

  public PatientClinician() {
  }

  @JoinColumn(name = "patient", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public Patient getPatient() {
    return patient;
  }

  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  @JoinColumn(name = "clinician", referencedColumnName = "id")
  @ManyToOne(optional = true)
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
    result = prime * result + ((patient == null) ? 0 : patient.hashCode());
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
    PatientClinician other = (PatientClinician) obj;
    if (clinician == null) {
      if (other.clinician != null)
        {return false;}
    } else if (!clinician.equals(other.clinician))
      {return false;}
    if (patient == null) {
      if (other.patient != null)
        {return false;}
    } else if (!patient.equals(other.patient))
      {return false;}
    return true;
  }

  @Override
  public String toString() {
    return "PatientClinician [patient=" + patient + ", clinician="
        + clinician + "]";
  }

}
