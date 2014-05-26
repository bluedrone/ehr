/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

package com.wdeanmedical.ehr.entity.dto;

import java.util.Date;

public class ClinicianDTO {

  public static final Integer STATUS_AUTHORIZED = 1;
  public static final Integer STATUS_NOT_FOUND = 0;
  public static final Integer STATUS_INVALID_PASSWORD = -1;
  public static final Integer STATUS_INACTIVE = -2;

  private String username;
  private String password;
  private String firstName;
  private String middleName;
  private String lastName;
  private String primaryPhone;
  private String secondaryPhone;
  private String groupName;
  private String practiceName;
  private String email;
  private String fax;
  private String pager;
  private RoleDTO roleDTO;
  private DivisionDTO division;
  private DepartmentDTO department;
  private CredentialDTO credential;
  private boolean active;
  private boolean purged;
  private String salt;
  private int authStatus;
  private String sessionId;
  private Date lastLoginTime;
  private String previousLoginTime;
  private UserDTO adminUser;

  public ClinicianDTO() {
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

  public String getPrimaryPhone() {
    return primaryPhone;
  }

  public void setPrimaryPhone(String primaryPhone) {
    this.primaryPhone = primaryPhone;
  }

  public String getSecondaryPhone() {
    return secondaryPhone;
  }

  public void setSecondaryPhone(String secondaryPhone) {
    this.secondaryPhone = secondaryPhone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFax() {
    return fax;
  }

  public void setFax(String fax) {
    this.fax = fax;
  }

  public String getPager() {
    return pager;
  }

  public void setPager(String pager) {
    this.pager = pager;
  }

  public DivisionDTO getDivision() {
    return division;
  }

  public void setDivision(DivisionDTO division) {
    this.division = division;
  }

  public DepartmentDTO getDepartment() {
    return department;
  }

  public void setDepartment(DepartmentDTO department) {
    this.department = department;
  }

  public CredentialDTO getCredential() {
    return credential;
  }

  public void setCredential(CredentialDTO credential) {
    this.credential = credential;
  }

  public boolean getActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public boolean getPurged() {
    return purged;
  }

  public void setPurged(boolean purged) {
    this.purged = purged;
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

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public String getPracticeName() {
    return practiceName;
  }

  public void setPracticeName(String practiceName) {
    this.practiceName = practiceName;
  }

  public RoleDTO getRoleDTO() {
    return roleDTO;
  }

  public void setRoleDTO(RoleDTO roleDTO) {
    this.roleDTO = roleDTO;
  }

  public UserDTO getAdminUser() {
    return adminUser;
  }

  public void setAdminUser(UserDTO adminUser) {
    this.adminUser = adminUser;
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
    return "ClinicianDTO [username=" + username + ", password=" + password + ", firstName=" + firstName
        + ", middleName=" + middleName + ", lastName=" + lastName + ", primaryPhone=" + primaryPhone
        + ", secondaryPhone=" + secondaryPhone + ", groupName=" + groupName + ", practiceName=" + practiceName
        + ", email=" + email + ", fax=" + fax + ", pager=" + pager + ", roleDTO=" + roleDTO + ", division=" + division
        + ", department=" + department + ", credential=" + credential + ", active=" + active + ", purged=" + purged
        + ", salt=" + salt + ", authStatus=" + authStatus + ", sessionId=" + sessionId + ", lastLoginTime="
        + lastLoginTime + ", previousLoginTime=" + previousLoginTime + ", adminUser=" + adminUser + "]";
  }

}
