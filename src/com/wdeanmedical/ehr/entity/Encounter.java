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
@Table(name = "encounter")
public class Encounter extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1462745762564975233L;

  public static final Integer LOCK_FREE = 0;
  public static final Integer LOCK_LOCKED = 1;
  public static final Integer LOCK_OVERRIDDEN = 2;

  private Date date;
  private Patient patient;
  private Clinician clinician;
  private EncounterType encounterType;
  private ChiefComplaint cc;
  private VitalSigns vitals;
  private SuppQuestions supp;
  private Exam exam;
  private Lab lab;
  private SOAPNote soapNote;
  private OBGYNEncounterData obgyn;
  private PatientFollowUp followUp;
  private Integer lockStatus;
  private Boolean completed = false;
  private String notes;
  private Boolean demoSaved = true;
  private Boolean vitalsSaved = false;
  private Boolean soapNoteSaved = false;
  private Boolean ccSaved = false;
  private Boolean obgynSaved = false;
  private Boolean pfshSaved = false;
  private Boolean suppSaved = false;
  private Boolean histSaved = false;
  private Boolean examSaved = false;
  private Boolean followUpSaved = false;
  private List<DxCode> dxCodes;
  private List<TxCode> txCodes;

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

  @JoinColumn(name = "soap_note", referencedColumnName = "id")
  @ManyToOne(optional = true)
  public SOAPNote getSOAPNote() { return soapNote; }
  public void setSOAPNote(SOAPNote soapNote) { this.soapNote = soapNote; }

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

  @JoinColumn(name = "supp_questions", referencedColumnName = "id")
  @ManyToOne(optional = true)
  public SuppQuestions getSupp() { return supp; }
  public void setSupp(SuppQuestions supp) { this.supp = supp; }

  @Column(name = "demo_saved")
  public Boolean getDemoSaved() { return demoSaved; }
  public void setDemoSaved(Boolean demoSaved) { this.demoSaved = demoSaved; }

@Column(name = "vitals_saved")
  public Boolean getVitalsSaved() { return vitalsSaved; }
  public void setVitalsSaved(Boolean vitalsSaved) { this.vitalsSaved = vitalsSaved; }

  @Column(name = "soap_note_saved")
  public Boolean getSOAPNoteSaved() { return soapNoteSaved; }
  public void setSOAPNoteSaved(Boolean soapNoteSaved) { this.soapNoteSaved = soapNoteSaved; }

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

  @Column(name = "notes")
  public String getNotes() { return notes; }
  public void setNotes(String notes) { this.notes = notes; }
  
  @Transient
  public List<DxCode> getDxCodes() { return dxCodes; }
  public void setDxCodes(List<DxCode> dxCodes) { this.dxCodes = dxCodes; }

  @Transient
  public List<TxCode> getTxCodes() { return txCodes; }
  public void setTxCodes(List<TxCode> txCodes) { this.txCodes = txCodes; }


  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result
        + ((demoSaved == null) ? 0 : demoSaved.hashCode());
    result = prime * result + ((cc == null) ? 0 : cc.hashCode());
    result = prime * result + ((ccSaved == null) ? 0 : ccSaved.hashCode());
    result = prime * result
        + ((clinician == null) ? 0 : clinician.hashCode());
    result = prime * result
        + ((completed == null) ? 0 : completed.hashCode());
    result = prime * result + ((date == null) ? 0 : date.hashCode());
    result = prime * result
        + ((encounterType == null) ? 0 : encounterType.hashCode());
    result = prime * result + ((exam == null) ? 0 : exam.hashCode());
    result = prime * result
        + ((examSaved == null) ? 0 : examSaved.hashCode());
    result = prime * result
        + ((soapNoteSaved == null) ? 0 : soapNoteSaved.hashCode());
    result = prime * result
        + ((followUp == null) ? 0 : followUp.hashCode());
    result = prime * result
        + ((followUpSaved == null) ? 0 : followUpSaved.hashCode());
    result = prime * result
        + ((histSaved == null) ? 0 : histSaved.hashCode());
    result = prime * result + ((lab == null) ? 0 : lab.hashCode());
    result = prime * result
        + ((lockStatus == null) ? 0 : lockStatus.hashCode());
    result = prime * result + ((notes == null) ? 0 : notes.hashCode());
    result = prime * result + ((obgyn == null) ? 0 : obgyn.hashCode());
    result = prime * result
        + ((obgynSaved == null) ? 0 : obgynSaved.hashCode());
    result = prime * result + ((patient == null) ? 0 : patient.hashCode());
    result = prime * result
        + ((pfshSaved == null) ? 0 : pfshSaved.hashCode());
    result = prime * result + ((supp == null) ? 0 : supp.hashCode());
    result = prime * result
        + ((suppSaved == null) ? 0 : suppSaved.hashCode());
    result = prime * result + ((vitals == null) ? 0 : vitals.hashCode());
    result = prime * result
        + ((vitalsSaved == null) ? 0 : vitalsSaved.hashCode());
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
    Encounter other = (Encounter) obj;
    if (demoSaved == null) {
      if (other.demoSaved != null)
        {return false;}
    } else if (!demoSaved.equals(other.demoSaved))
      {return false;}
    if (cc == null) {
      if (other.cc != null)
        {return false;}
    } else if (!cc.equals(other.cc))
      {return false;}
    if (ccSaved == null) {
      if (other.ccSaved != null)
        {return false;}
    } else if (!ccSaved.equals(other.ccSaved))
      {return false;}
    if (clinician == null) {
      if (other.clinician != null)
        {return false;}
    } else if (!clinician.equals(other.clinician))
      {return false;}
    if (completed == null) {
      if (other.completed != null)
        {return false;}
    } else if (!completed.equals(other.completed))
      {return false;}
    if (date == null) {
      if (other.date != null)
        {return false;}
    } else if (!date.equals(other.date))
      {return false;}
    if (encounterType == null) {
      if (other.encounterType != null)
        {return false;}
    } else if (!encounterType.equals(other.encounterType))
      {return false;}
    if (exam == null) {
      if (other.exam != null)
        {return false;}
    } else if (!exam.equals(other.exam))
      {return false;}
    if (examSaved == null) {
      if (other.examSaved != null)
        {return false;}
    } else if (!examSaved.equals(other.examSaved))
      {return false;}
    if (soapNoteSaved == null) {
      if (other.soapNoteSaved != null)
        {return false;}
    } else if (!soapNoteSaved.equals(other.soapNoteSaved))
      {return false;}
    if (followUp == null) {
      if (other.followUp != null)
        {return false;}
    } else if (!followUp.equals(other.followUp))
      {return false;}
    if (followUpSaved == null) {
      if (other.followUpSaved != null)
        {return false;}
    } else if (!followUpSaved.equals(other.followUpSaved))
      {return false;}
    if (histSaved == null) {
      if (other.histSaved != null)
        {return false;}
    } else if (!histSaved.equals(other.histSaved))
      {return false;}
    if (lab == null) {
      if (other.lab != null)
        {return false;}
    } else if (!lab.equals(other.lab))
      {return false;}
    if (lockStatus == null) {
      if (other.lockStatus != null)
        {return false;}
    } else if (!lockStatus.equals(other.lockStatus))
      {return false;}
    if (notes == null) {
      if (other.notes != null)
        {return false;}
    } else if (!notes.equals(other.notes))
      {return false;}
    if (obgyn == null) {
      if (other.obgyn != null)
        {return false;}
    } else if (!obgyn.equals(other.obgyn))
      {return false;}
    if (obgynSaved == null) {
      if (other.obgynSaved != null)
        {return false;}
    } else if (!obgynSaved.equals(other.obgynSaved))
      {return false;}
    if (patient == null) {
      if (other.patient != null)
        {return false;}
    } else if (!patient.equals(other.patient))
      {return false;}
    if (pfshSaved == null) {
      if (other.pfshSaved != null)
        {return false;}
    } else if (!pfshSaved.equals(other.pfshSaved))
      {return false;}
    if (supp == null) {
      if (other.supp != null)
        {return false;}
    } else if (!supp.equals(other.supp))
      {return false;}
    if (suppSaved == null) {
      if (other.suppSaved != null)
        {return false;}
    } else if (!suppSaved.equals(other.suppSaved))
      {return false;}
    if (vitals == null) {
      if (other.vitals != null)
        {return false;}
    } else if (!vitals.equals(other.vitals))
      {return false;}
    if (vitalsSaved == null) {
      if (other.vitalsSaved != null)
        {return false;}
    } else if (!vitalsSaved.equals(other.vitalsSaved))
      {return false;}
    return true;
  }

  @Override
  public String toString() {
    return "Encounter [date=" + date + ", patient=" + patient
        + ", clinician=" + clinician + ", encounterType="
        + encounterType + ", cc=" + cc + ", vitals=" + vitals
        + ", supp=" + supp + ", exam=" + exam + ", lab=" + lab
        + ", obgyn=" + obgyn + ", followUp=" + followUp
        + ", lockStatus=" + lockStatus + ", completed=" + completed
        + ", notes=" + notes + ", demoSaved="
        + demoSaved + ", vitalsSaved=" + vitalsSaved
        + ", soapNoteSaved=" + soapNoteSaved + ", ccSaved=" + ccSaved
        + ", obgynSaved=" + obgynSaved + ", pfshSaved=" + pfshSaved
        + ", suppSaved=" + suppSaved + ", histSaved=" + histSaved
        + ", examSaved=" + examSaved + ", followUpSaved="
        + followUpSaved + "]";
  }

}
