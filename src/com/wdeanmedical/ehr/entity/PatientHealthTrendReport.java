package com.wdeanmedical.ehr.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "patient_health_trend_report")
public class PatientHealthTrendReport extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 6364564784378683322L;
  private Integer patientId;
  private HealthTrendReport healthTrendReport;

  public PatientHealthTrendReport() {
  }



  @Column(name = "patient_id")
  public Integer getPatientId() { return patientId; }
  public void setPatientId(Integer patientId) { this.patientId = patientId; }
  
  @JoinColumn(name = "health_trend_report", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public  HealthTrendReport getHealthTrendReport() {
    return healthTrendReport;
  }
  public void setHealthTrendReport(HealthTrendReport healthTrendReport) {
    this.healthTrendReport = healthTrendReport;
  }

}
