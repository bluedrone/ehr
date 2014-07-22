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
@Table(name = "appointment")
public class Appointment extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1462745762564975233L;

  private Date startTime;
  private Date endTime;
  private Integer dayOfWeek;
  private boolean override;
  private Patient patient;
  private Clinician clinician;
  private Department department;
  private AppointmentType appointmentType;
  private String title;
  private String desc;

  public Appointment() {
  }

  @JoinColumn(name = "patient", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public Patient getPatient() {
    return patient;
  }

  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  @JoinColumn(name = "clinician", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public Clinician getClinician() {
    return clinician;
  }

  public void setClinician(Clinician clinician) {
    this.clinician = clinician;
  }

  @JoinColumn(name = "department", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public Department getDepartment() {
    return department;
  }

  public void setDepartment(Department department) {
    this.department = department;
  }

  @JoinColumn(name = "appointment_type", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public AppointmentType getAppointmentType() {
    return appointmentType;
  }

  public void setAppointmentType(AppointmentType appointmentType) {
    this.appointmentType = appointmentType;
  }

  @Column(name = "start_time")
  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  @Column(name = "end_time")
  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  @Column(name = "day_of_week")
  public Integer getDayOfWeek() {
    return dayOfWeek;
  }

  public void setDayOfWeek(Integer dayOfWeek) {
    this.dayOfWeek = dayOfWeek;
  }

  @Column(name = "override")
  public boolean isOverride() {
    return override;
  }

  public void setOverride(boolean override) {
    this.override = override;
  }

  @Column(name = "title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Column(name = "description")
  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((appointmentType == null) ? 0 : appointmentType.hashCode());
    result = prime * result + ((clinician == null) ? 0 : clinician.hashCode());
    result = prime * result + ((dayOfWeek == null) ? 0 : dayOfWeek.hashCode());
    result = prime * result + ((department == null) ? 0 : department.hashCode());
    result = prime * result + ((desc == null) ? 0 : desc.hashCode());
    result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
    result = prime * result + (override ? 1231 : 1237);
    result = prime * result + ((patient == null) ? 0 : patient.hashCode());
    result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
    result = prime * result + ((title == null) ? 0 : title.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;
    Appointment other = (Appointment) obj;
    if (appointmentType == null) {
      if (other.appointmentType != null)
        return false;
    } else if (!appointmentType.equals(other.appointmentType))
      return false;
    if (clinician == null) {
      if (other.clinician != null)
        return false;
    } else if (!clinician.equals(other.clinician))
      return false;
    if (dayOfWeek == null) {
      if (other.dayOfWeek != null)
        return false;
    } else if (!dayOfWeek.equals(other.dayOfWeek))
      return false;
    if (department == null) {
      if (other.department != null)
        return false;
    } else if (!department.equals(other.department))
      return false;
    if (desc == null) {
      if (other.desc != null)
        return false;
    } else if (!desc.equals(other.desc))
      return false;
    if (endTime == null) {
      if (other.endTime != null)
        return false;
    } else if (!endTime.equals(other.endTime))
      return false;
    if (override != other.override)
      return false;
    if (patient == null) {
      if (other.patient != null)
        return false;
    } else if (!patient.equals(other.patient))
      return false;
    if (startTime == null) {
      if (other.startTime != null)
        return false;
    } else if (!startTime.equals(other.startTime))
      return false;
    if (title == null) {
      if (other.title != null)
        return false;
    } else if (!title.equals(other.title))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "Appointment [startTime=" + startTime + ", endTime=" + endTime + ", dayOfWeek=" + dayOfWeek + ", override="
        + override + ", patient=" + patient + ", clinician=" + clinician + ", department=" + department
        + ", appointmentType=" + appointmentType + ", title=" + title + ", desc=" + desc + "]";
  }

}
