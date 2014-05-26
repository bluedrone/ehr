/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

package com.wdeanmedical.ehr.entity.dto;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

public class CredentialsDTO {

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
  private PatientStatusDTO status;
  private String govtId;
  private Date lastLoginTime;
  private String previousLoginTime;
  private String activationCode;

  public CredentialsDTO() {
  }

  public Integer getPatientId() {
    return patientId;
  }

  public void setPatientId(Integer patientId) {
    this.patientId = patientId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getSalt() {
    return salt;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  public Date getLastLoginTime() {
    return lastLoginTime;
  }

  public void setLastLoginTime(Date lastLoginTime) {
    this.lastLoginTime = lastLoginTime;
  }

  public int getAccessLevel() {
    return accessLevel;
  }

  public void setAccessLevel(int accessLevel) {
    this.accessLevel = accessLevel;
  }

  public String getActivationCode() {
    return activationCode;
  }

  public void setActivationCode(String activationCode) {
    this.activationCode = activationCode;
  }

  public PatientStatusDTO getStatus() {
    return status;
  }

  public void setStatus(PatientStatusDTO status) {
    this.status = status;
  }

  public String getMrn() {
    return mrn;
  }

  public void setMrn(String mrn) {
    this.mrn = mrn;
  }

  public String getAdditionalName() {
    return additionalName;
  }

  public void setAdditionalName(String additionalName) {
    this.additionalName = additionalName;
  }

  public String getGovtId() {
    return govtId;
  }

  public void setGovtId(String govtId) {
    this.govtId = govtId;
  }

  public Integer getAuthStatus() {
    return authStatus;
  }

  public void setAuthStatus(Integer authStatus) {
    this.authStatus = authStatus;
  }

  public String getPreviousLoginTime() {
    return previousLoginTime;
  }

  public void setPreviousLoginTime(String previousLoginTime) {
    this.previousLoginTime = previousLoginTime;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  @Override
  public String toString() {
    return "CredentialsDTO [patientId=" + patientId + ", mrn=" + mrn + ", username=" + username + ", password="
        + password + ", firstName=" + firstName + ", middleName=" + middleName + ", lastName=" + lastName
        + ", additionalName=" + additionalName + ", email=" + email + ", salt=" + salt + ", authStatus=" + authStatus
        + ", accessLevel=" + accessLevel + ", sessionId=" + sessionId + ", status=" + status + ", govtId=" + govtId
        + ", lastLoginTime=" + lastLoginTime + ", previousLoginTime=" + previousLoginTime + ", activationCode="
        + activationCode + "]";
  }

}
