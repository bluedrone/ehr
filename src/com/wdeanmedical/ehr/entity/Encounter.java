package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "encounter")
public class Encounter extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1462745762564975233L;
  
  public static final Integer LOCK_FREE = 0;
  public static final Integer LOCK_LOCKED = 1;
  public static final Integer LOCK_OVERRIDDEN = 2;
  
  private Date date;
  private Patient patient;
  private Clinician clinician;
  private Department department;
  private EncounterType encounterType;
  private ChiefComplaint cc;
  private VitalSigns vitals;
  private SuppQuestions supp;
  private Exam exam;
  private Lab lab;
  private OBGYNEncounterData obgyn;
  private PatientFollowUp followUp;
  private Integer lockStatus;
  private Integer triage;
  private String notes;
  private Boolean completed = false;
  private Boolean checkIn = false;
  private Boolean intake = false;
  private Boolean provider = false;
  private Boolean missing = false;
  private String consultLocation;
  private Integer ageInYears;
  private Integer ageInMonths;
  private String community;
  private Integer patientIntakeGroupId;
  private Boolean basicInfoSaved = false;
  private Boolean vitalsSaved = false;
  private Boolean familySaved = false;
  private Boolean ccSaved = false; 
  private Boolean obgynSaved = false; 
  private Boolean pfshSaved = false; 
  private Boolean suppSaved = false; 
  private Boolean histSaved = false; 
  private Boolean examSaved = false; 
  private Boolean followUpSaved = false; 
  private Boolean intakeCompleted = false;

  
  public Encounter() {
  }
  
  
  @Column(name = "completed")
  public Boolean getCompleted() { return completed; }
  public void setCompleted(Boolean completed) { this.completed = completed; }

  @JoinColumn(name = "patient", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public Patient getPatient() { return patient; }
  public void setPatient(Patient patient) { this.patient = patient; }
  
  @Column(name = "date")
  public Date getDate() { return date; }
  public void setDate(Date date) { this.date = date; }

  @JoinColumn(name = "clinician", referencedColumnName = "id")
  @ManyToOne(optional = true)
  public Clinician getClinician() { return clinician; }
  public void setClinician(Clinician clinician) { this.clinician = clinician; }
  
  @JoinColumn(name = "department", referencedColumnName = "id")
  @ManyToOne(optional = true)
  public Department getDepartment() { return department; }
  public void setDepartment(Department department) { this.department = department; }

  @JoinColumn(name = "vital_signs", referencedColumnName = "id")
  @ManyToOne(optional = true)
  public VitalSigns getVitals() { return vitals; }
  public void setVitals(VitalSigns vitals) { this.vitals = vitals; }

  @JoinColumn(name = "chief_complaint", referencedColumnName = "id")
  @ManyToOne(optional = true)
  public ChiefComplaint getCc() { return cc; }
  public void setCc(ChiefComplaint cc) { this.cc = cc; }
  
  @JoinColumn(name = "exam", referencedColumnName = "id")
  @ManyToOne(optional = true)
  public Exam getExam() { return exam; }
  public void setExam(Exam exam) { this.exam = exam; }
  
  @JoinColumn(name = "lab", referencedColumnName = "id")
  @ManyToOne(optional = true)
  public Lab getLab() { return lab; }
  public void setLab(Lab lab) { this.lab = lab; }
  
  @JoinColumn(name = "obgyn", referencedColumnName = "id")
  @ManyToOne(optional = true)
  public OBGYNEncounterData getObgyn() { return obgyn; }
  public void setObgyn(OBGYNEncounterData obgyn) { this.obgyn = obgyn; }
  
  @JoinColumn(name = "patient_follow_up", referencedColumnName = "id")
  @ManyToOne(optional = true)
  public PatientFollowUp getFollowUp() { return followUp; }
  public void setFollowUp(PatientFollowUp followUp) { this.followUp = followUp; }

  @JoinColumn(name = "encounter_type", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public EncounterType getEncounterType() { return encounterType; }
  public void setEncounterType(EncounterType encounterType) { this.encounterType = encounterType; }
  
  @Column(name = "lock_status")
  public Integer getLockStatus() { return lockStatus; }
  public void setLockStatus(Integer lockStatus) { this.lockStatus = lockStatus; }

  @Column(name = "triage")
  public Integer getTriage() { return triage; }
  public void setTriage(Integer triage) { this.triage = triage; }

  @Column(name = "notes")
  public String getNotes() { return notes; }
  public void setNotes(String notes) { this.notes = notes; }

  @Column(name = "check_in")
  public Boolean getCheckIn() { return checkIn; }
  public void setCheckIn(Boolean checkIn) { this.checkIn = checkIn; }

  @Column(name = "intake")
  public Boolean getIntake() { return intake; }
  public void setIntake(Boolean intake) { this.intake = intake; }

  @Column(name = "provider")
  public Boolean getProvider() { return provider; }
  public void setProvider(Boolean provider) { this.provider = provider; }

  @Column(name = "missing")
  public Boolean getMissing() { return missing; }
  public void setMissing(Boolean missing) { this.missing = missing; }

  @Column(name = "intake_completed")
  public Boolean getIntakeCompleted() { return intakeCompleted; }
  public void setIntakeCompleted(Boolean intakeCompleted) { this.intakeCompleted = intakeCompleted; }

  @Column(name = "consult_location")
  public String getConsultLocation() { return consultLocation; }
  public void setConsultLocation(String consultLocation) { this.consultLocation = consultLocation; }

  @Column(name = "age_in_years")
  public Integer getAgeInYears() { return ageInYears; }
  public void setAgeInYears(Integer ageInYears) { this.ageInYears = ageInYears; }

  @Column(name = "age_in_months")
  public Integer getAgeInMonths() { return ageInMonths; }
  public void setAgeInMonths(Integer ageInMonths) { this.ageInMonths = ageInMonths; }

  @Column(name = "community")
  public String getCommunity() { return community; }
  public void setCommunity(String community) { this.community = community; }
  
  @JoinColumn(name = "supp_questions", referencedColumnName = "id")
  @ManyToOne(optional = true)
  public SuppQuestions getSupp() { return supp; }
  public void setSupp(SuppQuestions supp) { this.supp = supp; }
  
  @Column(name = "patient_intake_group_id")
  public Integer getPatientIntakeGroupId() { return patientIntakeGroupId; }
  public void setPatientIntakeGroupId(Integer patientIntakeGroupId) { this.patientIntakeGroupId = patientIntakeGroupId; }

  @Column(name = "basic_info_saved")
  public Boolean getBasicInfoSaved() { return basicInfoSaved; }
  public void setBasicInfoSaved(Boolean basicInfoSaved) { this.basicInfoSaved = basicInfoSaved; }

  @Column(name = "vitals_saved")
  public Boolean getVitalsSaved() { return vitalsSaved; }
  public void setVitalsSaved(Boolean vitalsSaved) { this.vitalsSaved = vitalsSaved; }

  @Column(name = "family_saved")
  public Boolean getFamilySaved() { return familySaved; }
  public void setFamilySaved(Boolean familySaved) { this.familySaved = familySaved; }

  @Column(name = "cc_saved")
  public Boolean getCcSaved() { return ccSaved; }
  public void setCcSaved(Boolean ccSaved) { this.ccSaved = ccSaved; }

  @Column(name = "obgyn_saved")
  public Boolean getObgynSaved() { return obgynSaved; }
  public void setObgynSaved(Boolean obgynSaved) { this.obgynSaved = obgynSaved; }

  @Column(name = "pfsh_saved")
  public Boolean getPfshSaved() { return pfshSaved; }
  public void setPfshSaved(Boolean pfshSaved) { this.pfshSaved = pfshSaved; }

  @Column(name = "supp_saved")
  public Boolean getSuppSaved() { return suppSaved; }
  public void setSuppSaved(Boolean suppSaved) { this.suppSaved = suppSaved; }

  @Column(name = "hist_saved")
  public Boolean getHistSaved() { return histSaved; }
  public void setHistSaved(Boolean histSaved) { this.histSaved = histSaved; }

  @Column(name = "exam_saved")
  public Boolean getExamSaved() { return examSaved; }
  public void setExamSaved(Boolean examSaved) { this.examSaved = examSaved; }

  @Column(name = "follow_up_saved")
  public Boolean getFollowUpSaved() { return followUpSaved; }
  public void setFollowUpSaved(Boolean followUpSaved) { this.followUpSaved = followUpSaved; }

}
