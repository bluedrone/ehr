/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */
 
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
  public Patient getPatient() {
    return patient;
  }

  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  @JoinColumn(name = "health_issue", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public HealthIssue getHealthIssue() {
    return healthIssue;
  }

  public void setHealthIssue(HealthIssue healthIssue) {
    this.healthIssue = healthIssue;
  }

  @Column(name = "date")
  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((date == null) ? 0 : date.hashCode());
    result = prime * result
        + ((healthIssue == null) ? 0 : healthIssue.hashCode());
    result = prime * result + ((patient == null) ? 0 : patient.hashCode());
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
    PatientHealthIssue other = (PatientHealthIssue) obj;
    if (date == null) {
      if (other.date != null)
        {return false;}
    } else if (!date.equals(other.date))
      {return false;}
    if (healthIssue == null) {
      if (other.healthIssue != null)
        {return false;}
    } else if (!healthIssue.equals(other.healthIssue))
      {return false;}
    if (patient == null) {
      if (other.patient != null)
        {return false;}
    } else if (!patient.equals(other.patient))
      {return false;}
    return true;
  }

  @Override
  public String toString() {
    return "PatientHealthIssue [patient=" + patient + ", healthIssue="
        + healthIssue + ", date=" + date + "]";
  }

}
