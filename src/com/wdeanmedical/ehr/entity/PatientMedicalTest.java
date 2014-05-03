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
  public Integer getPatientId() { return patientId; }
  public void setPatientId(Integer patientId) { this.patientId = patientId; }
  
  @JoinColumn(name = "medical_test", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public  MedicalTest getMedicalTest() {
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

}
