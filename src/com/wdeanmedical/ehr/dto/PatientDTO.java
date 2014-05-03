/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */
 
package com.wdeanmedical.ehr.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wdeanmedical.ehr.entity.Appointment;
import com.wdeanmedical.ehr.entity.Encounter;
import com.wdeanmedical.ehr.entity.Gender;
import com.wdeanmedical.ehr.entity.IntakeMedication;
import com.wdeanmedical.ehr.entity.IntakeQuestion;
import com.wdeanmedical.ehr.entity.Patient;
import com.wdeanmedical.ehr.entity.PatientAllergen;
import com.wdeanmedical.ehr.entity.PatientClinician;
import com.wdeanmedical.ehr.entity.PatientHealthIssue;
import com.wdeanmedical.ehr.entity.PatientHealthTrendReport;
import com.wdeanmedical.ehr.entity.PatientImmunization;
import com.wdeanmedical.ehr.entity.PatientIntakeGroup;
import com.wdeanmedical.ehr.entity.PatientLetter;
import com.wdeanmedical.ehr.entity.PatientMedicalProcedure;
import com.wdeanmedical.ehr.entity.PatientMedicalTest;
import com.wdeanmedical.ehr.entity.PatientMedication;
import com.wdeanmedical.ehr.entity.PatientMessage;
import com.wdeanmedical.ehr.entity.ProgressNote;

public class PatientDTO extends AuthorizedDTO {
  private int id;
  private Encounter encounter;
  private ProgressNote progressNote;
  private String username;
  private String password;
  private String firstName;
  private String middleName;
  private String lastName;
  private String additionalName;
  private String primaryPhone;
  private String secondaryPhone;
  private String email;
  private String mrn;
  private boolean active;
  private boolean purged;
  private String salt;
  private int authStatus;
  private String sessionId;
  private Date lastLoginTime;
  private String previousLoginTime;
  private String profileImagePath;
  private int encounterId;
  private int progressNoteId;
  private List<PatientAllergen> patientAllergens;
  private List<PatientMedication> patientMedications;
  private List<PatientImmunization> patientImmunizations;
  private List<PatientHealthIssue> patientHealthIssues;
  private List<PatientMedicalTest> patientMedicalTests;
  private List<PatientMedicalProcedure> patientMedicalProcedures;
  private List<PatientHealthTrendReport> patientHealthTrendReports;
  private List<PatientLetter> patientLetters;
  private List<PatientMessage> patientMessages;
  private List<PatientClinician> patientClinicians;
  private List<Appointment> appointments;
  private List<Patient> patients;
  private List<ProgressNote> progressNotes;
  private List<Encounter> patientEncounters;
  public Map<String,List> patientChartSummary = new HashMap<String,List>();
  private Patient patient;
  private int patientId;
  private int clinicianId;
  private String nombre;
  private String apellido;
  private String apellidoSegundo;
  private boolean bioFinger;
  private boolean bioPhoto;
  private String age;
  private String triage;
  private String notes;
  private String gender;
  private boolean checkIn;
  private boolean intake;
  private boolean provider;
  private boolean missing;
  private boolean completed;
  private int patientIntakeGroupId;
  private int patientIntakeId;
  private List<PatientIntakeGroup> patientIntakeGroups;
  private PatientIntakeGroup newPatientIntakeGroup;
  private int swapGroupId;
  private String updateProperty;
  private String updatePropertyValue;
  private boolean basicInfoSaved;
  private String consultLocation;
  private String govtId;
  private String dob;
  private String ageInYears;
  private String ageInMonths;
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
  private String motherDob;
  private String caretakerName;
  private String patientRelationship;
  private boolean ccSaved; 
  private String ccDescription;
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
  private String painScale;
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
  private String numResidents; 
  private String jobType; 
  private boolean motherAlive; 
  private String motherDeathReason; 
  private boolean fatherAlive; 
  private String fatherDeathReason; 
  private boolean partnerAlive; 
  private String partnerDeathReason; 
  private String numSiblings; 
  private String numBrothers; 
  private String numSisters; 
  private String numChildren; 
  private String numSons; 
  private String numDaughters;
  private boolean suppSaved; 
  private String numCupsWater; 
  private String numCupsCoffee; 
  private String numCupsTea; 
  private String waterSource; 
  private List<IntakeQuestion> intakeQuestionList;
  private List<IntakeMedication> intakeMedicationList;
  private int intakeQuestionId;
  private int intakeMedicationId;
  private boolean histSaved; 
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
  private String followUpDate; 
  private String followUpNotes; 
  private int lockStatus; 
  private int patientEncounterId; 

  public PatientDTO() {
  }


  public ProgressNote getProgressNote() { return progressNote; }
  public void setProgressNote(ProgressNote progressNote) { this.progressNote = progressNote; }

  public String getPassword() { return this.password; }
  public void setPassword(String password) { this.password = password; }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public int getId() { return id; }
  public void setId(int id) { this.id = id; }
  
  public String getFirstName() { return firstName; }
  public void setFirstName(String firstName) { this.firstName = firstName; }
  
  public String getMiddleName() { return middleName; }
  public void setMiddleName(String middleName) { this.middleName = middleName; }
  
  public String getLastName() { return lastName; }
  public void setLastName(String lastName) { this.lastName = lastName; }
  
  public String getAdditionalName() { return additionalName; }
  public void setAdditionalName(String additionalName) { this.additionalName = additionalName; }

  public String getPrimaryPhone() { return primaryPhone; }
  public void setPrimaryPhone(String primaryPhone) { this.primaryPhone = primaryPhone; }
  
  public String getSecondaryPhone() { return secondaryPhone; }
  public void setSecondaryPhone(String secondaryPhone) { this.secondaryPhone = secondaryPhone; }

  public boolean isActive() { return active; }
  public void setActive(boolean active) { this.active = active; }

  public String getSalt() { return salt; }
  public void setSalt(String salt) { this.salt = salt; }
  
  public int getAuthStatus() { return authStatus; }
  public void setAuthStatus(int authStatus) { this.authStatus = authStatus; }

  public String getUsername() { return username; }
  public void setUsername(String username) { this.username = username; }

  public boolean isPurged() { return purged; }
  public void setPurged(boolean purged) { this.purged = purged; }

  @Override
  public String getSessionId() { return sessionId; }
  @Override
  public void setSessionId(String sessionId) { this.sessionId = sessionId; }

  public Date getLastLoginTime() { return lastLoginTime; }
  public void setLastLoginTime(Date lastLoginTime) { this.lastLoginTime = lastLoginTime; }

  public String getPreviousLoginTime() { return previousLoginTime; }
  public void setPreviousLoginTime(String previousLoginTime) { this.previousLoginTime = previousLoginTime; }
  
  public List<PatientAllergen> getPatientAllergens() { return patientAllergens; }
  public void setPatientAllergens(List<PatientAllergen> patientAllergens) { this.patientAllergens = patientAllergens; }

  public List<PatientMedication> getPatientMedications() { return patientMedications; }
  public void setPatientMedications(List<PatientMedication> patientMedications) { this.patientMedications = patientMedications; }

  public List<PatientImmunization> getPatientImmunizations() { return patientImmunizations; }
  public void setPatientImmunizations(List<PatientImmunization> patientImmunizations) { this.patientImmunizations = patientImmunizations; }

  public List<PatientHealthIssue> getPatientHealthIssues() { return patientHealthIssues; }
  public void setPatientHealthIssues(List<PatientHealthIssue> patientHealthIssues) { this.patientHealthIssues = patientHealthIssues; }

  public List<PatientMedicalTest> getPatientMedicalTests() { return patientMedicalTests; }
  public void setPatientMedicalTests(List<PatientMedicalTest> patientMedicalTests) { this.patientMedicalTests = patientMedicalTests; }
  
  public List<PatientMedicalProcedure> getPatientMedicalProcedures() { return patientMedicalProcedures; }
  public void setPatientMedicalProcedures(List<PatientMedicalProcedure> patientProcedures) { this.patientMedicalProcedures = patientProcedures; }
  
  public List<PatientHealthTrendReport> getPatientHealthTrendReports() { return patientHealthTrendReports; }
  public void setPatientHealthTrendReports(List<PatientHealthTrendReport> patientHealthTrendReports) { this.patientHealthTrendReports = patientHealthTrendReports; }

  public List<PatientLetter> getPatientLetters() { return patientLetters; }
  public void setPatientLetters(List<PatientLetter> patientLetters) { this.patientLetters = patientLetters; }

  public List<PatientMessage> getPatientMessages() { return patientMessages; }
  public void setPatientMessages(List<PatientMessage> patientMessages) { this.patientMessages = patientMessages; }
  
  public List<Appointment> getAppointments() { return appointments; }
  public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }

  public List<PatientClinician> getPatientClinicians() { return patientClinicians; }
  public void setPatientClinicians(List<PatientClinician> patientClinicians) { this.patientClinicians = patientClinicians; }
  
  public String getMrn() { return mrn; }
  public void setMrn(String mrn) { this.mrn = mrn; }

  public List<Patient> getPatients() { return patients; }
  public void setPatients(List<Patient> patients) { this.patients = patients; }
  
  public String getProfileImagePath() { return profileImagePath; }
  public void setProfileImagePath(String profileImagePath) { this.profileImagePath = profileImagePath; }

  public List<Encounter> getPatientEncounters() { return patientEncounters; }
  public void setPatientEncounters(List<Encounter> patientEncounters) { this.patientEncounters = patientEncounters; }

  public int getClinicianId() { return clinicianId; }
  public void setClinicianId(int clinicianId) { this.clinicianId = clinicianId; }

  public int getEncounterId() { return encounterId; }
  public void setEncounterId(int encounterId) { this.encounterId = encounterId; }
  
  public int getPatientId() { return patientId; }
  public void setPatientId(int patientId) { this.patientId = patientId; }
  
  public Patient getPatient() { return patient; }
  public void setPatient(Patient patient) { this.patient = patient; }
  
  public String getNombre() { return nombre; }
  public void setNombre(String nombre) { this.nombre = nombre; }
  
  public String getApellido() { return apellido; }
  public void setApellido(String apellido) { this.apellido = apellido; }
  
  public String getApellidoSegundo() { return apellidoSegundo; }
  public void setApellidoSegundo(String apellidoSegundo) { this.apellidoSegundo = apellidoSegundo; }
  
  public boolean isBioFinger() { return bioFinger; }
  public void setBioFinger(boolean bioFinger) { this.bioFinger = bioFinger; }
  
  public boolean isBioPhoto() { return bioPhoto; }
  public void setBioPhoto(boolean bioPhoto) { this.bioPhoto = bioPhoto; }
  
  public String getAge() { return age; }
  public void setAge(String age) { this.age = age; }
  
  public String getTriage() { return triage; }
  public void setTriage(String triage) { this.triage = triage; }
  
  public String getNotes() { return notes; }
  public void setNotes(String notes) { this.notes = notes; }
  
  public String getGender() { return gender; }
  public void setGender(String gender) { this.gender = gender; }
  
  public boolean isCheckIn() { return checkIn; }
  public void setCheckIn(boolean checkIn) { this.checkIn = checkIn; }
  
  public boolean isIntake() { return intake; }
  public void setIntake(boolean intake) { this.intake = intake; }
  
  public boolean isProvider() { return provider; }
  public void setProvider(boolean provider) { this.provider = provider; }
  
  public boolean isMissing() { return missing; }
  public void setMissing(boolean missing) { this.missing = missing; }
  
  public boolean isCompleted() { return completed; }
  public void setCompleted(boolean completed) { this.completed = completed; }
  
  public int getPatientIntakeGroupId() { return patientIntakeGroupId; }
  public void setPatientIntakeGroupId(int patientIntakeGroupId) { this.patientIntakeGroupId = patientIntakeGroupId; }
  
  public int getPatientIntakeId() { return patientIntakeId; }
  public void setPatientIntakeId(int patientIntakeId) { this.patientIntakeId = patientIntakeId; }
  
  public List<PatientIntakeGroup> getPatientIntakeGroups() { return patientIntakeGroups; }
  public void setPatientIntakeGroups(List<PatientIntakeGroup> patientIntakeGroups) { this.patientIntakeGroups = patientIntakeGroups; }
  
  public PatientIntakeGroup getNewPatientIntakeGroup() { return newPatientIntakeGroup; }
  public void setNewPatientIntakeGroup(PatientIntakeGroup newPatientIntakeGroup) { this.newPatientIntakeGroup = newPatientIntakeGroup; }
  
  public int getSwapGroupId() { return swapGroupId; }
  public void setSwapGroupId(int swapGroupId) { this.swapGroupId = swapGroupId; }
  
  public String getUpdateProperty() { return updateProperty; }
  public void setUpdateProperty(String updateProperty) { this.updateProperty = updateProperty; }
  
  public String getUpdatePropertyValue() { return updatePropertyValue; }
  public void setUpdatePropertyValue(String updatePropertyValue) { this.updatePropertyValue = updatePropertyValue; }
  
  public boolean isBasicInfoSaved() { return basicInfoSaved; }
  public void setBasicInfoSaved(boolean basicInfoSaved) { this.basicInfoSaved = basicInfoSaved; }
  
  public String getConsultLocation() { return consultLocation; }
  public void setConsultLocation(String consultLocation) { this.consultLocation = consultLocation; }

  public String getGovtId() { return govtId; }
  public void setGovtId(String govtId) { this.govtId = govtId; }

  public String getDob() { return dob; }
  public void setDob(String dob) { this.dob = dob; }

  public String getAgeInYears() { return ageInYears; }
  public void setAgeInYears(String ageInYears) { this.ageInYears = ageInYears; }

  public String getAgeInMonths() { return ageInMonths; }
  public void setAgeInMonths(String ageInMonths) { this.ageInMonths = ageInMonths; }

  public String getPhone() { return phone; }
  public void setPhone(String phone) { this.phone = phone; }

  public String getCommunity() { return community; }
  public void setCommunity(String community) { this.community = community; }
  
  public boolean isVitalsSaved() { return vitalsSaved; }
  public void setVitalsSaved(boolean vitalsSaved) { this.vitalsSaved = vitalsSaved; }
  
  public String getHeight() { return height; }
  public void setHeight(String height) { this.height = height; }
  
  public String getWeight() { return weight; }
  public void setWeight(String weight) { this.weight = weight; }
  
  public String getSys() { return sys; }
  public void setSys(String sys) { this.sys = sys; }
  
  public String getDia() { return dia; }
  public void setDia(String dia) { this.dia = dia; }
  
  public String getHr() { return hr; }
  public void setHr(String hr) { this.hr = hr; }
  
  public String getRr() { return rr; }
  public void setRr(String rr) { this.rr = rr; }
  
  public String getTemp() { return temp; }
  public void setTemp(String temp) { this.temp = temp; }
  
  public String getArm() { return arm; }
  public void setArm(String arm) { this.arm = arm; }
  
  public boolean isFamilySaved() { return familySaved; }
  public void setFamilySaved(boolean familySaved) { this.familySaved = familySaved; }

  public String getMotherName() { return motherName; }
  public void setMotherName(String motherName) { this.motherName = motherName; }

  public String getMotherDob() { return motherDob; }
  public void setMotherDob(String motherDob) { this.motherDob = motherDob; }

  public String getCaretakerName() { return caretakerName; }
  public void setCaretakerName(String caretakerName) { this.caretakerName = caretakerName; }

  public String getPatientRelationship() { return patientRelationship; }
  public void setPatientRelationship(String patientRelationship) { this.patientRelationship = patientRelationship; }
  
  public boolean isCcSaved() { return ccSaved; }
  public void setCcSaved(boolean ccSaved) { this.ccSaved = ccSaved; }
  
  public String getCcDescription() { return ccDescription; }
  public void setCcDescription(String ccDescription) { this.ccDescription = ccDescription; }

  public String getSpecificLocation() { return specificLocation; }
  public void setSpecificLocation(String specificLocation) { this.specificLocation = specificLocation; }
  
  public String getHoursSince() { return hoursSince; }
  public void setHoursSince(String hoursSince) { this.hoursSince = hoursSince; }
  
  public String getDaysSince() { return daysSince; }
  public void setDaysSince(String daysSince) { this.daysSince = daysSince; }
  
  public String getWeeksSince() { return weeksSince; }
  public void setWeeksSince(String weeksSince) { this.weeksSince = weeksSince; }
  
  public String getMonthsSince() { return monthsSince; }
  public void setMonthsSince(String monthsSince) { this.monthsSince = monthsSince; }
  
  public String getYearsSince() { return yearsSince; }
  public void setYearsSince(String yearsSince) { this.yearsSince = yearsSince; }
  
  public String getHowLongOther() { return howLongOther; }
  public void setHowLongOther(String howLongOther) { this.howLongOther = howLongOther; }
  
  public String getPainXHour() { return painXHour; }
  public void setPainXHour(String painXHour) { this.painXHour = painXHour; }
  
  public String getPainXDay() { return painXDay; }
  public void setPainXDay(String painXDay) { this.painXDay = painXDay; }
  
  public String getPainXWeek() { return painXWeek; }
  public void setPainXWeek(String painXWeek) { this.painXWeek = painXWeek; }
  
  public String getPainXMonth() { return painXMonth; }
  public void setPainXMonth(String painXMonth) { this.painXMonth = painXMonth; }
  
  public String getPainDuration() { return painDuration; }
  public void setPainDuration(String painDuration) { this.painDuration = painDuration; }
  
  public String getPainScale() { return painScale; }
  public void setPainScale(String painScale) { this.painScale = painScale; }

  public String getPainType() { return painType; }
  public void setPainType(String painType) { this.painType = painType; }
  
  public String getDenies() { return denies; }
  public void setDenies(String denies) { this.denies = denies; }
  
  public String getDeniesOther() { return deniesOther; }
  public void setDeniesOther(String deniesOther) { this.deniesOther = deniesOther; }
  
  public String getOccursWhen() { return occursWhen; }
  public void setOccursWhen(String occursWhen) { this.occursWhen = occursWhen; }
  
  public boolean isObgynSaved() { return obgynSaved; }
  public void setObgynSaved(boolean obgynSaved) { this.obgynSaved = obgynSaved; }
  
  public String getObgynG() { return obgynG; }
  public void setObgynG(String obgynG) { this.obgynG = obgynG; }

  public String getObgynP() { return obgynP; }
  public void setObgynP(String obgynP) { this.obgynP = obgynP; }

  public String getObgynT() { return obgynT; }
  public void setObgynT(String obgynT) { this.obgynT = obgynT; }

  public String getObgynA() { return obgynA; }
  public void setObgynA(String obgynA) { this.obgynA = obgynA; }

  public String getObgynL() { return obgynL; }
  public void setObgynL(String obgynL) { this.obgynL = obgynL; }

  public String getPregStatus() { return pregStatus; }
  public void setPregStatus(String pregStatus) { this.pregStatus = pregStatus; }

  public String getBreastfeeding() { return breastfeeding; }
  public void setBreastfeeding(String breastfeeding) { this.breastfeeding = breastfeeding; }

  public String getBreastfeedingMonths() { return breastfeedingMonths; }
  public void setBreastfeedingMonths(String breastfeedingMonths) { this.breastfeedingMonths = breastfeedingMonths; }

  public String getLastPeriod() { return lastPeriod; }
  public void setLastPeriod(String lastPeriod) { this.lastPeriod = lastPeriod; }

  public String getAgeFirstPeriod() { return ageFirstPeriod; }
  public void setAgeFirstPeriod(String ageFirstPeriod) { this.ageFirstPeriod = ageFirstPeriod; }

  public String getPapSmearStatus() { return papSmearStatus; }
  public void setPapSmearStatus(String papSmearStatus) { this.papSmearStatus = papSmearStatus; }

  public String getBirthControlStatus() { return birthControlStatus; }
  public void setBirthControlStatus(String birthControlStatus) { this.birthControlStatus = birthControlStatus; }

  public String getBirthControlType() { return birthControlType; }
  public void setBirthControlType(String birthControlType) { this.birthControlType = birthControlType; }

  public String getObgynHistory() { return obgynHistory; }
  public void setObgynHistory(String obgynHistory) { this.obgynHistory = obgynHistory; }
  
  public String getObgynHistoryOther() { return obgynHistoryOther; }
  public void setObgynHistoryOther(String obgynHistoryOther) { this.obgynHistoryOther = obgynHistoryOther; }
  
  public boolean isPfshSaved() { return pfshSaved; }
  public void setPfshSaved(boolean pfshSaved) { this.pfshSaved = pfshSaved; }
  
  public String getNumResidents() { return numResidents; }
  public void setNumResidents(String numResidents) { this.numResidents = numResidents; }
  
  public String getJobType() { return jobType; }
  public void setJobType(String jobType) { this.jobType = jobType; }
  
  public boolean isMotherAlive() { return motherAlive; }
  public void setMotherAlive(boolean motherAlive) { this.motherAlive = motherAlive; }

  public String getMotherDeathReason() { return motherDeathReason; }
  public void setMotherDeathReason(String motherDeathReason) { this.motherDeathReason = motherDeathReason; }

  public boolean isFatherAlive() { return fatherAlive; }
  public void setFatherAlive(boolean fatherAlive) { this.fatherAlive = fatherAlive; }

  public String getFatherDeathReason() { return fatherDeathReason; }
  public void setFatherDeathReason(String fatherDeathReason) { this.fatherDeathReason = fatherDeathReason; }

  public boolean isPartnerAlive() { return partnerAlive; }
  public void setPartnerAlive(boolean partnerAlive) { this.partnerAlive = partnerAlive; }

  public String getPartnerDeathReason() { return partnerDeathReason; }
  public void setPartnerDeathReason(String partnerDeathReason) { this.partnerDeathReason = partnerDeathReason; }

  public String getNumSiblings() { return numSiblings; }
  public void setNumSiblings(String numSiblings) { this.numSiblings = numSiblings; }

  public String getNumBrothers() { return numBrothers; }
  public void setNumBrothers(String numBrothers) { this.numBrothers = numBrothers; }

  public String getNumSisters() { return numSisters; }
  public void setNumSisters(String numSisters) { this.numSisters = numSisters; }

  public String getNumChildren() { return numChildren; }
  public void setNumChildren(String numChildren) { this.numChildren = numChildren; }

  public String getNumSons() { return numSons; }
  public void setNumSons(String numSons) { this.numSons = numSons; }

  public String getNumDaughters() { return numDaughters; }
  public void setNumDaughters(String numDaughters) { this.numDaughters = numDaughters; }
  
  public boolean isSuppSaved() { return suppSaved; }
  public void setSuppSaved(boolean suppSaved) { this.suppSaved = suppSaved; }
  
  public String getNumCupsWater() { return numCupsWater; }
  public void setNumCupsWater(String numCupsWater) { this.numCupsWater = numCupsWater; }
  
  public String getNumCupsCoffee() { return numCupsCoffee; }
  public void setNumCupsCoffee(String numCupsCoffee) { this.numCupsCoffee = numCupsCoffee; }
  
  public String getNumCupsTea() { return numCupsTea; }
  public void setNumCupsTea(String numCupsTea) { this.numCupsTea = numCupsTea; }
  
  public String getWaterSource() { return waterSource; }
  public void setWaterSource(String waterSource) { this.waterSource = waterSource; }
  
  public List<IntakeQuestion> getIntakeQuestionList() { return intakeQuestionList; }
  public void setIntakeQuestionList(List<IntakeQuestion> intakeQuestionList) { this.intakeQuestionList = intakeQuestionList; }

  public List<IntakeMedication> getIntakeMedicationList() { return intakeMedicationList; }
  public void setIntakeMedicationList(List<IntakeMedication> intakeMedicationList) { this.intakeMedicationList = intakeMedicationList; }
  
  public int getIntakeQuestionId() { return intakeQuestionId; }
  public void setIntakeQuestionId(int intakeQuestionId) { this.intakeQuestionId = intakeQuestionId; }
  
  public int getIntakeMedicationId() { return intakeMedicationId; }
  public void setIntakeMedicationId(int intakeMedicationId) { this.intakeMedicationId = intakeMedicationId; }
  
  public boolean isHistSaved() { return histSaved; }
  public void setHistSaved(boolean histSaved) { this.histSaved = histSaved; }
  
  public String getPastSM() { return pastSM; }
  public void setPastSM(String pastSM) { this.pastSM = pastSM; }
  
  public String getFamHist() { return famHist; }
  public void setFamHist(String famHist) { this.famHist = famHist; }
  
  public String getFamHistNotes() { return famHistNotes; }
  public void setFamHistNotes(String famHistNotes) { this.famHistNotes = famHistNotes; }
  
  public String getFamHistOther() { return famHistOther; }
  public void setFamHistOther(String famHistOther) { this.famHistOther = famHistOther; }
  
  public String getAllergFood() { return allergFood; }
  public void setAllergFood(String allergFood) { this.allergFood = allergFood; }
  
  public String getAllergDrug() { return allergDrug; }
  public void setAllergDrug(String allergDrug) { this.allergDrug = allergDrug; }
  
  public String getAllergEnv() { return allergEnv; }
  public void setAllergEnv(String allergEnv) { this.allergEnv = allergEnv; }
  
  public String getVacc() { return vacc; }
  public void setVacc(String vacc) { this.vacc = vacc; }
  
  public String getVaccNotes() { return vaccNotes; }
  public void setVaccNotes(String vaccNotes) { this.vaccNotes = vaccNotes; }
  
  public String getSubst() { return subst; }
  public void setSubst(String subst) { this.subst = subst; }
  
  public String getSmokePksDay() { return smokePksDay; }
  public void setSmokePksDay(String smokePksDay) { this.smokePksDay = smokePksDay; }
  
  public String getYearsSmoked() { return yearsSmoked; }
  public void setYearsSmoked(String yearsSmoked) { this.yearsSmoked = yearsSmoked; }
  
  public String getSmokeYearsQuit() { return smokeYearsQuit; }
  public void setSmokeYearsQuit(String smokeYearsQuit) { this.smokeYearsQuit = smokeYearsQuit; }
  
  public String getEtohUnitsWeek() { return etohUnitsWeek; }
  public void setEtohUnitsWeek(String etohUnitsWeek) { this.etohUnitsWeek = etohUnitsWeek; }
  
  public String getCurrentDrugs() { return currentDrugs; }
  public void setCurrentDrugs(String currentDrugs) { this.currentDrugs = currentDrugs; }
  
  public boolean isExamSaved() { return examSaved; }
  public void setExamSaved(boolean examSaved) { this.examSaved = examSaved; }
  
  public String getHs() { return hs; }
  public void setHs(String hs) { this.hs = hs; }
  
  public String getHeartRhythm() { return heartRhythm; }
  public void setHeartRhythm(String heartRhythm) { this.heartRhythm = heartRhythm; }
  
  public String getHb() { return hb; }
  public void setHb(String hb) { this.hb = hb  ; }
  
  public String getGlucose() { return glucose; }
  public void setGlucose(String glucose) { this.glucose = glucose; }
  
  public String getUrineDIP() { return urineDIP; }
  public void setUrineDIP(String urineDIP) { this.urineDIP = urineDIP; }
  
  public String getDiagnosis() { return diagnosis; }
  public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
  
  public String getDxCode() { return dxCode; } 
  public void setDxCode(String dxCode) { this.dxCode = dxCode; }
  
  public String getTreatmentPlan() { return treatmentPlan; }
  public void setTreatmentPlan(String treatmentPlan) { this.treatmentPlan = treatmentPlan; }
  
  public String getTxCode() { return txCode; }
  public void setTxCode(String txCode) { this.txCode = txCode; }
  
  public String getProviderName() { return providerName; }
  public void setProviderName(String providerName) { this.providerName = providerName; }
  
  public boolean isFollowUpSaved() { return followUpSaved; }
  public void setFollowUpSaved(boolean followUpSaved) { this.followUpSaved = followUpSaved; }
  
  public String getFollowUpLevel() { return followUpLevel; }
  public void setFollowUpLevel(String followUpLevel) { this.followUpLevel = followUpLevel; }
  
  public String getFollowUpWhen() { return followUpWhen; }
  public void setFollowUpWhen(String followUpWhen) { this.followUpWhen = followUpWhen; }
  
  public String getFollowUpCondition() { return followUpCondition; }
  public void setFollowUpCondition(String followUpCondition) { this.followUpCondition = followUpCondition; }
  
  public String getFollowUpDispenseRx() { return followUpDispenseRx; }
  public void setFollowUpDispenseRx(String followUpDispenseRx) { this.followUpDispenseRx = followUpDispenseRx; }
  
  public String getFollowUpUSS() { return followUpUSS; }
  public void setFollowUpUSS(String followUpUSS) { this.followUpUSS = followUpUSS; }
  
  public String getFollowUpPregnant() { return followUpPregnant; }
  public void setFollowUpPregnant(String followUpPregnant) { this.followUpPregnant = followUpPregnant; }
  
  public String getFollowUpWoundCare() { return followUpWoundCare; }
  public void setFollowUpWoundCare(String followUpWoundCare) { this.followUpWoundCare = followUpWoundCare; }
  
  public String getFollowUpRefToSpecialist() { return followUpRefToSpecialist; }
  public void setFollowUpRefToSpecialist(String followUpRefToSpecialist) { this.followUpRefToSpecialist = followUpRefToSpecialist; }
  
  public String getFollowUpDentalList() { return followUpDentalList; }
  public void setFollowUpDentalList(String followUpDentalList) { this.followUpDentalList = followUpDentalList; }
  
  public String getFollowUpPhysiotherapy() { return followUpPhysiotherapy; }
  public void setFollowUpPhysiotherapy(String followUpPhysiotherapy) { this.followUpPhysiotherapy = followUpPhysiotherapy; }
  
  public String getFollowUpBloodLabs() { return followUpBloodLabs; }
  public void setFollowUpBloodLabs(String followUpBloodLabs) { this.followUpBloodLabs = followUpBloodLabs; }
  
  public String getFollowUpOther() { return followUpOther; }
  public void setFollowUpOther(String followUpOther) { this.followUpOther = followUpOther; }
  
  public String getFollowUpPulmonaryFXTest() { return followUpPulmonaryFXTest; }
  public void setFollowUpPulmonaryFXTest(String followUpPulmonaryFXTest) { this.followUpPulmonaryFXTest = followUpPulmonaryFXTest; }
  
  public String getFollowUpVision() { return followUpVision; }
  public void setFollowUpVision(String followUpVision) { this.followUpVision = followUpVision; }
  
  public boolean getFollowUpCompleted() { return followUpCompleted; }
  public void setFollowUpCompleted(boolean followUpCompleted) { this.followUpCompleted = followUpCompleted; }
  
  public String getFollowUpDate() { return followUpDate; }
  public void setFollowUpDate(String followUpDate) { this.followUpDate = followUpDate; }
  
  public String getFollowUpNotes() { return followUpNotes; }
  public void setFollowUpNotes(String followUpNotes) { this.followUpNotes = followUpNotes; }

  public int getLockStatus() { return lockStatus; }
  public void setLockStatus(int lockStatus) { this.lockStatus = lockStatus; }
  
  public int getPatientEncounterId() { return patientEncounterId; }
  public void setPatientEncounterId(int patientEncounterId) { this.patientEncounterId = patientEncounterId; }

  public Encounter getEncounter() { return encounter; }
  public void setEncounter(Encounter encounter) { this.encounter = encounter; }

  public List<ProgressNote> getProgressNotes() { return progressNotes; }
  public void setProgressNotes(List<ProgressNote> progressNotes) { this.progressNotes = progressNotes; }

  public int getProgressNoteId() { return progressNoteId; }
  public void setProgressNoteId(int progressNoteId) { this.progressNoteId = progressNoteId; }   
  
}
