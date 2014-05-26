/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

package com.wdeanmedical.ehr.entity.dto;

import java.util.Date;

public class DemographicsDTO {

  private Integer patientId;
  private String primaryPhone;
  private String secondaryPhone;
  private String streetAddress1;
  private String streetAddress2;
  private String city;
  private USStateDTO usStateDTO;
  private String postalCode;
  private CountryDTO countryDTO;
  private EthnicityDTO ethnicityDTO;
  private RaceDTO raceDTO;
  private GenderDTO genderDTO;
  private MaritalStatusDTO maritalStatusDTO;
  private String employmentStatus;
  private String employer;
  private String schoolStatus;
  private String schoolName;
  private String region;
  private Date dob;
  private String profileImagePath;

  public DemographicsDTO() {
  }

  public Integer getPatientId() {
    return patientId;
  }

  public void setPatientId(Integer patientId) {
    this.patientId = patientId;
  }

  public String getProfileImagePath() {
    return profileImagePath;
  }

  public void setProfileImagePath(String profileImagePath) {
    this.profileImagePath = profileImagePath;
  }

  public Date getDob() {
    return dob;
  }

  public void setDob(Date dob) {
    this.dob = dob;
  }

  public String getEmploymentStatus() {
    return employmentStatus;
  }

  public void setEmploymentStatus(String employmentStatus) {
    this.employmentStatus = employmentStatus;
  }

  public String getSchoolStatus() {
    return schoolStatus;
  }

  public void setSchoolStatus(String schoolStatus) {
    this.schoolStatus = schoolStatus;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
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

  public String getStreetAddress1() {
    return streetAddress1;
  }

  public void setStreetAddress1(String streetAddress1) {
    this.streetAddress1 = streetAddress1;
  }

  public String getStreetAddress2() {
    return streetAddress2;
  }

  public void setStreetAddress2(String streetAddress2) {
    this.streetAddress2 = streetAddress2;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public USStateDTO getUsStateDTO() {
    return usStateDTO;
  }

  public void setUsStateDTO(USStateDTO usStateDTO) {
    this.usStateDTO = usStateDTO;
  }

  public CountryDTO getCountryDTO() {
    return countryDTO;
  }

  public void setCountryDTO(CountryDTO countryDTO) {
    this.countryDTO = countryDTO;
  }

  public String getEmployer() {
    return employer;
  }

  public void setEmployer(String employer) {
    this.employer = employer;
  }

  public String getSchoolName() {
    return schoolName;
  }

  public void setSchoolName(String schoolName) {
    this.schoolName = schoolName;
  }

  public EthnicityDTO getEthnicityDTO() {
    return ethnicityDTO;
  }

  public void setEthnicityDTO(EthnicityDTO ethnicityDTO) {
    this.ethnicityDTO = ethnicityDTO;
  }

  public RaceDTO getRaceDTO() {
    return raceDTO;
  }

  public void setRaceDTO(RaceDTO raceDTO) {
    this.raceDTO = raceDTO;
  }

  public GenderDTO getGenderDTO() {
    return genderDTO;
  }

  public void setGenderDTO(GenderDTO genderDTO) {
    this.genderDTO = genderDTO;
  }

  public MaritalStatusDTO getMaritalStatusDTO() {
    return maritalStatusDTO;
  }

  public void setMaritalStatusDTO(MaritalStatusDTO maritalStatusDTO) {
    this.maritalStatusDTO = maritalStatusDTO;
  }

  @Override
  public String toString() {
    return "DemographicsDTO [patientId=" + patientId + ", primaryPhone=" + primaryPhone + ", secondaryPhone="
        + secondaryPhone + ", streetAddress1=" + streetAddress1 + ", streetAddress2=" + streetAddress2 + ", city="
        + city + ", usStateDTO=" + usStateDTO + ", postalCode=" + postalCode + ", countryDTO=" + countryDTO
        + ", ethnicityDTO=" + ethnicityDTO + ", raceDTO=" + raceDTO + ", genderDTO=" + genderDTO
        + ", maritalStatusDTO=" + maritalStatusDTO + ", employmentStatus=" + employmentStatus + ", employer="
        + employer + ", schoolStatus=" + schoolStatus + ", schoolName=" + schoolName + ", region=" + region + ", dob="
        + dob + ", profileImagePath=" + profileImagePath + "]";
  }

}
