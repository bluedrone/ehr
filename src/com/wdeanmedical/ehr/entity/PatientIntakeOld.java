package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "patient_intake_old")
public class PatientIntakeOld extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 4560216979435814188L;
  
  public static final Integer LOCK_FREE = 0;
  public static final Integer LOCK_LOCKED = 1;
  public static final Integer LOCK_OVERRIDDEN = 2;
  
  private int patientId;
  private Clinician clinician;
  private String nombre;
  private String apellido;
  private String apellidoSegundo;
  private boolean bioFinger;
  private boolean bioPhoto;
  private Integer age;
  private Integer triage;
  private String notes;
  private String gender;
  private boolean checkIn;
  private boolean intake;
  private boolean provider;
  private boolean missing;
  private boolean completed;
  private int patientIntakeGroupId;
  private boolean basicInfoSaved; 
  private String consultLocation;
  private String govtId;
  private Date dob;
  private Integer ageInYears;
  private Integer ageInMonths;
  private String phone;
  private String community;
  private boolean vitalsSaved; 
  private String height;
  private String weight;
  private String sys;
  private String dia;
  private String hr;
  private String rr;
  private String temp;
  private String arm;
  private boolean familySaved; 
  private String motherName;
  private Date motherDob;
  private String caretakerName;
  private String patientRelationship;
  private boolean ccSaved; 
  private String chiefComplaint;
  private String specificLocation;
  private String occursWhen;
  private String hoursSince;
  private String daysSince;
  private String weeksSince;
  private String monthsSince;
  private String yearsSince;
  private String howLongOther;
  private String painXHour;
  private String painXDay;
  private String painXWeek;
  private String painXMonth;
  private String painDuration;
  private Integer painScale;
  private String painType;
  private String denies;
  private String deniesOther;
  private boolean obgynSaved; 
  private String obgynG;
  private String obgynP;
  private String obgynT;
  private String obgynA;
  private String obgynL;
  private String pregStatus;
  private String breastfeeding;
  private String breastfeedingMonths;
  private String lastPeriod;
  private String ageFirstPeriod;
  private String papSmearStatus;
  private String birthControlStatus;
  private String birthControlType;
  private String obgynHistory;
  private String obgynHistoryOther;
  private boolean pfshSaved; 
  private Integer numResidents; 
  private String jobType; 
  private boolean motherAlive; 
  private String motherDeathReason; 
  private boolean fatherAlive; 
  private String fatherDeathReason; 
  private boolean partnerAlive; 
  private String partnerDeathReason; 
  private Integer numSiblings; 
  private Integer numBrothers; 
  private Integer numSisters; 
  private Integer numChildren; 
  private Integer numSons; 
  private Integer numDaughters; 
  private boolean suppSaved; 
  private Integer numCupsWater; 
  private Integer numCupsCoffee; 
  private Integer numCupsTea; 
  private String waterSource; 
  private List<IntakeQuestion> intakeQuestionList;
  private boolean histSaved; 
  private List<IntakeMedication> intakeMedicationList;
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
  private String smokePksDay; 
  private String yearsSmoked; 
  private String smokeYearsQuit; 
  private String etohUnitsWeek; 
  private String currentDrugs; 
  private boolean examSaved; 
  private String hs; 
  private String heartRhythm; 
  private String hb; 
  private String glucose; 
  private String urineDIP; 
  private String diagnosis; 
  private String dxCode; 
  private String treatmentPlan; 
  private String txCode; 
  private String providerName; 
  private boolean followUpSaved; 
  private String followUpLevel; 
  private String followUpWhen; 
  private String followUpCondition; 
  private String followUpDispenseRx; 
  private String followUpUSS; 
  private String followUpPregnant; 
  private String followUpWoundCare; 
  private String followUpRefToSpecialist; 
  private String followUpDentalList; 
  private String followUpPhysiotherapy; 
  private String followUpBloodLabs; 
  private String followUpOther; 
  private String followUpPulmonaryFXTest; 
  private String followUpVision; 
  private boolean followUpCompleted; 
  private Date followUpDate; 
  private String followUpNotes; 
  private Integer lockStatus; 
  private Integer patientEncounterId;
  private Integer patientVitalsSignsId;
  private Integer patientPFSHId;
  private Integer OBGYNEncounterDataId;
  private Integer patientSuppQuestionsId;
  private Integer patientMedicalHistoryId;
  private Integer patientFollowUpId;
  private String profileImagePath; 
  
  public PatientIntakeOld() {
  }
  
  @Column(name = "patient_id")
  public int getPatientId() { return patientId; }
  public void setPatientId(int patientId) { this.patientId = patientId; }

  @JoinColumn(name = "clinician", referencedColumnName = "id")
  @ManyToOne(optional = true)
  public Clinician getClinician() { return clinician; }
  public void setClinician(Clinician clinician) { this.clinician = clinician; }
    
  @Column(name = "nombre")
  @Basic(optional = false)  
  public String getNombre() { return nombre; }
  public void setNombre(String nombre) { this.nombre = nombre; }

  @Column(name = "apellido")
  public String getApellido() { return apellido; }
  public void setApellido(String apellido) { this.apellido = apellido; }

  @Column(name = "apellido_segundo")
  public String getApellidoSegundo() { return apellidoSegundo; }
  public void setApellidoSegundo(String apellidoSegundo) { this.apellidoSegundo = apellidoSegundo; }
  
  @Column(name = "bio_finger")
  public boolean isBioFinger() { return bioFinger; }
  public void setBioFinger(boolean bioFinger) { this.bioFinger = bioFinger; }

  @Column(name = "bio_photo")
  public boolean isBioPhoto() { return bioPhoto; }
  public void setBioPhoto(boolean bioPhoto) { this.bioPhoto = bioPhoto; }
  
  @Column(name = "age")
  public Integer getAge() { return age; }
  public void setAge(Integer age) { this.age = age; }
  
  @Column(name = "triage")
  public Integer getTriage() { return triage; }
  public void setTriage(Integer triage) { this.triage = triage; }

  @Column(name = "notes", columnDefinition="text")
  public String getNotes() { return notes; }
  public void setNotes(String notes) { this.notes = notes; }
  
  @Column(name = "gender")
  public String getGender() { return gender; }
  public void setGender(String gender) { this.gender = gender; }

  @Column(name = "check_in")
  public boolean isCheckIn() { return checkIn; }
  public void setCheckIn(boolean checkIn) { this.checkIn = checkIn; }

  @Column(name = "intake")
  public boolean isIntake() { return intake; }
  public void setIntake(boolean intake) { this.intake = intake; }

  @Column(name = "provider")
  public boolean isProvider() { return provider; }
  public void setProvider(boolean provider) { this.provider = provider; }

  @Column(name = "missing")
  public boolean isMissing() { return missing; }
  public void setMissing(boolean missing) { this.missing = missing; }

  @Column(name = "completed")
  public boolean isCompleted() { return completed; }
  public void setCompleted(boolean completed) { this.completed = completed; }

  @Column(name = "patient_intake_group_id")
  public int getPatientIntakeGroupId() { return patientIntakeGroupId; }
  public void setPatientIntakeGroupId(int patientIntakeGroupId) { this.patientIntakeGroupId = patientIntakeGroupId; }
  
  @Column(name = "consult_location")
  public String getConsultLocation() { return consultLocation; }
  public void setConsultLocation(String consultLocation) { this.consultLocation = consultLocation; }

  @Column(name = "govt_id")
  public String getGovtId() { return govtId; }
  public void setGovtId(String govtId) { this.govtId = govtId; }

  @Column(name = "dob")
  public Date getDob() { return dob; }
  public void setDob(Date dob) { this.dob = dob; }

  @Column(name = "age_years")
  public Integer getAgeInYears() { return ageInYears; }
  public void setAgeInYears(Integer ageInYears) { this.ageInYears = ageInYears; }

  @Column(name = "age_months")
  public Integer getAgeInMonths() { return ageInMonths; }
  public void setAgeInMonths(Integer ageInMonths) { this.ageInMonths = ageInMonths; }

  @Column(name = "phone")
  public String getPhone() { return phone; }
  public void setPhone(String phone) { this.phone = phone; }

  @Column(name = "community")
  public String getCommunity() { return community; }
  public void setCommunity(String community) { this.community = community; }
  
  @Column(name = "basic_info_saved")
  public boolean isBasicInfoSaved() { return basicInfoSaved; }
  public void setBasicInfoSaved(boolean basicInfoSaved) { this.basicInfoSaved = basicInfoSaved; }
  
  @Column(name = "vitals_saved")
  public boolean isVitalsSaved() { return vitalsSaved; }
  public void setVitalsSaved(boolean vitalsSaved) { this.vitalsSaved = vitalsSaved; }
  
  @Column(name = "height")
  public String getHeight() { return height; }
  public void setHeight(String height) { this.height = height; }
  
  @Column(name = "weight")
  public String getWeight() { return weight; }
  public void setWeight(String weight) { this.weight = weight; }
  
  @Column(name = "sys")
  public String getSys() { return sys; }
  public void setSys(String sys) { this.sys = sys; }
  
  @Column(name = "dia")
  public String getDia() { return dia; }
  public void setDia(String dia) { this.dia = dia; }
  
  @Column(name = "hr")
  public String getHr() { return hr; }
  public void setHr(String hr) { this.hr = hr; }
  
  @Column(name = "rr")
  public String getRr() { return rr; }
  public void setRr(String rr) { this.rr = rr; }
  
  @Column(name = "temp")
  public String getTemp() { return temp; }
  public void setTemp(String temp) { this.temp = temp; }
  
  @Column(name = "arm")
  public String getArm() { return arm; }
  public void setArm(String arm) { this.arm = arm; }
  
  @Column(name = "family_saved")
  public boolean isFamilySaved() { return familySaved; }
  public void setFamilySaved(boolean familySaved) { this.familySaved = familySaved; }

  @Column(name = "mother_name")
  public String getMotherName() { return motherName; }
  public void setMotherName(String motherName) { this.motherName = motherName; }

  @Column(name = "mother_dob")
  public Date getMotherDob() { return motherDob; }
  public void setMotherDob(Date motherDob) { this.motherDob = motherDob; }

  @Column(name = "caretaker_name")
  public String getCaretakerName() { return caretakerName; }
  public void setCaretakerName(String caretakerName) { this.caretakerName = caretakerName; }

  @Column(name = "patient_relationship")
  public String getPatientRelationship() { return patientRelationship; }
  public void setPatientRelationship(String patientRelationship) { this.patientRelationship = patientRelationship; }
  
  @Column(name = "cc_saved")
  public boolean isCcSaved() { return ccSaved; }
  public void setCcSaved(boolean ccSaved) { this.ccSaved = ccSaved; }
  
  @Column(name = "chief_complaint")
  public String getChiefComplaint() { return chiefComplaint; }
  public void setChiefComplaint(String chiefComplaint) { this.chiefComplaint = chiefComplaint; }
  
  @Column(name = "specific_location")
  public String getSpecificLocation() { return specificLocation; }
  public void setSpecificLocation(String specificLocation) { this.specificLocation = specificLocation; }
  
  @Column(name = "hours_since")
  public String getHoursSince() { return hoursSince; }
  public void setHoursSince(String hoursSince) { this.hoursSince = hoursSince; }
  
  @Column(name = "day_since")
  public String getDaysSince() { return daysSince; }
  public void setDaysSince(String daysSince) { this.daysSince = daysSince; }
  
  @Column(name = "weeks_since")
  public String getWeeksSince() { return weeksSince; }
  public void setWeeksSince(String weeksSince) { this.weeksSince = weeksSince; }
  
  @Column(name = "months_since")
  public String getMonthsSince() { return monthsSince; }
  public void setMonthsSince(String monthsSince) { this.monthsSince = monthsSince; }
  
  @Column(name = "years_since")
  public String getYearsSince() { return yearsSince; }
  public void setYearsSince(String yearsSince) { this.yearsSince = yearsSince; }
  
  @Column(name = "how_long_other")
  public String getHowLongOther() { return howLongOther; }
  public void setHowLongOther(String howLongOther) { this.howLongOther = howLongOther; }

  @Column(name = "pain_x_hour")
  public String getPainXHour() { return painXHour; }
  public void setPainXHour(String painXHour) { this.painXHour = painXHour; }
  
  @Column(name = "pain_x_day")
  public String getPainXDay() { return painXDay; }
  public void setPainXDay(String painXDay) { this.painXDay = painXDay; }
  
  @Column(name = "pain_x_week")
  public String getPainXWeek() { return painXWeek; }
  public void setPainXWeek(String painXWeek) { this.painXWeek = painXWeek; }
  
  @Column(name = "pain_x_month")
  public String getPainXMonth() { return painXMonth; }
  public void setPainXMonth(String painXMonth) { this.painXMonth = painXMonth; }
  
  @Column(name = "pain_duration")
  public String getPainDuration() { return painDuration; }
  public void setPainDuration(String painDuration) { this.painDuration = painDuration; }
  
  @Column(name = "pain_scale")
  public Integer getPainScale() { return painScale; }
  public void setPainScale(Integer painScale) { this.painScale = painScale; }

  @Column(name = "pain_type")
  public String getPainType() { return painType; }
  public void setPainType(String painType) { this.painType = painType; }

  @Column(name = "denies")
  public String getDenies() { return denies; }
  public void setDenies(String denies) { this.denies = denies; }
  
  @Column(name = "denies_other")
  public String getDeniesOther() { return deniesOther; }
  public void setDeniesOther(String deniesOther) { this.deniesOther = deniesOther; }
  
  @Column(name = "occurs_when")
  public String getOccursWhen() { return occursWhen; }
  public void setOccursWhen(String occursWhen) { this.occursWhen = occursWhen; }
  
  @Column(name = "obgyn_saved")
  public boolean isObgynSaved() { return obgynSaved; }
  public void setObgynSaved(boolean obgynSaved) { this.obgynSaved = obgynSaved; }
  
  @Column(name = "obgyn_g")
  public String getObgynG() { return obgynG; }
  public void setObgynG(String obgynG) { this.obgynG = obgynG; }

  @Column(name = "obgyn_p")
  public String getObgynP() { return obgynP; }
  public void setObgynP(String obgynP) { this.obgynP = obgynP; }

  @Column(name = "obgyn_t")
  public String getObgynT() { return obgynT; }
  public void setObgynT(String obgynT) { this.obgynT = obgynT; }

  @Column(name = "obgyn_a")
  public String getObgynA() { return obgynA; }
  public void setObgynA(String obgynA) { this.obgynA = obgynA; }

  @Column(name = "obgyn_l")
  public String getObgynL() { return obgynL; }
  public void setObgynL(String obgynL) { this.obgynL = obgynL; }

  @Column(name = "preg_status")
  public String getPregStatus() { return pregStatus; }
  public void setPregStatus(String pregStatus) { this.pregStatus = pregStatus; }

  @Column(name = "breastfeeding")
  public String getBreastfeeding() { return breastfeeding; }
  public void setBreastfeeding(String breastfeeding) { this.breastfeeding = breastfeeding; }

  @Column(name = "breastfeedingMonths")
  public String getBreastfeedingMonths() { return breastfeedingMonths; }
  public void setBreastfeedingMonths(String breastfeedingMonths) { this.breastfeedingMonths = breastfeedingMonths; }

  @Column(name = "last_period")
  public String getLastPeriod() { return lastPeriod; }
  public void setLastPeriod(String lastPeriod) { this.lastPeriod = lastPeriod; }

  @Column(name = "age_first_period")
  public String getAgeFirstPeriod() { return ageFirstPeriod; }
  public void setAgeFirstPeriod(String ageFirstPeriod) { this.ageFirstPeriod = ageFirstPeriod; }

  @Column(name = "pap_smear_status")
  public String getPapSmearStatus() { return papSmearStatus; }
  public void setPapSmearStatus(String papSmearStatus) { this.papSmearStatus = papSmearStatus; }

  @Column(name = "birth_control_status")
  public String getBirthControlStatus() { return birthControlStatus; }
  public void setBirthControlStatus(String birthControlStatus) { this.birthControlStatus = birthControlStatus; }

  @Column(name = "birth_control_type")
  public String getBirthControlType() { return birthControlType; }
  public void setBirthControlType(String birthControlType) { this.birthControlType = birthControlType; }

  @Column(name = "obgyn_history")
  public String getObgynHistory() { return obgynHistory; }
  public void setObgynHistory(String obgynHistory) { this.obgynHistory = obgynHistory; }
  
  @Column(name = "obgyn_history_other")
  public String getObgynHistoryOther() { return obgynHistoryOther; }
  public void setObgynHistoryOther(String obgynHistoryOther) { this.obgynHistoryOther = obgynHistoryOther; }
  
  @Column(name = "pfsh_saved")
  public boolean isPfshSaved() { return pfshSaved; }
  public void setPfshSaved(boolean pfshSaved) { this.pfshSaved = pfshSaved; }
  
  @Column(name = "num_residents")
  public Integer getNumResidents() { return numResidents; }
  public void setNumResidents(Integer numResidents) { this.numResidents = numResidents; }
  
  @Column(name = "job_type")
  public String getJobType() { return jobType; }
  public void setJobType(String jobType) { this.jobType = jobType; }
  
  @Column(name = "mother_alive")
  public boolean isMotherAlive() { return motherAlive; }
  public void setMotherAlive(boolean motherAlive) { this.motherAlive = motherAlive; }

  @Column(name = "mother_death_reason")
  public String getMotherDeathReason() { return motherDeathReason; }
  public void setMotherDeathReason(String motherDeathReason) { this.motherDeathReason = motherDeathReason; }

  @Column(name = "father_alive")
  public boolean isFatherAlive() { return fatherAlive; }
  public void setFatherAlive(boolean fatherAlive) { this.fatherAlive = fatherAlive; }

  @Column(name = "father_death_reason")
  public String getFatherDeathReason() { return fatherDeathReason; }
  public void setFatherDeathReason(String fatherDeathReason) { this.fatherDeathReason = fatherDeathReason; }

  @Column(name = "partner_alive")
  public boolean isPartnerAlive() { return partnerAlive; }
  public void setPartnerAlive(boolean partnerAlive) { this.partnerAlive = partnerAlive; }

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

  @Column(name = "supp_saved")
  public boolean isSuppSaved() { return suppSaved; }
  public void setSuppSaved(boolean suppSaved) { this.suppSaved = suppSaved; }

  @Column(name = "num_cups_water")
  public Integer getNumCupsWater() { return numCupsWater; }
  public void setNumCupsWater(Integer numCupsWater) { this.numCupsWater = numCupsWater; }

  @Column(name = "num_cups_coffee")
  public Integer getNumCupsCoffee() { return numCupsCoffee; }
  public void setNumCupsCoffee(Integer numCupsCoffee) { this.numCupsCoffee = numCupsCoffee; }

  @Column(name = "num_cups_tea")
  public Integer getNumCupsTea() { return numCupsTea; }
  public void setNumCupsTea(Integer numCupsTea) { this.numCupsTea = numCupsTea; }

  @Column(name = "water_source")
  public String getWaterSource() { return waterSource; }
  public void setWaterSource(String waterSource) { this.waterSource = waterSource; }
  
  @Column(name = "hist_saved")
  public boolean isHistSaved() { return histSaved; }
  public void setHistSaved(boolean histSaved) { this.histSaved = histSaved; }
  
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
  
  @Column(name = "allerg_env")
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
  public String getSmokePksDay() { return smokePksDay; }
  public void setSmokePksDay(String smokePksDay) { this.smokePksDay = smokePksDay; }
  
  @Column(name = "years_smoked")
  public String getYearsSmoked() { return yearsSmoked; }
  public void setYearsSmoked(String yearsSmoked) { this.yearsSmoked = yearsSmoked; }
  
  @Column(name = "smoke_years_quit")
  public String getSmokeYearsQuit() { return smokeYearsQuit; }
  public void setSmokeYearsQuit(String smokeYearsQuit) { this.smokeYearsQuit = smokeYearsQuit; }
  
  @Column(name = "etoh_units_week")
  public String getEtohUnitsWeek() { return etohUnitsWeek; }
  public void setEtohUnitsWeek(String etohUnitsWeek) { this.etohUnitsWeek = etohUnitsWeek; }
  
  @Column(name = "current_drugs")
  public String getCurrentDrugs() { return currentDrugs; }
  public void setCurrentDrugs(String currentDrugs) { this.currentDrugs = currentDrugs; }
  
  @Column(name = "exam_saved")
  public boolean isExamSaved() { return examSaved; }
  public void setExamSaved(boolean examSaved) { this.examSaved = examSaved; }
  
  @Column(name = "hs")
  public String getHs() { return hs; }
  public void setHs(String hs) { this.hs = hs; }
  
  @Column(name = "heart_rhythm")
  public String getHeartRhythm() { return heartRhythm; }
  public void setHeartRhythm(String heartRhythm) { this.heartRhythm = heartRhythm; }
  
  @Column(name = "hb")
  public String getHb() { return hb; }
  public void setHb(String hb) { this.hb = hb  ; }
  
  @Column(name = "glucose")
  public String getGlucose() { return glucose; }
  public void setGlucose(String glucose) { this.glucose = glucose; }
  
  @Column(name = "urine_dip")
  public String getUrineDIP() { return urineDIP; }
  public void setUrineDIP(String urineDIP) { this.urineDIP = urineDIP; }
  
  @Column(name = "diagnosis")
  public String getDiagnosis() { return diagnosis; }
  public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
  
  @Column(name = "dx_code")
  public String getDxCode() { return dxCode; } 
  public void setDxCode(String dxCode) { this.dxCode = dxCode; }
  
  @Column(name = "treatment_plan")
  public String getTreatmentPlan() { return treatmentPlan; }
  public void setTreatmentPlan(String treatmentPlan) { this.treatmentPlan = treatmentPlan; }
  
  @Column(name = "tx_code")
  public String getTxCode() { return txCode; }
  public void setTxCode(String txCode) { this.txCode = txCode; }
  
  @Column(name = "provider_name")
  public String getProviderName() { return providerName; }
  public void setProviderName(String providerName) { this.providerName = providerName; }
  
  @Column(name = "follow_up_saved")
  public boolean isFollowUpSaved() { return followUpSaved; }
  public void setFollowUpSaved(boolean followUpSaved) { this.followUpSaved = followUpSaved; }
  
  @Column(name = "follow_up_level")
  public String getFollowUpLevel() { return followUpLevel; }
  public void setFollowUpLevel(String followUpLevel) { this.followUpLevel = followUpLevel; }
  
  @Column(name = "follow_up_when")
  public String getFollowUpWhen() { return followUpWhen; }
  public void setFollowUpWhen(String followUpWhen) { this.followUpWhen = followUpWhen; }
  
  @Column(name = "follow_up_condition")
  public String getFollowUpCondition() { return followUpCondition; }
  public void setFollowUpCondition(String followUpCondition) { this.followUpCondition = followUpCondition; }
  
  @Column(name = "follow_up_dispense_rx")
  public String getFollowUpDispenseRx() { return followUpDispenseRx; }
  public void setFollowUpDispenseRx(String followUpDispenseRx) { this.followUpDispenseRx = followUpDispenseRx; }
  
  @Column(name = "follow_up_uss")
  public String getFollowUpUSS() { return followUpUSS; }
  public void setFollowUpUSS(String followUpUSS) { this.followUpUSS = followUpUSS; }
  
  @Column(name = "follow_up_pregnant")
  public String getFollowUpPregnant() { return followUpPregnant; }
  public void setFollowUpPregnant(String followUpPregnant) { this.followUpPregnant = followUpPregnant; }
  
  @Column(name = "follow_up_wound_care")
  public String getFollowUpWoundCare() { return followUpWoundCare; }
  public void setFollowUpWoundCare(String followUpWoundCare) { this.followUpWoundCare = followUpWoundCare; }
  
  @Column(name = "follow_up_ref_to_specialist")
  public String getFollowUpRefToSpecialist() { return followUpRefToSpecialist; }
  public void setFollowUpRefToSpecialist(String followUpRefToSpecialist) { this.followUpRefToSpecialist = followUpRefToSpecialist; }
  
  @Column(name = "follow_up_dental_list")
  public String getFollowUpDentalList() { return followUpDentalList; }
  public void setFollowUpDentalList(String followUpDentalList) { this.followUpDentalList = followUpDentalList; }
  
  @Column(name = "follow_up_physiotherapy")
  public String getFollowUpPhysiotherapy() { return followUpPhysiotherapy; }
  public void setFollowUpPhysiotherapy(String followUpPhysiotherapy) { this.followUpPhysiotherapy = followUpPhysiotherapy; }
  
  @Column(name = "follow_up_blood_labs")
  public String getFollowUpBloodLabs() { return followUpBloodLabs; }
  public void setFollowUpBloodLabs(String followUpBloodLabs) { this.followUpBloodLabs = followUpBloodLabs; }
  
  @Column(name = "follow_up_other")
  public String getFollowUpOther() { return followUpOther; }
  public void setFollowUpOther(String followUpOther) { this.followUpOther = followUpOther; }
  
  @Column(name = "follow_up_pulmonary_fx_test")
  public String getFollowUpPulmonaryFXTest() { return followUpPulmonaryFXTest; }
  public void setFollowUpPulmonaryFXTest(String followUpPulmonaryFXTest) { this.followUpPulmonaryFXTest = followUpPulmonaryFXTest; }
  
  @Column(name = "follow_up_vision")
  public String getFollowUpVision() { return followUpVision; }
  public void setFollowUpVision(String followUpVision) { this.followUpVision = followUpVision; }
  
  @Column(name = "follow_up_completed")
  public boolean getFollowUpCompleted() { return followUpCompleted; }
  public void setFollowUpCompleted(boolean followUpCompleted) { this.followUpCompleted = followUpCompleted; }
  
  @Column(name = "follow_up_date")
  public Date getFollowUpDate() { return followUpDate; }
  public void setFollowUpDate(Date followUpDate) { this.followUpDate = followUpDate; }
  
  @Column(name = "follow_up_notes")
  public String getFollowUpNotes() { return followUpNotes; }
  public void setFollowUpNotes(String followUpNotes) { this.followUpNotes = followUpNotes; }
  
  @Column(name = "lock_status")
  public Integer getLockStatus() { return lockStatus; }
  public void setLockStatus(Integer lockStatus) { this.lockStatus = lockStatus; }
  
  @Column(name = "paitent_encounter_id")
  public Integer getPatientEncounterId() { return patientEncounterId; }
  public void setPatientEncounterId(Integer patientEncounterId) { this.patientEncounterId = patientEncounterId; }
  
  @Column(name = "patient_vital_signs_id")
  public Integer getPatientVitalsSignsId() { return patientVitalsSignsId; }
  public void setPatientVitalsSignsId(Integer patientVitalsSignsId) { this.patientVitalsSignsId = patientVitalsSignsId; }
  
  @Column(name = "patient_pfsh_id")
  public Integer getPatientPFSHId() { return patientPFSHId; }
  public void setPatientPFSHId(Integer patientPFSHId) { this.patientPFSHId = patientPFSHId; }
  
  @Column(name = "obgyn_encounter_data_id")
  public Integer getOBGYNEncounterDataId() { return OBGYNEncounterDataId; }
  public void setOBGYNEncounterDataId(Integer oBGYNEncounterDataId) { OBGYNEncounterDataId = oBGYNEncounterDataId; }

  @Column(name = "patient_supp_questions_id")
  public Integer getPatientSuppQuestionsId() { return patientSuppQuestionsId; }
  public void setPatientSuppQuestionsId(Integer patientSuppQuestionsId) { this.patientSuppQuestionsId = patientSuppQuestionsId; }

  @Column(name = "patient_medical_history_id")
  public Integer getPatientMedicalHistoryId() { return patientMedicalHistoryId; }
  public void setPatientMedicalHistoryId(Integer patientMedicalHistoryId) { this.patientMedicalHistoryId = patientMedicalHistoryId; }

  @Column(name = "patient_follow_up_id")
  public Integer getPatientFollowUpId() { return patientFollowUpId; }
  public void setPatientFollowUpId(Integer patientFollowUpId) { this.patientFollowUpId = patientFollowUpId; }

  @Transient
  public List<IntakeQuestion> getIntakeQuestionList() { return intakeQuestionList; }
  public void setIntakeQuestionList(List<IntakeQuestion> intakeQuestionList) { this.intakeQuestionList = intakeQuestionList; }

  @Transient
  public List<IntakeMedication> getIntakeMedicationList() { return intakeMedicationList; }
  public void setIntakeMedicationList(List<IntakeMedication> intakeMedicationList) { this.intakeMedicationList = intakeMedicationList; }
  
  @Transient
  public String getProfileImagePath() { return profileImagePath; }
  public void setProfileImagePath(String profileImagePath) { this.profileImagePath = profileImagePath; }

  @Override
  public String toString() {
    return "com.wdeanmedical.ehr.entity.PatientIntake[id=" + getId() + "]";
  }
    
}
