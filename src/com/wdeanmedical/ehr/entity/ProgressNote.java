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
@Table(name = "progress_note")
public class ProgressNote extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 896941564059182981L;
  private Patient patient;
  private Clinician clinician;
  private Date date;
  private String subject;
  private String content;
  private Boolean completed = false;

  public ProgressNote() {
  }

  @JoinColumn(name = "patient", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public Patient getPatient() {
    return patient;
  }

  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  @Column(name = "date")
  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  @JoinColumn(name = "clinician", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public Clinician getClinician() {
    return clinician;
  }

  public void setClinician(Clinician clinician) {
    this.clinician = clinician;
  }

  @Column(name = "content")
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Column(name = "subject")
  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  @Column(name = "completed")
  public Boolean getCompleted() {
    return completed;
  }

  public void setCompleted(Boolean completed) {
    this.completed = completed;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result
        + ((clinician == null) ? 0 : clinician.hashCode());
    result = prime * result
        + ((completed == null) ? 0 : completed.hashCode());
    result = prime * result + ((content == null) ? 0 : content.hashCode());
    result = prime * result + ((date == null) ? 0 : date.hashCode());
    result = prime * result + ((patient == null) ? 0 : patient.hashCode());
    result = prime * result + ((subject == null) ? 0 : subject.hashCode());
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
    ProgressNote other = (ProgressNote) obj;
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
    if (content == null) {
      if (other.content != null)
        {return false;}
    } else if (!content.equals(other.content))
      {return false;}
    if (date == null) {
      if (other.date != null)
        {return false;}
    } else if (!date.equals(other.date))
      {return false;}
    if (patient == null) {
      if (other.patient != null)
        {return false;}
    } else if (!patient.equals(other.patient))
      {return false;}
    if (subject == null) {
      if (other.subject != null)
        {return false;}
    } else if (!subject.equals(other.subject))
      {return false;}
    return true;
  }

  @Override
  public String toString() {
    return "ProgressNote [patient=" + patient + ", clinician=" + clinician
        + ", date=" + date + ", subject=" + subject + ", content="
        + content + ", completed=" + completed + "]";
  }

}
