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
  public Patient getPatient() { return patient; }
  public void setPatient(Patient patient) { this.patient = patient; }
  
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

}
