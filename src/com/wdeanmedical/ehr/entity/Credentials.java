package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "credentials")
public class Credentials extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1462745762564975233L;
  
  private Integer patientId;
  private String mrn;
  private String username;
  private String password;
  private String firstName;
  private String middleName;
  private String lastName;
  private String additionalName;
  private String email;
  private String salt;
  private int authStatus;
  private int accessLevel;
  private String sessionId;
  private PatientStatus status;
  private String govtId;
  private Date lastLoginTime;
  private String previousLoginTime;
  private String activationCode;
  
  public Credentials() {
  }
  
  @Column(name = "patient_id")
  public Integer getPatientId() { return patientId; }
  public void setPatientId(Integer patientId) { this.patientId = patientId; }
  
  @Column(name = "username")
  public String getUsername() { return username; }
  public void setUsername(String username) { this.username = username; }

  @Column(name = "password")
  @Basic(optional = false)
  public String getPassword() { return password; }
  public void setPassword(String password) { this.password = password; }

  @Column(name = "first_name")
  @Basic(optional = false)
  public String getFirstName() { return firstName; }
  public void setFirstName(String firstName) { this.firstName = firstName; }

  @Column(name = "middle_name")
  public String getMiddleName() { return middleName; }
  public void setMiddleName(String middleName) { this.middleName = middleName; }

  @Column(name = "last_name")
  @Basic(optional = false)
  public String getLastName() { return lastName; }
  public void setLastName(String lastName) { this.lastName = lastName; }
  
  @Column(name = "email")
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  @Column(name = "salt")
  public String getSalt() { return salt; }
  public void setSalt(String salt) { this.salt = salt; }
  
  @Column(name = "last_login_time")
  public Date getLastLoginTime() { return lastLoginTime; }
  public void setLastLoginTime(Date lastLoginTime) { this.lastLoginTime = lastLoginTime; }
  
  @Column(name = "access_level")
  public int getAccessLevel() { return accessLevel; }
  public void setAccessLevel(int accessLevel) { this.accessLevel = accessLevel; }
  
  @Column(name = "activation_code")
  public String getActivationCode() { return activationCode; }
  public void setActivationCode(String activationCode) { this.activationCode = activationCode; }
  
  @JoinColumn(name = "patient_status", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public PatientStatus getStatus() { return status; }
  public void setStatus(PatientStatus status) { this.status = status; }
  
  @Column(name = "mrn")
  public String getMrn() { return mrn; }
  public void setMrn(String mrn) { this.mrn = mrn; }
  
  @Column(name = "additional_name")
  public String getAdditionalName() { return additionalName; }
  public void setAdditionalName(String additionalName) { this.additionalName = additionalName; }
  
  @Column(name = "govt_id")
  public String getGovtId() { return govtId; }
  public void setGovtId(String govtId) { this.govtId = govtId; }
  
  @Transient
  public Integer getAuthStatus() { return authStatus; }
  public void setAuthStatus(Integer authStatus) { this.authStatus = authStatus; }

  @Transient
  public String getPreviousLoginTime() { return previousLoginTime; }
  public void setPreviousLoginTime(String previousLoginTime) { this.previousLoginTime = previousLoginTime; }

  @Transient
  public String getSessionId() { return sessionId; }
  public void setSessionId(String sessionId) { this.sessionId = sessionId; }

  
}
