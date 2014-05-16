/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */
 
package com.wdeanmedical.ehr.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "patient_health_trend_report")
public class PatientHealthTrendReport extends BaseEntity implements
    Serializable {

  private static final long serialVersionUID = 6364564784378683322L;
  private Integer patientId;
  private HealthTrendReport healthTrendReport;

  public PatientHealthTrendReport() {
  }

  @Column(name = "patient_id")
  public Integer getPatientId() {
    return patientId;
  }

  public void setPatientId(Integer patientId) {
    this.patientId = patientId;
  }

  @JoinColumn(name = "health_trend_report", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public HealthTrendReport getHealthTrendReport() {
    return healthTrendReport;
  }

  public void setHealthTrendReport(HealthTrendReport healthTrendReport) {
    this.healthTrendReport = healthTrendReport;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime
        * result
        + ((healthTrendReport == null) ? 0 : healthTrendReport
            .hashCode());
    result = prime * result
        + ((patientId == null) ? 0 : patientId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      {return false;}
    if (getClass() != obj.getClass())
      {return false;}
    PatientHealthTrendReport other = (PatientHealthTrendReport) obj;
    if (healthTrendReport == null) {
      if (other.healthTrendReport != null)
        {return false;}
    } else if (!healthTrendReport.equals(other.healthTrendReport))
      {return false;}
    if (patientId == null) {
      if (other.patientId != null)
        {return false;}
    } else if (!patientId.equals(other.patientId))
      {return false;}
    return true;
  }

  @Override
  public String toString() {
    return "PatientHealthTrendReport [patientId=" + patientId
        + ", healthTrendReport=" + healthTrendReport + "]";
  }

}
