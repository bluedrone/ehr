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
@Table(name = "exam")
public class Exam extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1462745762564975233L;

  private Date date;
  private Integer encounterId;
  private Integer patientId;
  private Integer clinicianId;
  private String hs;
  private String heartRhythm;
  private String diagnosis;
  private String treatmentPlan;
  private String diagramPath;

  public Exam() {
  }

  @Column(name = "encounter_id")
  public Integer getEncounterId() { return encounterId; }
  public void setEncounterId(Integer encounterId) { this.encounterId = encounterId; }

  @Column(name = "date")
  public Date getDate() { return date; }
  public void setDate(Date date) { this.date = date; }

  @Column(name = "patient_id")
  public Integer getPatientId() { return patientId; }
  public void setPatientId(Integer patientId) { this.patientId = patientId; }

  @Column(name = "clinician_id")
  public Integer getClinicianId() { return clinicianId; }
  public void setClinicianId(Integer clinicianId) { this.clinicianId = clinicianId; }

  @Column(name = "hs")
  public String getHs() { return hs; }
  public void setHs(String hs) { this.hs = hs; }

  @Column(name = "heart_rhythm")
  public String getHeartRhythm() { return heartRhythm; }
  public void setHeartRhythm(String heartRhythm) { this.heartRhythm = heartRhythm; }

  @Column(name = "diagnosis")
  public String getDiagnosis() { return diagnosis; }
  public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

  @Column(name = "treatment_plan")
  public String getTreatmentPlan() { return treatmentPlan; }
  public void setTreatmentPlan(String treatmentPlan) { this.treatmentPlan = treatmentPlan; }

  @Column(name = "diagram_path")
  public String getDiagramPath() { return diagramPath; }
  public void setDiagramPath(String diagramPath) { this.diagramPath = diagramPath; }



  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result
        + ((clinicianId == null) ? 0 : clinicianId.hashCode());
    result = prime * result + ((date == null) ? 0 : date.hashCode());
    result = prime * result
        + ((diagnosis == null) ? 0 : diagnosis.hashCode());
    result = prime * result
        + ((diagramPath == null) ? 0 : diagramPath.hashCode());
    result = prime * result
        + ((encounterId == null) ? 0 : encounterId.hashCode());
    result = prime * result
        + ((heartRhythm == null) ? 0 : heartRhythm.hashCode());
    result = prime * result + ((hs == null) ? 0 : hs.hashCode());
    result = prime * result
        + ((patientId == null) ? 0 : patientId.hashCode());
    result = prime * result
        + ((treatmentPlan == null) ? 0 : treatmentPlan.hashCode());
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
    Exam other = (Exam) obj;
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
    if (diagnosis == null) {
      if (other.diagnosis != null)
        {return false;}
    } else if (!diagnosis.equals(other.diagnosis))
      {return false;}
    if (diagramPath == null) {
      if (other.diagramPath != null)
        {return false;}
    } else if (!diagramPath.equals(other.diagramPath))
      {return false;}
    if (encounterId == null) {
      if (other.encounterId != null)
        {return false;}
    } else if (!encounterId.equals(other.encounterId))
      {return false;}
    if (heartRhythm == null) {
      if (other.heartRhythm != null)
        {return false;}
    } else if (!heartRhythm.equals(other.heartRhythm))
      {return false;}
    if (hs == null) {
      if (other.hs != null)
        {return false;}
    } else if (!hs.equals(other.hs))
      {return false;}
    if (patientId == null) {
      if (other.patientId != null)
        {return false;}
    } else if (!patientId.equals(other.patientId))
      {return false;}
    if (treatmentPlan == null) {
      if (other.treatmentPlan != null)
        {return false;}
    } else if (!treatmentPlan.equals(other.treatmentPlan))
      {return false;}
    return true;
  }

  @Override
  public String toString() {
    return "Exam [date=" + date + ", encounterId=" + encounterId
        + ", patientId=" + patientId + ", clinicianId=" + clinicianId
        + ", hs=" + hs + ", heartRhythm=" + heartRhythm
        + ", treatmentPlan=" + treatmentPlan 
        + ", diagramPath=" + diagramPath + "]";
  }

}
