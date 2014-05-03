package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "lab_review")
public class LabReview extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 8448261762534690734L;
  
  private Integer patientId;
  private Integer clinicianId;
  private Date date;
  private String name;
  private String value;
  

  public LabReview() {
  }
  
  @Column(name = "patient_id")
  public Integer getPatientId() { return patientId; }
  public void setPatientId(Integer patientId) { this.patientId = patientId; }

  @Column(name = "clinician_id")
  public Integer getClinicianId() { return clinicianId; }
  public void setClinicianId(Integer clinicianId) { this.clinicianId = clinicianId; }
  
  @Column(name = "date")
  public Date getDate() { return date; }
  public void setDate(Date date) { this.date = date; }

  @Column(name = "name")
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  @Column(name = "value")
  public String getValue() { return value; }
  public void setValue(String value) { this.value = value; }


}
