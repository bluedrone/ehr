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
  public Integer getEncounterId() {
    return encounterId;
  }

  public void setEncounterId(Integer encounterId) {
    this.encounterId = encounterId;
  }

  @Column(name = "date")
  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  @Column(name = "patient_id")
  public Integer getPatientId() {
    return patientId;
  }

  public void setPatientId(Integer patientId) {
    this.patientId = patientId;
  }

  @Column(name = "clinician_id")
  public Integer getClinicianId() {
    return clinicianId;
  }

  public void setClinicianId(Integer clinicianId) {
    this.clinicianId = clinicianId;
  }

  @Column(name = "hb")
  public String getHb() {
    return hb;
  }

  public void setHb(String hb) {
    this.hb = hb;
  }

  @Column(name = "glucose")
  public String getGlucose() {
    return glucose;
  }

  public void setGlucose(String glucose) {
    this.glucose = glucose;
  }

  @Column(name = "urine_dip")
  public String getUrineDIP() {
    return urineDIP;
  }

  public void setUrineDIP(String urineDIP) {
    this.urineDIP = urineDIP;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result
        + ((clinicianId == null) ? 0 : clinicianId.hashCode());
    result = prime * result + ((date == null) ? 0 : date.hashCode());
    result = prime * result
        + ((encounterId == null) ? 0 : encounterId.hashCode());
    result = prime * result + ((glucose == null) ? 0 : glucose.hashCode());
    result = prime * result + ((hb == null) ? 0 : hb.hashCode());
    result = prime * result
        + ((patientId == null) ? 0 : patientId.hashCode());
    result = prime * result
        + ((urineDIP == null) ? 0 : urineDIP.hashCode());
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
    Lab other = (Lab) obj;
    if (clinicianId == null) {
      if (other.clinicianId != null)
        {return false;}
    } else if (!clinicianId.equals(other.clinicianId))
      {return false;}
    if (date == null) {
      if (other.date != null)
        {return false;}
    } else if (!date.equals(other.date))
      {return false;}
    if (encounterId == null) {
      if (other.encounterId != null)
        {return false;}
    } else if (!encounterId.equals(other.encounterId))
      {return false;}
    if (glucose == null) {
      if (other.glucose != null)
        {return false;}
    } else if (!glucose.equals(other.glucose))
      {return false;}
    if (hb == null) {
      if (other.hb != null)
        {return false;}
    } else if (!hb.equals(other.hb))
      {return false;}
    if (patientId == null) {
      if (other.patientId != null)
        {return false;}
    } else if (!patientId.equals(other.patientId))
      {return false;}
    if (urineDIP == null) {
      if (other.urineDIP != null)
        {return false;}
    } else if (!urineDIP.equals(other.urineDIP))
      {return false;}
    return true;
  }

  @Override
  public String toString() {
    return "Lab [date=" + date + ", encounterId=" + encounterId
        + ", patientId=" + patientId + ", clinicianId=" + clinicianId
        + ", hb=" + hb + ", glucose=" + glucose + ", urineDIP="
        + urineDIP + "]";
  }

}
