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
  
  private Patient patient;
  private Clinician clinician;
  private Date date;
  private String subject;
  private String content;
  

  public ToDoNote() {
  }
  
  @JoinColumn(name = "patient", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public Patient getPatient() { return patient; }
  public void setPatient(Patient patient) { this.patient = patient; }
  
  @Column(name = "date")
  public Date getDate() { return date; }
  public void setDate(Date date) { this.date = date; }

  @JoinColumn(name = "clinician", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public Clinician getClinician() { return clinician; }
  public void setClinician(Clinician clinician) { this.clinician = clinician; }

  @Column(name = "content")
  public String getContent() { return content; }
  public void setContent(String content) { this.content = content;
  }
  
  @Column(name = "subject")
  public String getSubject() { return subject; }
  public void setSubject(String subject) { this.subject = subject; }

}
