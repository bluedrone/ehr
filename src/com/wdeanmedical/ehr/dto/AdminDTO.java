/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */
 
package com.wdeanmedical.ehr.dto; 

import java.util.List;

import com.wdeanmedical.ehr.entity.PatientAllergen;
import com.wdeanmedical.ehr.entity.PatientMedication;


public class AdminDTO extends AuthorizedDTO {

  public AdminDTO() {
  }
  
  private Integer clinicianId;
  private String firstName;
  private String middleName;
  private String lastName;
  private String username;
  private String primaryPhone;
  private String secondaryPhone;
  private String email;
  private String pager;
  private String password;
  private Integer roleId;
  private Integer credentialId;
  private String groupName;
  private String practiceName;
  private String updateProperty;
  private String updatePropertyValue;
  
  
  public Integer getClinicianId() { return clinicianId; }
  public void setClinicianId(Integer clinicianId) { this.clinicianId = clinicianId; }
  
  public String getFirstName() { return firstName; }
  public void setFirstName(String firstName) { this.firstName = firstName; }
  
  public String getMiddleName() { return middleName; }
  public void setMiddleName(String middleName) { this.middleName = middleName; }
  
  public String getLastName() { return lastName; }
  public void setLastName(String lastName) { this.lastName = lastName; }
  
  public String getUsername() { return username; }
  public void setUsername(String username) { this.username = username; }
  
  public String getPrimaryPhone() { return primaryPhone; }
  public void setPrimaryPhone(String primaryPhone) { this.primaryPhone = primaryPhone; }
  
  public String getSecondaryPhone() { return secondaryPhone; }
  public void setSecondaryPhone(String secondaryPhone) { this.secondaryPhone = secondaryPhone; }
  
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }
  
  public String getPager() { return pager; }
  public void setPager(String pager) { this.pager = pager; }
  
  public String getPassword() { return password; }
  public void setPassword(String password) { this.password = password; }
  
  public Integer getRoleId() { return roleId; }
  public void setRoleId(Integer roleId) { this.roleId = roleId; }
  
  public Integer getCredentialId() { return credentialId; }
  public void setCredentialId(Integer credentialId) { this.credentialId = credentialId; }
  
  public String getGroupName() { return groupName; }
  public void setGroupName(String groupName) { this.groupName = groupName; }
  
  public String getPracticeName() { return practiceName; }
  public void setPracticeName(String practiceName) { this.practiceName = practiceName; }
  
  public String getUpdateProperty() { return updateProperty; }
  public void setUpdateProperty(String updateProperty) { this.updateProperty = updateProperty; }
  
  public String getUpdatePropertyValue() { return updatePropertyValue; }
  public void setUpdatePropertyValue(String updatePropertyValue) { this.updatePropertyValue = updatePropertyValue; }
 
}