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
@Table(name = "user")
public class User extends BaseEntity implements Serializable {

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
  private String email;
  private String fax;
  private String pager;
  private Division division;
  private Department department;
  private Role role;
  private Credential credential;
  private boolean active;
  private boolean purged;
  private String salt;
  private int authStatus;
  private String sessionId;
  private Date lastLoginTime;
  private String previousLoginTime;

  public User() {
  }

  @Column(name = "username")
  @Basic(optional = false)
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Column(name = "password")
  @Basic(optional = false)
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Column(name = "first_name")
  @Basic(optional = false)
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @Column(name = "middle_name")
  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  @Column(name = "last_name")
  @Basic(optional = false)
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Column(name = "primary_phone")
  public String getPrimaryPhone() {
    return primaryPhone;
  }

  public void setPrimaryPhone(String primaryPhone) {
    this.primaryPhone = primaryPhone;
  }

  @Column(name = "secondary_phone")
  public String getSecondaryPhone() {
    return secondaryPhone;
  }

  public void setSecondaryPhone(String secondaryPhone) {
    this.secondaryPhone = secondaryPhone;
  }

  @Column(name = "email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Column(name = "fax")
  public String getFax() {
    return fax;
  }

  public void setFax(String fax) {
    this.fax = fax;
  }

  @Column(name = "pager")
  public String getPager() {
    return pager;
  }

  public void setPager(String pager) {
    this.pager = pager;
  }

  @JoinColumn(name = "division", referencedColumnName = "id")
  @ManyToOne(optional = true)
  public Division getDivision() {
    return division;
  }

  public void setDivision(Division division) {
    this.division = division;
  }

  @JoinColumn(name = "department", referencedColumnName = "id")
  @ManyToOne(optional = true)
  public Department getDepartment() {
    return department;
  }

  public void setDepartment(Department department) {
    this.department = department;
  }

  @JoinColumn(name = "credential", referencedColumnName = "id")
  @ManyToOne(optional = true)
  public Credential getCredential() {
    return credential;
  }

  public void setCredential(Credential credential) {
    this.credential = credential;
  }

  @JoinColumn(name = "role", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  @Column(name = "active")
  public boolean getActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  @Column(name = "purged")
  public boolean getPurged() {
    return purged;
  }

  public void setPurged(boolean purged) {
    this.purged = purged;
  }

  @Column(name = "salt")
  @Basic(optional = false)
  public String getSalt() {
    return salt;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  @Column(name = "last_login_time")
  public Date getLastLoginTime() {
    return lastLoginTime;
  }

  public void setLastLoginTime(Date lastLoginTime) {
    this.lastLoginTime = lastLoginTime;
  }

  @Transient
  public Integer getAuthStatus() {
    return authStatus;
  }

  public void setAuthStatus(Integer authStatus) {
    this.authStatus = authStatus;
  }

  @Transient
  public String getPreviousLoginTime() {
    return previousLoginTime;
  }

  public void setPreviousLoginTime(String previousLoginTime) {
    this.previousLoginTime = previousLoginTime;
  }

  @Transient
  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + (active ? 1231 : 1237);
    result = prime * result + authStatus;
    result = prime * result
        + ((credential == null) ? 0 : credential.hashCode());
    result = prime * result
        + ((department == null) ? 0 : department.hashCode());
    result = prime * result
        + ((division == null) ? 0 : division.hashCode());
    result = prime * result + ((email == null) ? 0 : email.hashCode());
    result = prime * result + ((fax == null) ? 0 : fax.hashCode());
    result = prime * result
        + ((firstName == null) ? 0 : firstName.hashCode());
    result = prime * result
        + ((lastLoginTime == null) ? 0 : lastLoginTime.hashCode());
    result = prime * result
        + ((lastName == null) ? 0 : lastName.hashCode());
    result = prime * result
        + ((middleName == null) ? 0 : middleName.hashCode());
    result = prime * result + ((pager == null) ? 0 : pager.hashCode());
    result = prime * result
        + ((password == null) ? 0 : password.hashCode());
    result = prime
        * result
        + ((previousLoginTime == null) ? 0 : previousLoginTime
            .hashCode());
    result = prime * result
        + ((primaryPhone == null) ? 0 : primaryPhone.hashCode());
    result = prime * result + (purged ? 1231 : 1237);
    result = prime * result + ((role == null) ? 0 : role.hashCode());
    result = prime * result + ((salt == null) ? 0 : salt.hashCode());
    result = prime * result
        + ((secondaryPhone == null) ? 0 : secondaryPhone.hashCode());
    result = prime * result
        + ((sessionId == null) ? 0 : sessionId.hashCode());
    result = prime * result
        + ((username == null) ? 0 : username.hashCode());
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
    User other = (User) obj;
    if (active != other.active)
      {return false;}
    if (authStatus != other.authStatus)
      {return false;}
    if (credential == null) {
      if (other.credential != null)
        {return false;}
    } else if (!credential.equals(other.credential))
      {return false;}
    if (department == null) {
      if (other.department != null)
        {return false;}
    } else if (!department.equals(other.department))
      {return false;}
    if (division == null) {
      if (other.division != null)
        {return false;}
    } else if (!division.equals(other.division))
      {return false;}
    if (email == null) {
      if (other.email != null)
        {return false;}
    } else if (!email.equals(other.email))
      {return false;}
    if (fax == null) {
      if (other.fax != null)
        {return false;}
    } else if (!fax.equals(other.fax))
      {return false;}
    if (firstName == null) {
      if (other.firstName != null)
        {return false;}
    } else if (!firstName.equals(other.firstName))
      {return false;}
    if (lastLoginTime == null) {
      if (other.lastLoginTime != null)
        {return false;}
    } else if (!lastLoginTime.equals(other.lastLoginTime))
      {return false;}
    if (lastName == null) {
      if (other.lastName != null)
        {return false;}
    } else if (!lastName.equals(other.lastName))
      {return false;}
    if (middleName == null) {
      if (other.middleName != null)
        {return false;}
    } else if (!middleName.equals(other.middleName))
      {return false;}
    if (pager == null) {
      if (other.pager != null)
        {return false;}
    } else if (!pager.equals(other.pager))
      {return false;}
    if (password == null) {
      if (other.password != null)
        {return false;}
    } else if (!password.equals(other.password))
      {return false;}
    if (previousLoginTime == null) {
      if (other.previousLoginTime != null)
        {return false;}
    } else if (!previousLoginTime.equals(other.previousLoginTime))
      {return false;}
    if (primaryPhone == null) {
      if (other.primaryPhone != null)
        {return false;}
    } else if (!primaryPhone.equals(other.primaryPhone))
      {return false;}
    if (purged != other.purged)
      {return false;}
    if (role == null) {
      if (other.role != null)
        {return false;}
    } else if (!role.equals(other.role))
      {return false;}
    if (salt == null) {
      if (other.salt != null)
        {return false;}
    } else if (!salt.equals(other.salt))
      {return false;}
    if (secondaryPhone == null) {
      if (other.secondaryPhone != null)
        {return false;}
    } else if (!secondaryPhone.equals(other.secondaryPhone))
      {return false;}
    if (sessionId == null) {
      if (other.sessionId != null)
        {return false;}
    } else if (!sessionId.equals(other.sessionId))
      {return false;}
    if (username == null) {
      if (other.username != null)
        {return false;}
    } else if (!username.equals(other.username))
      {return false;}
    return true;
  }

  @Override
  public String toString() {
    return "User [username=" + username + ", password=" + password
        + ", firstName=" + firstName + ", middleName=" + middleName
        + ", lastName=" + lastName + ", primaryPhone=" + primaryPhone
        + ", secondaryPhone=" + secondaryPhone + ", email=" + email
        + ", fax=" + fax + ", pager=" + pager + ", division="
        + division + ", department=" + department + ", role=" + role
        + ", credential=" + credential + ", active=" + active
        + ", purged=" + purged + ", salt=" + salt + ", authStatus="
        + authStatus + ", sessionId=" + sessionId + ", lastLoginTime="
        + lastLoginTime + ", previousLoginTime=" + previousLoginTime
        + "]";
  }

}
