/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */
 
package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "patient_supp_questions")
public class SuppQuestions extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -7215757912004945158L;

  private Integer encounterId;
  private Integer patientId;
  private Integer clinicianId;
  private Date date;
  private Integer numCupsWater;
  private Integer numCupsCoffee;
  private Integer numCupsTea;
  private String waterSource;
  private List<EncounterQuestion> encounterQuestionList;

  public SuppQuestions() {
  }

  @Column(name = "encounter_id")
  public Integer getEncounterId() {
    return encounterId;
  }

  public void setEncounterId(Integer encounterId) {
    this.encounterId = encounterId;
  }

  @Column(name = "patient_id")
  public Integer getPatientId() {
    return patientId;
  }

  public void setPatientId(Integer patientId) {
    this.patientId = patientId;
  }

  @Column(name = "clinician_id")
  public Integer getClinicianId() {
    return clinicianId;
  }

  public void setClinicianId(Integer clinicianId) {
    this.clinicianId = clinicianId;
  }

  @Column(name = "date")
  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  @Column(name = "num_cups_water")
  public Integer getNumCupsWater() {
    return numCupsWater;
  }

  public void setNumCupsWater(Integer numCupsWater) {
    this.numCupsWater = numCupsWater;
  }

  @Column(name = "num_cups_coffee")
  public Integer getNumCupsCoffee() {
    return numCupsCoffee;
  }

  public void setNumCupsCoffee(Integer numCupsCoffee) {
    this.numCupsCoffee = numCupsCoffee;
  }

  @Column(name = "num_cups_tea")
  public Integer getNumCupsTea() {
    return numCupsTea;
  }

  public void setNumCupsTea(Integer numCupsTea) {
    this.numCupsTea = numCupsTea;
  }

  @Column(name = "water_source")
  public String getWaterSource() {
    return waterSource;
  }

  public void setWaterSource(String waterSource) {
    this.waterSource = waterSource;
  }

  @Transient
  public List<EncounterQuestion> getEncounterQuestionList() {
    return encounterQuestionList;
  }

  public void setEncounterQuestionList(
      List<EncounterQuestion> encounterQuestionList) {
    this.encounterQuestionList = encounterQuestionList;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result
        + ((clinicianId == null) ? 0 : clinicianId.hashCode());
    result = prime * result + ((date == null) ? 0 : date.hashCode());
    result = prime * result
        + ((encounterId == null) ? 0 : encounterId.hashCode());
    result = prime
        * result
        + ((encounterQuestionList == null) ? 0 : encounterQuestionList
            .hashCode());
    result = prime * result
        + ((numCupsCoffee == null) ? 0 : numCupsCoffee.hashCode());
    result = prime * result
        + ((numCupsTea == null) ? 0 : numCupsTea.hashCode());
    result = prime * result
        + ((numCupsWater == null) ? 0 : numCupsWater.hashCode());
    result = prime * result
        + ((patientId == null) ? 0 : patientId.hashCode());
    result = prime * result
        + ((waterSource == null) ? 0 : waterSource.hashCode());
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
    SuppQuestions other = (SuppQuestions) obj;
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
    if (encounterId == null) {
      if (other.encounterId != null)
        {return false;}
    } else if (!encounterId.equals(other.encounterId))
      {return false;}
    if (encounterQuestionList == null) {
      if (other.encounterQuestionList != null)
        {return false;}
    } else if (!encounterQuestionList.equals(other.encounterQuestionList))
      {return false;}
    if (numCupsCoffee == null) {
      if (other.numCupsCoffee != null)
        {return false;}
    } else if (!numCupsCoffee.equals(other.numCupsCoffee))
      {return false;}
    if (numCupsTea == null) {
      if (other.numCupsTea != null)
        {return false;}
    } else if (!numCupsTea.equals(other.numCupsTea))
      {return false;}
    if (numCupsWater == null) {
      if (other.numCupsWater != null)
        {return false;}
    } else if (!numCupsWater.equals(other.numCupsWater))
      {return false;}
    if (patientId == null) {
      if (other.patientId != null)
        {return false;}
    } else if (!patientId.equals(other.patientId))
      {return false;}
    if (waterSource == null) {
      if (other.waterSource != null)
        {return false;}
    } else if (!waterSource.equals(other.waterSource))
      {return false;}
    return true;
  }

  @Override
  public String toString() {
    return "SuppQuestions [encounterId=" + encounterId + ", patientId="
        + patientId + ", clinicianId=" + clinicianId + ", date=" + date
        + ", numCupsWater=" + numCupsWater + ", numCupsCoffee="
        + numCupsCoffee + ", numCupsTea=" + numCupsTea
        + ", waterSource=" + waterSource + ", encounterQuestionList="
        + encounterQuestionList + "]";
  }

}
