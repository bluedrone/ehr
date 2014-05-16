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
@Table(name = "patient_immunization")
public class PatientImmunization extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -210450026605157765L;
  private Integer patientId;
  private Immunization immunization;
  private Date date;

  public PatientImmunization() {
  }

  @Column(name = "patient_id")
  public Integer getPatientId() {
    return patientId;
  }

  public void setPatientId(Integer patientId) {
    this.patientId = patientId;
  }

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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((date == null) ? 0 : date.hashCode());
    result = prime * result
        + ((immunization == null) ? 0 : immunization.hashCode());
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
    PatientImmunization other = (PatientImmunization) obj;
    if (date == null) {
      if (other.date != null)
        {return false;}
    } else if (!date.equals(other.date))
      {return false;}
    if (immunization == null) {
      if (other.immunization != null)
        {return false;}
    } else if (!immunization.equals(other.immunization))
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
    return "PatientImmunization [patientId=" + patientId
        + ", immunization=" + immunization + ", date=" + date + "]";
  }

}
