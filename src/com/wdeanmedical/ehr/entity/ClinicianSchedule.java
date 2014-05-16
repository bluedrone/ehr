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
@Table(name = "clinician_schedule")
public class ClinicianSchedule extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -3809829904313485829L;

  private Patient patient;
  private Clinician clinician;
  private Date date;
  private String length;
  private float age;
  private Gender gender;
  private String reason;
  private String comments;
  private String status;
  private String patientLocation;
  private String room;
  private boolean checkedIn;
  private float waitTime;
  private String progressNoteStatus;

  public ClinicianSchedule() {
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

  @Column(name = "length")
  public String getLength() {
    return length;
  }

  public void setLength(String length) {
    this.length = length;
  }

  @Column(name = "age")
  public float getAge() {
    return age;
  }

  public void setAge(float age) {
    this.age = age;
  }

  @JoinColumn(name = "gender", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  @Column(name = "reason")
  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  @Column(name = "comments")
  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  @Column(name = "status")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Column(name = "patient_location")
  public String getPatientLocation() {
    return patientLocation;
  }

  public void setPatientLocation(String patientLocation) {
    this.patientLocation = patientLocation;
  }

  @Column(name = "room")
  public String getRoom() {
    return room;
  }

  public void setRoom(String room) {
    this.room = room;
  }

  @Column(name = "checked_in")
  public boolean isCheckedIn() {
    return checkedIn;
  }

  public void setCheckedIn(boolean checkedIn) {
    this.checkedIn = checkedIn;
  }

  @Column(name = "wait_time")
  public float getWaitTime() {
    return waitTime;
  }

  public void setWaitTime(float waitTime) {
    this.waitTime = waitTime;
  }

  @Column(name = "progress_note_status")
  public String getProgressNoteStatus() {
    return progressNoteStatus;
  }

  public void setProgressNoteStatus(String progressNoteStatus) {
    this.progressNoteStatus = progressNoteStatus;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + Float.floatToIntBits(age);
    result = prime * result + (checkedIn ? 1231 : 1237);
    result = prime * result
        + ((clinician == null) ? 0 : clinician.hashCode());
    result = prime * result
        + ((comments == null) ? 0 : comments.hashCode());
    result = prime * result + ((date == null) ? 0 : date.hashCode());
    result = prime * result + ((gender == null) ? 0 : gender.hashCode());
    result = prime * result + ((length == null) ? 0 : length.hashCode());
    result = prime * result + ((patient == null) ? 0 : patient.hashCode());
    result = prime * result
        + ((patientLocation == null) ? 0 : patientLocation.hashCode());
    result = prime
        * result
        + ((progressNoteStatus == null) ? 0 : progressNoteStatus
            .hashCode());
    result = prime * result + ((reason == null) ? 0 : reason.hashCode());
    result = prime * result + ((room == null) ? 0 : room.hashCode());
    result = prime * result + ((status == null) ? 0 : status.hashCode());
    result = prime * result + Float.floatToIntBits(waitTime);
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
    ClinicianSchedule other = (ClinicianSchedule) obj;
    if (Float.floatToIntBits(age) != Float.floatToIntBits(other.age))
      {return false;}
    if (checkedIn != other.checkedIn)
      {return false;}
    if (clinician == null) {
      if (other.clinician != null)
        {return false;}
    } else if (!clinician.equals(other.clinician))
      {return false;}
    if (comments == null) {
      if (other.comments != null)
        {return false;}
    } else if (!comments.equals(other.comments))
      {return false;}
    if (date == null) {
      if (other.date != null)
        {return false;}
    } else if (!date.equals(other.date))
      {return false;}
    if (gender == null) {
      if (other.gender != null)
        {return false;}
    } else if (!gender.equals(other.gender))
      {return false;}
    if (length == null) {
      if (other.length != null)
        {return false;}
    } else if (!length.equals(other.length))
      {return false;}
    if (patient == null) {
      if (other.patient != null)
        {return false;}
    } else if (!patient.equals(other.patient))
      {return false;}
    if (patientLocation == null) {
      if (other.patientLocation != null)
        {return false;}
    } else if (!patientLocation.equals(other.patientLocation))
      {return false;}
    if (progressNoteStatus == null) {
      if (other.progressNoteStatus != null)
        {return false;}
    } else if (!progressNoteStatus.equals(other.progressNoteStatus))
      {return false;}
    if (reason == null) {
      if (other.reason != null)
        {return false;}
    } else if (!reason.equals(other.reason))
      {return false;}
    if (room == null) {
      if (other.room != null)
        {return false;}
    } else if (!room.equals(other.room))
      {return false;}
    if (status == null) {
      if (other.status != null)
        {return false;}
    } else if (!status.equals(other.status))
      {return false;}
    if (Float.floatToIntBits(waitTime) != Float
        .floatToIntBits(other.waitTime))
      {return false;}
    return true;
  }

  @Override
  public String toString() {
    return "ClinicianSchedule [patient=" + patient + ", clinician="
        + clinician + ", date=" + date + ", length=" + length
        + ", age=" + age + ", gender=" + gender + ", reason=" + reason
        + ", comments=" + comments + ", status=" + status
        + ", patientLocation=" + patientLocation + ", room=" + room
        + ", checkedIn=" + checkedIn + ", waitTime=" + waitTime
        + ", progressNoteStatus=" + progressNoteStatus + "]";
  }

}
