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
@Table(name = "soap_note")
public class SOAPNote extends BaseEntity implements Serializable {
  private static final long serialVersionUID = -952472298193805980L;
  
  private Date date;
  private Integer encounterId;
  private Integer patientId;
  private Integer clinicianId;
  private String subjective;
  private String objective;
  private String assessment;
  private String plan;

  public SOAPNote() {
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

  @Column(name = "subjective", columnDefinition="text")
  public String getSubjective() { return subjective; }
  public void setSubjective(String subjective) { this.subjective = subjective; }

  @Column(name = "objective", columnDefinition="text")
  public String getObjective() { return objective; }
  public void setObjective(String objective) { this.objective = objective; }

  @Column(name = "assessment", columnDefinition="text")
  public String getAssessment() { return assessment; }
  public void setAssessment(String assessment) { this.assessment = assessment; }

  @Column(name = "plan", columnDefinition="text")
  public String getPlan() { return plan; }
  public void setPlan(String plan) { this.plan = plan; }

}
