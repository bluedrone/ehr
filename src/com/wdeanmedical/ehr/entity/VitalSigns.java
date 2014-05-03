package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "vital_signs")
public class VitalSigns extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1342327774068897447L;
  
  private Integer encounterId; 
  private Date date;
  private Integer patientId;
  private Integer clinicianId;
  private Float height;
  private Float weight;
  private Float bmi;
  private Float ofc;
  private Float temperature;
  private Integer pulse;
  private Integer respiration;
  private Integer systolic;
  private Integer diastolic;
  private Float oximetry;
  private Float arm;

  public VitalSigns() {
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

  @Column(name = "height")
  public Float getHeight() { return height; }
  public void setHeight(Float height) { this.height = height; }

  @Column(name = "weight")
  public Float getWeight() { return weight; }
  public void setWeight(Float weight) { this.weight = weight; }

  @Column(name = "bmi")
  public Float getBmi() { return bmi; }
  public void setBmi(Float bmi) { this.bmi = bmi; }

  @Column(name = "ofc")
  public Float getOfc() { return ofc; }
  public void setOfc(Float ofc) { this.ofc = ofc; }

  @Column(name = "temperature")
  public Float getTemperature() { return temperature; }
  public void setTemperature(Float temperature) { this.temperature = temperature; }

  @Column(name = "pulse")
  public Integer getPulse() { return pulse; }
  public void setPulse(Integer pulse) { this.pulse = pulse; }

  @Column(name = "respiration")
  public Integer getRespiration() { return respiration; }
  public void setRespiration(Integer respiration) { this.respiration = respiration; }

  @Column(name = "systolic")
  public Integer getSystolic() { return systolic; }
  public void setSystolic(Integer systolic) { this.systolic = systolic; }

  @Column(name = "diastolic")
  public Integer getDiastolic() { return diastolic; }
  public void setDiastolic(Integer diastolic) { this.diastolic = diastolic; }

  @Column(name = "oximetry")
  public Float getOximetry() { return oximetry; }
  public void setOximetry(Float oximetry) { this.oximetry = oximetry; }

  @Column(name = "arm")
  public Float getArm() { return arm; }
  public void setArm(Float arm) { this.arm = arm; }
  
  
  

}
