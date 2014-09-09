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
@Table(name = "pfsh")
public class PFSH extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -8340318118443065374L;

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
  private Boolean saved = false;

  public PFSH() {
  }

  @Column(name = "patient_id")
  public Integer getPatientId() { return patientId; }
  public void setPatientId(Integer patientId) { this.patientId = patientId; }

  @Column(name = "clinician_id")
  public Integer getClinicianId() { return clinicianId; }
  public void setClinicianId(Integer clinicianId) { this.clinicianId = clinicianId; }

  @Column(name = "date")
  public Date getDate() { return date; }
  public void setDate(Date date) { this.date = date; }

  @Column(name = "mother_name")
  public String getMotherName() { return motherName; }
  public void setMotherName(String motherName) { this.motherName = motherName; }

  @Column(name = "mother_dob")
  public Date getMotherDOB() { return motherDOB; }
  public void setMotherDOB(Date motherDOB) { this.motherDOB = motherDOB; }

  @Column(name = "caretaker_name")
  public String getCaretakerName() { return caretakerName; }
  public void setCaretakerName(String caretakerName) { this.caretakerName = caretakerName; }

  @Column(name = "caretaker_relationship")
  public String getCaretakerRelationship() { return caretakerRelationship; }
  public void setCaretakerRelationship(String caretakerRelationship) { this.caretakerRelationship = caretakerRelationship; }

  @Column(name = "num_residents")
  public Integer getNumResidents() { return numResidents; }
  public void setNumResidents(Integer numResidents) { this.numResidents = numResidents; }

  @Column(name = "job_type")
  public String getJobType() { return jobType; }
  public void setJobType(String jobType) { this.jobType = jobType; }

  @Column(name = "mother_alive")
  public Boolean isMotherAlive() { return motherAlive; }
  public void setMotherAlive(Boolean motherAlive) { this.motherAlive = motherAlive; }

  @Column(name = "mother_death_reason")
  public String getMotherDeathReason() { return motherDeathReason; }
  public void setMotherDeathReason(String motherDeathReason) { this.motherDeathReason = motherDeathReason; }

  @Column(name = "father_alive")
  public Boolean isFatherAlive() { return fatherAlive; }
  public void setFatherAlive(Boolean fatherAlive) { this.fatherAlive = fatherAlive; }

  @Column(name = "father_death_reason")
  public String getFatherDeathReason() { return fatherDeathReason; }
  public void setFatherDeathReason(String fatherDeathReason) { this.fatherDeathReason = fatherDeathReason; }

  @Column(name = "partner_alive")
  public Boolean isPartnerAlive() { return partnerAlive; }
  public void setPartnerAlive(Boolean partnerAlive) { this.partnerAlive = partnerAlive; }

  @Column(name = "partner_death_reason")
  public String getPartnerDeathReason() { return partnerDeathReason; }
  public void setPartnerDeathReason(String partnerDeathReason) { this.partnerDeathReason = partnerDeathReason; }

  @Column(name = "num_siblings")
  public Integer getNumSiblings() { return numSiblings; }
  public void setNumSiblings(Integer numSiblings) { this.numSiblings = numSiblings; }

  @Column(name = "num_brothers")
  public Integer getNumBrothers() { return numBrothers; }
  public void setNumBrothers(Integer numBrothers) { this.numBrothers = numBrothers; }

  @Column(name = "num_sisters")
  public Integer getNumSisters() { return numSisters; }
  public void setNumSisters(Integer numSisters) { this.numSisters = numSisters; }

  @Column(name = "num_children")
  public Integer getNumChildren() { return numChildren; }
  public void setNumChildren(Integer numChildren) { this.numChildren = numChildren; }

  @Column(name = "num_sons")
  public Integer getNumSons() { return numSons; }
  public void setNumSons(Integer numSons) { this.numSons = numSons; }

  @Column(name = "num_daughters")
  public Integer getNumDaughters() { return numDaughters; }
  public void setNumDaughters(Integer numDaughters) { this.numDaughters = numDaughters; }
  
  @Column(name = "saved")
  public Boolean getSaved() { return saved; }
  public void setSaved(Boolean saved) { this.saved = saved; }  
  

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result
        + ((caretakerName == null) ? 0 : caretakerName.hashCode());
    result = prime
        * result
        + ((caretakerRelationship == null) ? 0 : caretakerRelationship
            .hashCode());
    result = prime * result
        + ((clinicianId == null) ? 0 : clinicianId.hashCode());
    result = prime * result + ((date == null) ? 0 : date.hashCode());
    result = prime * result
        + ((fatherAlive == null) ? 0 : fatherAlive.hashCode());
    result = prime
        * result
        + ((fatherDeathReason == null) ? 0 : fatherDeathReason
            .hashCode());
    result = prime * result + ((jobType == null) ? 0 : jobType.hashCode());
    result = prime * result
        + ((motherAlive == null) ? 0 : motherAlive.hashCode());
    result = prime * result
        + ((motherDOB == null) ? 0 : motherDOB.hashCode());
    result = prime
        * result
        + ((motherDeathReason == null) ? 0 : motherDeathReason
            .hashCode());
    result = prime * result
        + ((motherName == null) ? 0 : motherName.hashCode());
    result = prime * result
        + ((numBrothers == null) ? 0 : numBrothers.hashCode());
    result = prime * result
        + ((numChildren == null) ? 0 : numChildren.hashCode());
    result = prime * result
        + ((numDaughters == null) ? 0 : numDaughters.hashCode());
    result = prime * result
        + ((numResidents == null) ? 0 : numResidents.hashCode());
    result = prime * result
        + ((numSiblings == null) ? 0 : numSiblings.hashCode());
    result = prime * result
        + ((numSisters == null) ? 0 : numSisters.hashCode());
    result = prime * result + ((numSons == null) ? 0 : numSons.hashCode());
    result = prime * result
        + ((partnerAlive == null) ? 0 : partnerAlive.hashCode());
    result = prime
        * result
        + ((partnerDeathReason == null) ? 0 : partnerDeathReason
            .hashCode());
    result = prime * result
        + ((patientId == null) ? 0 : patientId.hashCode());
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
    PFSH other = (PFSH) obj;
    if (caretakerName == null) {
      if (other.caretakerName != null)
        {return false;}
    } else if (!caretakerName.equals(other.caretakerName))
      {return false;}
    if (caretakerRelationship == null) {
      if (other.caretakerRelationship != null)
        {return false;}
    } else if (!caretakerRelationship.equals(other.caretakerRelationship))
      {return false;}
    if (clinicianId == null) {
      if (other.clinicianId != null)
        {return false;}
    } else if (!clinicianId.equals(other.clinicianId))
      {return false;}
    if (date == null) {
      if (other.date != null)
        {return false;}
    } else if (!date.equals(other.date))
      {return false;}
    if (fatherAlive == null) {
      if (other.fatherAlive != null)
        {return false;}
    } else if (!fatherAlive.equals(other.fatherAlive))
      {return false;}
    if (fatherDeathReason == null) {
      if (other.fatherDeathReason != null)
        {return false;}
    } else if (!fatherDeathReason.equals(other.fatherDeathReason))
      {return false;}
    if (jobType == null) {
      if (other.jobType != null)
        {return false;}
    } else if (!jobType.equals(other.jobType))
      {return false;}
    if (motherAlive == null) {
      if (other.motherAlive != null)
        {return false;}
    } else if (!motherAlive.equals(other.motherAlive))
      {return false;}
    if (motherDOB == null) {
      if (other.motherDOB != null)
        {return false;}
    } else if (!motherDOB.equals(other.motherDOB))
      {return false;}
    if (motherDeathReason == null) {
      if (other.motherDeathReason != null)
        {return false;}
    } else if (!motherDeathReason.equals(other.motherDeathReason))
      {return false;}
    if (motherName == null) {
      if (other.motherName != null)
        {return false;}
    } else if (!motherName.equals(other.motherName))
      {return false;}
    if (numBrothers == null) {
      if (other.numBrothers != null)
        {return false;}
    } else if (!numBrothers.equals(other.numBrothers))
      {return false;}
    if (numChildren == null) {
      if (other.numChildren != null)
        {return false;}
    } else if (!numChildren.equals(other.numChildren))
      {return false;}
    if (numDaughters == null) {
      if (other.numDaughters != null)
        {return false;}
    } else if (!numDaughters.equals(other.numDaughters))
      {return false;}
    if (numResidents == null) {
      if (other.numResidents != null)
        {return false;}
    } else if (!numResidents.equals(other.numResidents))
      {return false;}
    if (numSiblings == null) {
      if (other.numSiblings != null)
        {return false;}
    } else if (!numSiblings.equals(other.numSiblings))
      {return false;}
    if (numSisters == null) {
      if (other.numSisters != null)
        {return false;}
    } else if (!numSisters.equals(other.numSisters))
      {return false;}
    if (numSons == null) {
      if (other.numSons != null)
        {return false;}
    } else if (!numSons.equals(other.numSons))
      {return false;}
    if (partnerAlive == null) {
      if (other.partnerAlive != null)
        {return false;}
    } else if (!partnerAlive.equals(other.partnerAlive))
      {return false;}
    if (partnerDeathReason == null) {
      if (other.partnerDeathReason != null)
        {return false;}
    } else if (!partnerDeathReason.equals(other.partnerDeathReason))
      {return false;}
    if (patientId == null) {
      if (other.patientId != null)
        {return false;}
    } else if (!patientId.equals(other.patientId))
      {return false;}
    return true;
  }

  @Override
  public String toString() {
    return "PFSH [patientId=" + patientId + ", clinicianId=" + clinicianId
        + ", date=" + date + ", motherName=" + motherName
        + ", motherDOB=" + motherDOB + ", caretakerName="
        + caretakerName + ", caretakerRelationship="
        + caretakerRelationship + ", numResidents=" + numResidents
        + ", jobType=" + jobType + ", motherAlive=" + motherAlive
        + ", motherDeathReason=" + motherDeathReason + ", fatherAlive="
        + fatherAlive + ", fatherDeathReason=" + fatherDeathReason
        + ", partnerAlive=" + partnerAlive + ", partnerDeathReason="
        + partnerDeathReason + ", numSiblings=" + numSiblings
        + ", numBrothers=" + numBrothers + ", numSisters=" + numSisters
        + ", numChildren=" + numChildren + ", numSons=" + numSons
        + ", numDaughters=" + numDaughters + "]";
  }

}
