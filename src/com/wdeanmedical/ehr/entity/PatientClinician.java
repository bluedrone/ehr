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

}
