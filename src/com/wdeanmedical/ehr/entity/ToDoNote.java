package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "to_do_note")
public class ToDoNote extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 124835074057048599L;
  
  private Integer patientId;
  private Integer clinicianId;
  private Date date;
  private String subject;
  private String content;
  

  public ToDoNote() {
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

  @Column(name = "content")
  public String getContent() { return content; }
  public void setContent(String content) { this.content = content;
  }
  
  @Column(name = "subject")
  public String getSubject() { return subject; }
  public void setSubject(String subject) { this.subject = subject; }

}
