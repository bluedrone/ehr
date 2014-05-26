/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

package com.wdeanmedical.ehr.entity.dto;

import java.util.Date;
import java.util.List;

public class MedicalHistoryDTO {

  private Integer patientId;
  private Date date;
  private String pastSM;
  private String famHist;
  private String famHistOther;
  private String famHistNotes;
  private String allergFood;
  private String allergDrug;
  private String allergEnv;
  private String vacc;
  private String vaccNotes;
  private String subst;
  private Float smokePksDay;
  private Float yearsSmoked;
  private Float smokeYearsQuit;
  private Float etohUnitsWeek;
  private String currentDrugs;
  private List<EncounterMedicationDTO> encounterMedicationDTOList;

  public MedicalHistoryDTO() {
  }

  public Integer getPatientId() {
    return patientId;
  }

  public void setPatientId(Integer patientId) {
    this.patientId = patientId;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getPastSM() {
    return pastSM;
  }

  public void setPastSM(String pastSM) {
    this.pastSM = pastSM;
  }

  public String getFamHist() {
    return famHist;
  }

  public void setFamHist(String famHist) {
    this.famHist = famHist;
  }

  public String getFamHistOther() {
    return famHistOther;
  }

  public void setFamHistOther(String famHistOther) {
    this.famHistOther = famHistOther;
  }

  public String getFamHistNotes() {
    return famHistNotes;
  }

  public void setFamHistNotes(String famHistNotes) {
    this.famHistNotes = famHistNotes;
  }

  public String getAllergFood() {
    return allergFood;
  }

  public void setAllergFood(String allergFood) {
    this.allergFood = allergFood;
  }

  public String getAllergDrug() {
    return allergDrug;
  }

  public void setAllergDrug(String allergDrug) {
    this.allergDrug = allergDrug;
  }

  public String getAllergEnv() {
    return allergEnv;
  }

  public void setAllergEnv(String allergEnv) {
    this.allergEnv = allergEnv;
  }

  public String getVacc() {
    return vacc;
  }

  public void setVacc(String vacc) {
    this.vacc = vacc;
  }

  public String getVaccNotes() {
    return vaccNotes;
  }

  public void setVaccNotes(String vaccNotes) {
    this.vaccNotes = vaccNotes;
  }

  public String getSubst() {
    return subst;
  }

  public void setSubst(String subst) {
    this.subst = subst;
  }

  public Float getSmokePksDay() {
    return smokePksDay;
  }

  public void setSmokePksDay(Float smokePksDay) {
    this.smokePksDay = smokePksDay;
  }

  public Float getYearsSmoked() {
    return yearsSmoked;
  }

  public void setYearsSmoked(Float yearsSmoked) {
    this.yearsSmoked = yearsSmoked;
  }

  public Float getSmokeYearsQuit() {
    return smokeYearsQuit;
  }

  public void setSmokeYearsQuit(Float smokeYearsQuit) {
    this.smokeYearsQuit = smokeYearsQuit;
  }

  public Float getEtohUnitsWeek() {
    return etohUnitsWeek;
  }

  public void setEtohUnitsWeek(Float etohUnitsWeek) {
    this.etohUnitsWeek = etohUnitsWeek;
  }

  public String getCurrentDrugs() {
    return currentDrugs;
  }

  public void setCurrentDrugs(String currentDrugs) {
    this.currentDrugs = currentDrugs;
  }

  public List<EncounterMedicationDTO> getEncounterMedicationDTOList() {
    return encounterMedicationDTOList;
  }

  public void setEncounterMedicationDTOList(List<EncounterMedicationDTO> encounterMedicationDTOList) {
    this.encounterMedicationDTOList = encounterMedicationDTOList;
  }

  @Override
  public String toString() {
    return "MedicalHistoryDTO [patientId=" + patientId + ", date=" + date + ", pastSM=" + pastSM + ", famHist="
        + famHist + ", famHistOther=" + famHistOther + ", famHistNotes=" + famHistNotes + ", allergFood=" + allergFood
        + ", allergDrug=" + allergDrug + ", allergEnv=" + allergEnv + ", vacc=" + vacc + ", vaccNotes=" + vaccNotes
        + ", subst=" + subst + ", smokePksDay=" + smokePksDay + ", yearsSmoked=" + yearsSmoked + ", smokeYearsQuit="
        + smokeYearsQuit + ", etohUnitsWeek=" + etohUnitsWeek + ", currentDrugs=" + currentDrugs
        + ", encounterMedicationDTOList=" + encounterMedicationDTOList + "]";
  }

}
