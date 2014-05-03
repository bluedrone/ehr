package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "patient_follow_up")
public class PatientFollowUp extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -8700198827939079441L;
  private Integer patientId;
  private Integer encounterId; 
  private Integer clinicianId;
  private Date date;
  private Date followUpDate; 
  
  private String level; 
  private String when; 
  private String condition; 
  private String dispenseRx; 
  private String USS; 
  private String pregnant; 
  private String woundCare; 
  private String refToSpecialist; 
  private String dentalList; 
  private String physiotherapy; 
  private String bloodLabs; 
  private String other; 
  private String pulmonaryFXTest; 
  private String vision; 
  private boolean completed; 
  private String notes; 

  public PatientFollowUp() {
  }
  
  @Column(name = "patient_id")
  public Integer getPatientId() { return patientId; }
  public void setPatientId(Integer patientId) { this.patientId = patientId; }

  @Column(name = "clinician_id")
  public Integer getClinicianId() { return clinicianId; }
  public void setClinicianId(Integer clinicianId) { this.clinicianId = clinicianId; }
  
  @Column(name = "encounter_id")
  public Integer getEncounterId() { return encounterId; }
  public void setEncounterId(Integer encounterId) { this.encounterId = encounterId; }

  @Column(name = "date")
  public Date getDate() { return date; }
  public void setDate(Date date) { this.date = date; }
  
  @Column(name = "follow_up_date")
  public Date getFollowUpDate() { return followUpDate; }
  public void setFollowUpDate(Date followUpDate) { this.followUpDate = followUpDate; }

  @Column(name = "level")
  public String getLevel() { return level; }
  public void setLevel(String level) { this.level = level; }

  @Column(name = "follow_up_when")
  public String getwhen() { return when; }
  public void setWhen(String when) { this.when = when; }

  @Column(name = "follow_up_condition")
  public String getCondition() { return condition; }
  public void setCondition(String condition) { this.condition = condition; }

  @Column(name = "dispense_rx")
  public String getDispenseRx() { return dispenseRx; }
  public void setDispenseRx(String dispenseRx) { this.dispenseRx = dispenseRx; }

  @Column(name = "uss")
  public String getUSS() { return USS; }
  public void setUSS(String USS) { this.USS = USS; }

  @Column(name = "pregnant")
  public String getPregnant() { return pregnant; }
  public void setPregnant(String pregnant) { this.pregnant = pregnant; }

  @Column(name = "wound_care")
  public String getWoundCare() { return woundCare; }
  public void setWoundCare(String woundCare) { this.woundCare = woundCare; }

  @Column(name = "ref_specialist")
  public String getRefToSpecialist() { return refToSpecialist; }
  public void setRefToSpecialist(String refToSpecialist) { this.refToSpecialist = refToSpecialist; }

  @Column(name = "dental_list")
  public String getDentalList() { return dentalList; }
  public void setDentalList(String dentalList) { this.dentalList = dentalList; }

  @Column(name = "physiotherapy")
  public String getPhysiotherapy() { return physiotherapy; }
  public void setPhysiotherapy(String physiotherapy) { this.physiotherapy = physiotherapy; }

  @Column(name = "blood_labs")
  public String getBloodLabs() { return bloodLabs; }
  public void setBloodLabs(String bloodLabs) { this.bloodLabs = bloodLabs; }

  @Column(name = "other")
  public String getOther() { return other; }
  public void setOther(String other) { this.other = other; }

  @Column(name = "pulmonary_fx_test")
  public String getPulmonaryFXTest() { return pulmonaryFXTest; }
  public void setPulmonaryFXTest(String pulmonaryFXTest) { this.pulmonaryFXTest = pulmonaryFXTest; }

  @Column(name = "vision")
  public String getVision() { return vision; }
  public void setVision(String vision) { this.vision = vision; }

  @Column(name = "complete")
  public boolean isCompleted() { return completed; }
  public void setCompleted(boolean completed) { this.completed = completed; }

  @Column(name = "notes")
  public String getNotes() { return notes; }
  public void setNotes(String notes) { this.notes = notes; }
}
