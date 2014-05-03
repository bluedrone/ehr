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
  private Integer patientId;
  private MedicalProcedure medicalProcedure;
  private MedicalTestStatus status;
  private Date dueDate;
  private Date lastDone;

  public PatientMedicalProcedure() {
  }
  
  
  @Column(name = "patient_id")
  public Integer getPatientId() { return patientId; }
  public void setPatientId(Integer patientId) { this.patientId = patientId; }


  @JoinColumn(name = "medical_procedure", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public  MedicalProcedure getMedicalProcedure() {
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


}
