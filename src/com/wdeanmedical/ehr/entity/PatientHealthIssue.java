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
  private Patient patient;
  private HealthIssue healthIssue;
  private Date date;

  public PatientHealthIssue() {
  }

  @JoinColumn(name = "patient", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public Patient getPatient() { return patient; }
  public void setPatient(Patient patient) { this.patient = patient; }
  
  @JoinColumn(name = "health_issue", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public  HealthIssue getHealthIssue() { return healthIssue; }
  public void setHealthIssue(HealthIssue healthIssue) { this.healthIssue = healthIssue; }

  @Column(name = "date")
  public Date getDate() { return date; }
  public void setDate(Date date) { this.date = date; }

}
