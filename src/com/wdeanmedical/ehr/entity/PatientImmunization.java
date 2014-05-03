package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "patient_immunization")
public class PatientImmunization extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -210450026605157765L;
  private Integer patientId;
  private Immunization immunization;
  private Date date;

  public PatientImmunization() {
  }



  @Column(name = "patient_id")
  public Integer getPatientId() { return patientId; }
  public void setPatientId(Integer patientId) { this.patientId = patientId; }
  
  @JoinColumn(name = "immunization", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public Immunization getImmunization() {
    return immunization;
  }
  public void setImmunization(Immunization immunization) {
    this.immunization = immunization;
  }

  @Column(name = "date")
  public Date getDate() {
    return date;
  }
  public void setDate(Date date) {
    this.date = date;
  }
  
  
 

}
