package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "exam")
public class Exam extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1462745762564975233L;
  
  private Date date;
  private Integer encounterId;
  private Integer patientId;
  private Integer clinicianId;
  private String hs;
  private String heartRhythm;
  private String diagnosis;
  private String dxCode;
  private String treatmentPlan;
  private String txCode;
  private String diagramPath;
  
  public Exam() {
  }
  
  
  @Column(name = "encounter_id")
  public Integer getEncounterId() { return encounterId; }
  public void setEncounterId(Integer encounterId) { this.encounterId = encounterId; }
  
  @Column(name = "date")
  public Date getDate() { return date; }
  public void setDate(Date date) { this.date = date; }
  
  @Column(name = "patient_id")
  public Integer getPatientId() { return patientId; }
  public void setPatientId(Integer patientId) { this.patientId = patientId; }

  @Column(name = "clinician_id")
  public Integer getClinicianId() { return clinicianId; }
  public void setClinicianId(Integer clinicianId) { this.clinicianId = clinicianId; }

  @Column(name = "hs")
  public String getHs() { return hs; }
  public void setHs(String hs) { this.hs = hs; }

  @Column(name = "heart_rhythm")
  public String getHeartRhythm() { return heartRhythm; }
  public void setHeartRhythm(String heartRhythm) { this.heartRhythm = heartRhythm; }

  @Column(name = "diagnosis")
  public String getDiagnosis() { return diagnosis; }
  public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

  @Column(name = "dx_code")
  public String getDxCode() { return dxCode; }
  public void setDxCode(String dxCode) { this.dxCode = dxCode; }

  @Column(name = "treatment_plan")
  public String getTreatmentPlan() { return treatmentPlan; }
  public void setTreatmentPlan(String treatmentPlan) { this.treatmentPlan = treatmentPlan; }

  @Column(name = "tx_code")
  public String getTxCode() { return txCode; }
  public void setTxCode(String txCode) { this.txCode = txCode; }

  @Column(name = "diagram_path")
  public String getDiagramPath() { return diagramPath; }
  public void setDiagramPath(String diagramPath) { this.diagramPath = diagramPath; }

}
