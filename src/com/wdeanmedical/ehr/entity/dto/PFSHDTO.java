/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

package com.wdeanmedical.ehr.entity.dto;

import java.util.Date;

public class PFSHDTO {

  private Integer patientId;
  private Integer clinicianId;
  private Date date;
  private String motherName;
  private Date motherDOB;
  private String caretakerName;
  private String caretakerRelationship;
  private Integer numResidents;
  private String jobType;
  private Boolean motherAlive;
  private String motherDeathReason;
  private Boolean fatherAlive;
  private String fatherDeathReason;
  private Boolean partnerAlive;
  private String partnerDeathReason;
  private Integer numSiblings;
  private Integer numBrothers;
  private Integer numSisters;
  private Integer numChildren;
  private Integer numSons;
  private Integer numDaughters;

  public PFSHDTO() {
  }

  public Integer getPatientId() {
    return patientId;
  }

  public void setPatientId(Integer patientId) {
    this.patientId = patientId;
  }

  public Integer getClinicianId() {
    return clinicianId;
  }

  public void setClinicianId(Integer clinicianId) {
    this.clinicianId = clinicianId;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getMotherName() {
    return motherName;
  }

  public void setMotherName(String motherName) {
    this.motherName = motherName;
  }

  public Date getMotherDOB() {
    return motherDOB;
  }

  public void setMotherDOB(Date motherDOB) {
    this.motherDOB = motherDOB;
  }

  public String getCaretakerName() {
    return caretakerName;
  }

  public void setCaretakerName(String caretakerName) {
    this.caretakerName = caretakerName;
  }

  public String getCaretakerRelationship() {
    return caretakerRelationship;
  }

  public void setCaretakerRelationship(String caretakerRelationship) {
    this.caretakerRelationship = caretakerRelationship;
  }

  public Integer getNumResidents() {
    return numResidents;
  }

  public void setNumResidents(Integer numResidents) {
    this.numResidents = numResidents;
  }

  public String getJobType() {
    return jobType;
  }

  public void setJobType(String jobType) {
    this.jobType = jobType;
  }

  public Boolean isMotherAlive() {
    return motherAlive;
  }

  public void setMotherAlive(Boolean motherAlive) {
    this.motherAlive = motherAlive;
  }

  public String getMotherDeathReason() {
    return motherDeathReason;
  }

  public void setMotherDeathReason(String motherDeathReason) {
    this.motherDeathReason = motherDeathReason;
  }

  public Boolean isFatherAlive() {
    return fatherAlive;
  }

  public void setFatherAlive(Boolean fatherAlive) {
    this.fatherAlive = fatherAlive;
  }

  public String getFatherDeathReason() {
    return fatherDeathReason;
  }

  public void setFatherDeathReason(String fatherDeathReason) {
    this.fatherDeathReason = fatherDeathReason;
  }

  public Boolean isPartnerAlive() {
    return partnerAlive;
  }

  public void setPartnerAlive(Boolean partnerAlive) {
    this.partnerAlive = partnerAlive;
  }

  public String getPartnerDeathReason() {
    return partnerDeathReason;
  }

  public void setPartnerDeathReason(String partnerDeathReason) {
    this.partnerDeathReason = partnerDeathReason;
  }

  public Integer getNumSiblings() {
    return numSiblings;
  }

  public void setNumSiblings(Integer numSiblings) {
    this.numSiblings = numSiblings;
  }

  public Integer getNumBrothers() {
    return numBrothers;
  }

  public void setNumBrothers(Integer numBrothers) {
    this.numBrothers = numBrothers;
  }

  public Integer getNumSisters() {
    return numSisters;
  }

  public void setNumSisters(Integer numSisters) {
    this.numSisters = numSisters;
  }

  public Integer getNumChildren() {
    return numChildren;
  }

  public void setNumChildren(Integer numChildren) {
    this.numChildren = numChildren;
  }

  public Integer getNumSons() {
    return numSons;
  }

  public void setNumSons(Integer numSons) {
    this.numSons = numSons;
  }

  public Integer getNumDaughters() {
    return numDaughters;
  }

  public void setNumDaughters(Integer numDaughters) {
    this.numDaughters = numDaughters;
  }

  @Override
  public String toString() {
    return "PFSHDTO [patientId=" + patientId + ", clinicianId=" + clinicianId + ", date=" + date + ", motherName="
        + motherName + ", motherDOB=" + motherDOB + ", caretakerName=" + caretakerName + ", caretakerRelationship="
        + caretakerRelationship + ", numResidents=" + numResidents + ", jobType=" + jobType + ", motherAlive="
        + motherAlive + ", motherDeathReason=" + motherDeathReason + ", fatherAlive=" + fatherAlive
        + ", fatherDeathReason=" + fatherDeathReason + ", partnerAlive=" + partnerAlive + ", partnerDeathReason="
        + partnerDeathReason + ", numSiblings=" + numSiblings + ", numBrothers=" + numBrothers + ", numSisters="
        + numSisters + ", numChildren=" + numChildren + ", numSons=" + numSons + ", numDaughters=" + numDaughters + "]";
  }

}
