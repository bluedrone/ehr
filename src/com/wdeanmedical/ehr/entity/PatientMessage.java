package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "patient_message")
public class PatientMessage extends BaseEntity implements Serializable {
  private static final long serialVersionUID = -568848644986949555L;
  private Patient patient;
  private String subject;
  private Date date;
  private String from;
  private Clinician clinician;
  private Boolean fromClinician;
  private Boolean readByRecipient;
  private String content;
  private PatientMessageType patientMessageType;
  

  public PatientMessage() {
  }
  
  
  @JoinColumn(name = "patient", referencedColumnName = "id")
  @ManyToOne(optional = true)
  public Patient getPatient() { return patient; }
  public void setPatient(Patient patient) { this.patient = patient; }
  
  @Column(name = "subject")
  public String getSubject() { return subject; }
  public void setSubject(String subject) { this.subject = subject; }
  
  @Column(name = "date")
  public Date getDate() { return date; }
  public void setDate(Date date) { this.date = date; }

  @JoinColumn(name = "clinician", referencedColumnName = "id")
  @ManyToOne(optional = true)
  public Clinician getClinician() { return clinician; }
  public void setClinician(Clinician clinician) { this.clinician = clinician; }
  
  @Column(name = "sent_from")
  public String getFrom() { return from; }
  public void setFrom(String from) { this.from = from; }

  @Column(name = "content")
  public String getContent() { return content; }
  public void setContent(String content) { this.content = content; }

  @Column(name = "from_clinician")
  public Boolean getFromClinician() { return fromClinician; }
  public void setFromClinician(Boolean fromClinician) { this.fromClinician = fromClinician; }
  
  @JoinColumn(name = "patient_message_type", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public PatientMessageType getPatientMessageType() { return patientMessageType; }
  public void setPatientMessageType(PatientMessageType patientMessageType) { this.patientMessageType = patientMessageType; }

  @Column(name = "read_by_recipient")
  public Boolean getReadByRecipient() { return readByRecipient; }
  public void setReadByRecipient(Boolean readByRecipient) { this.readByRecipient = readByRecipient; }

}
