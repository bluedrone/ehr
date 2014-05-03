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
  private String employer;
  private String schoolStatus;
  private String schoolName;
  private String region;
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
  
  @Column(name = "region")
  public String getRegion() { return region; }
  public void setRegion(String region) { this.region = region; }

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
   
}
