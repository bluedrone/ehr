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
@Table(name = "demographics")
public class Demographics extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1462745762564975233L;

  private Integer patientId;
  private String primaryPhone;
  private String secondaryPhone;
  private String streetAddress1;
  private String streetAddress2;
  private String city;
  private USState usState;
  private String postalCode;
  private Country country;
  private Ethnicity ethnicity;
  private Race race;
  private Gender gender;
  private MaritalStatus maritalStatus;
  private String employmentStatus;
  private String occupation;
  private String employer;
  private String schoolStatus;
  private String schoolName;
  private Date dob;
  private String profileImagePath;

  public Demographics() {
  }

  @Column(name = "patient_id")
  public Integer getPatientId() { return patientId; }
  public void setPatientId(Integer patientId) { this.patientId = patientId; }

  @Column(name = "profile_image_path")
  public String getProfileImagePath() { return profileImagePath; }
  public void setProfileImagePath(String profileImagePath) { this.profileImagePath = profileImagePath; }

  @Column(name = "dob")
  public Date getDob() { return dob; }
  public void setDob(Date dob) { this.dob = dob; }

  @Column(name = "employment_status")
  public String getEmploymentStatus() { return employmentStatus; }
  public void setEmploymentStatus(String employmentStatus) { this.employmentStatus = employmentStatus; }

  @Column(name = "school_status")
  public String getSchoolStatus() { return schoolStatus; }
  public void setSchoolStatus(String schoolStatus) { this.schoolStatus = schoolStatus; }

  @Column(name = "postal_code")
  public String getPostalCode() { return postalCode; }
  public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

  @Column(name = "primary_phone")
  public String getPrimaryPhone() { return primaryPhone; }
  public void setPrimaryPhone(String primaryPhone) { this.primaryPhone = primaryPhone; }

  @Column(name = "secondary_phone")
  public String getSecondaryPhone() { return secondaryPhone; }
  public void setSecondaryPhone(String secondaryPhone) { this.secondaryPhone = secondaryPhone; }

  @Column(name = "street_address1")
  public String getStreetAddress1() { return streetAddress1; }
  public void setStreetAddress1(String streetAddress1) { this.streetAddress1 = streetAddress1; }

  @Column(name = "street_address2")
  public String getStreetAddress2() { return streetAddress2; }
  public void setStreetAddress2(String streetAddress2) { this.streetAddress2 = streetAddress2; }

  @Column(name = "city")
  public String getCity() { return city; }
  public void setCity(String city) { this.city = city; }

  @JoinColumn(name = "us_state", referencedColumnName = "id")
  @ManyToOne(optional = true)
  public USState getUsState() { return usState; }
  public void setUsState(USState usState) { this.usState = usState; }

  @JoinColumn(name = "country", referencedColumnName = "id")
  @ManyToOne(optional = true)
  public Country getCountry() { return country; }
  public void setCountry(Country country) { this.country = country; }

  @Column(name = "occupation")
  public String getOccupation() { return occupation; }
  public void setOccupation(String occupation) { this.occupation = occupation; }

  @Column(name = "employer")
  public String getEmployer() { return employer; }
  public void setEmployer(String employer) { this.employer = employer; }

  @Column(name = "school_name")
  public String getSchoolName() { return schoolName; }
  public void setSchoolName(String schoolName) { this.schoolName = schoolName; }

  @JoinColumn(name = "ethnicity", referencedColumnName = "id")
  @ManyToOne(optional = true)
  public Ethnicity getEthnicity() { return ethnicity; }
  public void setEthnicity(Ethnicity ethnicity) { this.ethnicity = ethnicity; }

  @JoinColumn(name = "race", referencedColumnName = "id")
  @ManyToOne(optional = true)
  public Race getRace() { return race; }
  public void setRace(Race race) { this.race = race; }

  @JoinColumn(name = "gender", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public Gender getGender() { return gender; }
  public void setGender(Gender gender) { this.gender = gender; }

  @JoinColumn(name = "marital_status", referencedColumnName = "id")
  @ManyToOne(optional = true)
  public MaritalStatus getMaritalStatus() { return maritalStatus; }
  public void setMaritalStatus(MaritalStatus maritalStatus) { this.maritalStatus = maritalStatus; }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((city == null) ? 0 : city.hashCode());
    result = prime * result + ((country == null) ? 0 : country.hashCode());
    result = prime * result + ((dob == null) ? 0 : dob.hashCode());
    result = prime * result
        + ((employer == null) ? 0 : employer.hashCode());
    result = prime
        * result
        + ((employmentStatus == null) ? 0 : employmentStatus.hashCode());
    result = prime * result
        + ((ethnicity == null) ? 0 : ethnicity.hashCode());
    result = prime * result + ((gender == null) ? 0 : gender.hashCode());
    result = prime * result
        + ((maritalStatus == null) ? 0 : maritalStatus.hashCode());
    result = prime * result
        + ((patientId == null) ? 0 : patientId.hashCode());
    result = prime * result
        + ((postalCode == null) ? 0 : postalCode.hashCode());
    result = prime * result
        + ((primaryPhone == null) ? 0 : primaryPhone.hashCode());
    result = prime
        * result
        + ((profileImagePath == null) ? 0 : profileImagePath.hashCode());
    result = prime * result + ((race == null) ? 0 : race.hashCode());
    result = prime * result
        + ((schoolName == null) ? 0 : schoolName.hashCode());
    result = prime * result
        + ((schoolStatus == null) ? 0 : schoolStatus.hashCode());
    result = prime * result
        + ((secondaryPhone == null) ? 0 : secondaryPhone.hashCode());
    result = prime * result
        + ((streetAddress1 == null) ? 0 : streetAddress1.hashCode());
    result = prime * result
        + ((streetAddress2 == null) ? 0 : streetAddress2.hashCode());
    result = prime * result + ((usState == null) ? 0 : usState.hashCode());
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
    Demographics other = (Demographics) obj;
    if (city == null) {
      if (other.city != null)
        {return false;}
    } else if (!city.equals(other.city))
      {return false;}
    if (country == null) {
      if (other.country != null)
        {return false;}
    } else if (!country.equals(other.country))
      {return false;}
    if (dob == null) {
      if (other.dob != null)
        {return false;}
    } else if (!dob.equals(other.dob))
      {return false;}
    if (employer == null) {
      if (other.employer != null)
        {return false;}
    } else if (!employer.equals(other.employer))
      {return false;}
    if (employmentStatus == null) {
      if (other.employmentStatus != null)
        {return false;}
    } else if (!employmentStatus.equals(other.employmentStatus))
      {return false;}
    if (ethnicity == null) {
      if (other.ethnicity != null)
        {return false;}
    } else if (!ethnicity.equals(other.ethnicity))
      {return false;}
    if (gender == null) {
      if (other.gender != null)
        {return false;}
    } else if (!gender.equals(other.gender))
      {return false;}
    if (maritalStatus == null) {
      if (other.maritalStatus != null)
        {return false;}
    } else if (!maritalStatus.equals(other.maritalStatus))
      {return false;}
    if (patientId == null) {
      if (other.patientId != null)
        {return false;}
    } else if (!patientId.equals(other.patientId))
      {return false;}
    if (postalCode == null) {
      if (other.postalCode != null)
        {return false;}
    } else if (!postalCode.equals(other.postalCode))
      {return false;}
    if (primaryPhone == null) {
      if (other.primaryPhone != null)
        {return false;}
    } else if (!primaryPhone.equals(other.primaryPhone))
      {return false;}
    if (profileImagePath == null) {
      if (other.profileImagePath != null)
        {return false;}
    } else if (!profileImagePath.equals(other.profileImagePath))
      {return false;}
    if (race == null) {
      if (other.race != null)
        {return false;}
    } else if (!race.equals(other.race))
      {return false;}
    if (schoolName == null) {
      if (other.schoolName != null)
        {return false;}
    } else if (!schoolName.equals(other.schoolName))
      {return false;}
    if (schoolStatus == null) {
      if (other.schoolStatus != null)
        {return false;}
    } else if (!schoolStatus.equals(other.schoolStatus))
      {return false;}
    if (secondaryPhone == null) {
      if (other.secondaryPhone != null)
        {return false;}
    } else if (!secondaryPhone.equals(other.secondaryPhone))
      {return false;}
    if (streetAddress1 == null) {
      if (other.streetAddress1 != null)
        {return false;}
    } else if (!streetAddress1.equals(other.streetAddress1))
      {return false;}
    if (streetAddress2 == null) {
      if (other.streetAddress2 != null)
        {return false;}
    } else if (!streetAddress2.equals(other.streetAddress2))
      {return false;}
    if (usState == null) {
      if (other.usState != null)
        {return false;}
    } else if (!usState.equals(other.usState))
      {return false;}
    return true;
  }

  @Override
  public String toString() {
    return "Demographics [patientId=" + patientId + ", primaryPhone="
        + primaryPhone + ", secondaryPhone=" + secondaryPhone
        + ", streetAddress1=" + streetAddress1 + ", streetAddress2="
        + streetAddress2 + ", city=" + city + ", usState=" + usState
        + ", postalCode=" + postalCode + ", country=" + country
        + ", ethnicity=" + ethnicity + ", race=" + race + ", gender="
        + gender + ", maritalStatus=" + maritalStatus
        + ", employmentStatus=" + employmentStatus + ", employer="
        + employer + ", schoolStatus=" + schoolStatus + ", schoolName="
        + schoolName + ", dob=" + dob
        + ", profileImagePath=" + profileImagePath + "]";
  }

}
