package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "patient_health_issue")
public class PatientHealthIssue extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 2004851625871696041L;
  private Integer patientId;
  private HealthIssue healthIssue;
  private Date date;

  public PatientHealthIssue() {
  }

  @Column(name = "patient_id")
  public Integer getPatientId() { return patientId; }
  public void setPatientId(Integer patientId) { this.patientId = patientId; }
  
  @JoinColumn(name = "health_issue", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public  HealthIssue getHealthIssue() { return healthIssue; }
  public void setHealthIssue(HealthIssue healthIssue) { this.healthIssue = healthIssue; }

  @Column(name = "date")
  public Date getDate() { return date; }
  public void setDate(Date date) { this.date = date; }

}
