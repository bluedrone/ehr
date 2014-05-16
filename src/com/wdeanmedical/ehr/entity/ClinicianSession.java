/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */
 
package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "clinician_session")
public class ClinicianSession extends BaseEntity implements Serializable {
  private static final long serialVersionUID = -4156072292651636347L;
  private Clinician clinician;
  private String ipAddress;
  private String sessionId;
  private Date lastAccessTime;
  private boolean parked;

  public ClinicianSession() {
  }

  @Column(name = "ip_address")
  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  @JoinColumn(name = "clinician", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public Clinician getClinician() {
    return clinician;
  }

  public void setClinician(Clinician clinician) {
    this.clinician = clinician;
  }

  @Column(name = "session_id")
  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  @Column(name = "last_access_time")
  @Temporal(TemporalType.TIMESTAMP)
  @Basic(optional = false)
  public Date getLastAccessTime() {
    return lastAccessTime;
  }

  public void setLastAccessTime(Date lastAccessTime) {
    this.lastAccessTime = lastAccessTime;
  }

  @Column(name = "parked")
  public boolean isParked() {
    return parked;
  }

  public void setParked(boolean parked) {
    this.parked = parked;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result
        + ((clinician == null) ? 0 : clinician.hashCode());
    result = prime * result
        + ((ipAddress == null) ? 0 : ipAddress.hashCode());
    result = prime * result
        + ((lastAccessTime == null) ? 0 : lastAccessTime.hashCode());
    result = prime * result + (parked ? 1231 : 1237);
    result = prime * result
        + ((sessionId == null) ? 0 : sessionId.hashCode());
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
    ClinicianSession other = (ClinicianSession) obj;
    if (clinician == null) {
      if (other.clinician != null)
        {return false;}
    } else if (!clinician.equals(other.clinician))
      {return false;}
    if (ipAddress == null) {
      if (other.ipAddress != null)
        {return false;}
    } else if (!ipAddress.equals(other.ipAddress))
      {return false;}
    if (lastAccessTime == null) {
      if (other.lastAccessTime != null)
        {return false;}
    } else if (!lastAccessTime.equals(other.lastAccessTime))
      {return false;}
    if (parked != other.parked)
      {return false;}
    if (sessionId == null) {
      if (other.sessionId != null)
        {return false;}
    } else if (!sessionId.equals(other.sessionId))
      {return false;}
    return true;
  }

  @Override
  public String toString() {
    return "ClinicianSession [clinician=" + clinician + ", ipAddress="
        + ipAddress + ", sessionId=" + sessionId + ", lastAccessTime="
        + lastAccessTime + ", parked=" + parked + "]";
  }

}
