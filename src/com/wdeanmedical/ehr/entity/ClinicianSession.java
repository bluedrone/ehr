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
  private Date  lastAccessTime;
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
  public Clinician getClinician() { return clinician; }
  public void setClinician(Clinician clinician) { this.clinician = clinician; }

  @Column(name = "session_id")
  public String getSessionId() { return sessionId; }
  public void setSessionId(String sessionId) { this.sessionId = sessionId; }
  
  @Column(name = "last_access_time")
  @Temporal(TemporalType.TIMESTAMP)
  @Basic(optional = false)
  public Date getLastAccessTime() { return lastAccessTime; }
  public void setLastAccessTime(Date lastAccessTime) { this.lastAccessTime = lastAccessTime; }
  
  @Column(name = "parked")
  public boolean isParked() { return parked; }
  public void setParked(boolean parked) { this.parked = parked; }

@Override
  public String toString() {
    return "UserSession[" + getSessionId() + ", " + getClinician().getUsername() + ", " + 
            getIpAddress() + ", " + getLastAccessTime() + ", " + isParked() + "]";
  }

}
