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
@Table(name = "medical_history")
public class MedicalHistory extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 795401034269323351L;
  
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
  private float smokePksDay; 
  private float yearsSmoked; 
  private float smokeYearsQuit; 
  private float etohUnitsWeek; 
  private String currentDrugs;
  private List<IntakeMedication> intakeMedicationList;

  public MedicalHistory() {
  }
  
  @Column(name = "patient_id")
  public Integer getPatientId() { return patientId; }
  public void setPatientId(Integer patientId) { this.patientId = patientId; }

  @Column(name = "date")
  public Date getDate() { return date; }
  public void setDate(Date date) { this.date = date; }

  @Column(name = "past_sm")
  public String getPastSM() { return pastSM; }
  public void setPastSM(String pastSM) { this.pastSM = pastSM; }

  @Column(name = "fam_hist")
  public String getFamHist() { return famHist; }
  public void setFamHist(String famHist) { this.famHist = famHist; }

  @Column(name = "fam_hist_other")
  public String getFamHistOther() { return famHistOther; }
  public void setFamHistOther(String famHistOther) { this.famHistOther = famHistOther; }

  @Column(name = "fam_hist_notes")
  public String getFamHistNotes() { return famHistNotes; }
  public void setFamHistNotes(String famHistNotes) { this.famHistNotes = famHistNotes; }

  @Column(name = "allerg_food")
  public String getAllergFood() { return allergFood; }
  public void setAllergFood(String allergFood) { this.allergFood = allergFood; }

  @Column(name = "allerg_drug")
  public String getAllergDrug() { return allergDrug; }
  public void setAllergDrug(String allergDrug) { this.allergDrug = allergDrug; }

  @Column(name = "allerg_evn")
  public String getAllergEnv() { return allergEnv; }
  public void setAllergEnv(String allergEnv) { this.allergEnv = allergEnv; }

  @Column(name = "vacc")
  public String getVacc() { return vacc; }
  public void setVacc(String vacc) { this.vacc = vacc; }

  @Column(name = "vacc_notes")
  public String getVaccNotes() { return vaccNotes; }
  public void setVaccNotes(String vaccNotes) { this.vaccNotes = vaccNotes; }

  @Column(name = "subst")
  public String getSubst() { return subst; }
  public void setSubst(String subst) { this.subst = subst; }

  @Column(name = "smoke_pks_day")
  public float getSmokePksDay() { return smokePksDay; }
  public void setSmokePksDay(float smokePksDay) { this.smokePksDay = smokePksDay; }

  @Column(name = "years_smoked")
  public float getYearsSmoked() { return yearsSmoked; }
  public void setYearsSmoked(float yearsSmoked) { this.yearsSmoked = yearsSmoked; }

  @Column(name = "smoke_years_quit")
  public float getSmokeYearsQuit() { return smokeYearsQuit; }
  public void setSmokeYearsQuit(float smokeYearsQuit) { this.smokeYearsQuit = smokeYearsQuit; }

  @Column(name = "etoh_units_week")
  public float getEtohUnitsWeek() { return etohUnitsWeek; }
  public void setEtohUnitsWeek(float etohUnitsWeek) { this.etohUnitsWeek = etohUnitsWeek; }

  @Column(name = "current_drugs")
  public String getCurrentDrugs() { return currentDrugs; }
  public void setCurrentDrugs(String currentDrugs) { this.currentDrugs = currentDrugs; }

  @Transient
  public List<IntakeMedication> getIntakeMedicationList() { return intakeMedicationList; }
  public void setIntakeMedicationList(List<IntakeMedication> intakeMedicationList) { this.intakeMedicationList = intakeMedicationList; }

}
