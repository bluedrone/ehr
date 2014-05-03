package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "lab")
public class Lab extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1462745762564975233L;
  
  private Date date;
  private Integer encounterId;
  private Integer patientId;
  private Integer clinicianId;
  private String hb;
  private String glucose;
  private String urineDIP;

  
  public Lab() {
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

  @Column(name = "hb")
  public String getHb() { return hb; }
  public void setHb(String hb) { this.hb = hb; }

  @Column(name = "glucose")
  public String getGlucose() { return glucose; }
  public void setGlucose(String glucose) { this.glucose = glucose; }

  @Column(name = "urine_dip")
  public String getUrineDIP() { return urineDIP; }
  public void setUrineDIP(String urineDIP) { this.urineDIP = urineDIP; }
  
}
