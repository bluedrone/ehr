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
import javax.persistence.Transient;

@Entity
@Table(name = "clinician")
public class Clinician extends BaseEntity implements Serializable {

  public static final Integer STATUS_AUTHORIZED = 1;
  public static final Integer STATUS_NOT_FOUND = 0;
  public static final Integer STATUS_INVALID_PASSWORD = -1;
  public static final Integer STATUS_INACTIVE = -2;

  private static final long serialVersionUID = 8014584895711544530L;
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
  private Role role;
  private Division division;
  private Department department;
  private Credential credential;
  private boolean active;
  private boolean purged;
  private String salt;
  private int authStatus;
  private String sessionId;
  private Date lastLoginTime;
  private String previousLoginTime;
  private User adminUser;

  public Clinician() {
  }

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
  
  @Column(name = "primary_phone")
  public String getPrimaryPhone() { return primaryPhone; }
  public void setPrimaryPhone(String primaryPhone) { this.primaryPhone = primaryPhone; }

  @Column(name = "secondary_phone")
  public String getSecondaryPhone() { return secondaryPhone; }
  public void setSecondaryPhone(String secondaryPhone) { this.secondaryPhone = secondaryPhone; }

  @Column(name = "email")
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  @Column(name = "fax")
  public String getFax() { return fax; }
  public void setFax(String fax) { this.fax = fax; }

  @Column(name = "pager")
  public String getPager() { return pager; }
  public void setPager(String pager) { this.pager = pager; }

  @JoinColumn(name = "division", referencedColumnName = "id")
  @ManyToOne(optional = true)
  public Division getDivision() { return division; }
  public void setDivision(Division division) { this.division = division; }

  @JoinColumn(name = "department", referencedColumnName = "id")
  @ManyToOne(optional = true)
  public Department getDepartment() { return department; }
  public void setDepartment(Department department) { this.department = department; }

  @JoinColumn(name = "credential", referencedColumnName = "id")
  @ManyToOne(optional = true)
  public Credential getCredential() { return credential; }
  public void setCredential(Credential credential) { this.credential = credential; }

  @Column(name = "active")
  public boolean getActive() { return active; }
  public void setActive(boolean active) { this.active = active; }

  @Column(name = "purged")
  public boolean getPurged() { return purged; }
  public void setPurged(boolean purged) { this.purged = purged; }

  @Column(name = "salt")
  @Basic(optional = false)
  public String getSalt() { return salt; }
  public void setSalt(String salt) { this.salt = salt; }
  
  @Column(name = "last_login_time")
  public Date getLastLoginTime() { return lastLoginTime; }
  public void setLastLoginTime(Date lastLoginTime) { this.lastLoginTime = lastLoginTime; }
  
  @Column(name = "username")
  @Basic(optional = false)
  public String getUsername() { return username; }
  public void setUsername(String username) { this.username = username; }
  
  
  @Column(name = "group_name")
  public String getGroupName() { return groupName; }
  public void setGroupName(String groupName) { this.groupName = groupName; }

  @Column(name = "practice_name")
  public String getPracticeName() { return practiceName; }
  public void setPracticeName(String practiceName) { this.practiceName = practiceName; }

  @JoinColumn(name = "role", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public Role getRole() { return role; }
  public void setRole(Role role) { this.role = role; }

  @JoinColumn(name = "admin_user", referencedColumnName = "id")
  @ManyToOne(optional = true)
  public User getAdminUser() { return adminUser; }
  public void setAdminUser(User adminUser) { this.adminUser = adminUser; }

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
