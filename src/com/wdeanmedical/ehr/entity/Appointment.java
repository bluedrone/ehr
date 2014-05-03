package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
  
  public Appointment() {
  }
  
  
  @JoinColumn(name = "patient", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public Patient getPatient() { return patient; }
  public void setPatient(Patient patient) { this.patient = patient; }

  @JoinColumn(name = "clinician", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public Clinician getClinician() { return clinician; }
  public void setClinician(Clinician clinician) { this.clinician = clinician; }
  
  @JoinColumn(name = "department", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public Department getDepartment() { return department; }
  public void setDepartment(Department department) { this.department = department; }


  @JoinColumn(name = "appointment_type", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public AppointmentType getAppointmentType() { return appointmentType; }
  public void setAppointmentType(AppointmentType appointmentType) { this.appointmentType = appointmentType; }

  @Column(name = "start_time")
  public Date getStartTime() { return startTime; }
  public void setStartTime(Date startTime) { this.startTime = startTime; }

  @Column(name = "end_time")
  public Date getEndTime() { return endTime; }
  public void setEndTime(Date endTime) { this.endTime = endTime; }

  @Column(name = "day_of_week")
  public Integer getDayOfWeek() { return dayOfWeek; }
  public void setDayOfWeek(Integer dayOfWeek) { this.dayOfWeek = dayOfWeek; }

  @Column(name = "override")
  public boolean isOverride() { return override; }
  public void setOverride(boolean override) { this.override = override; }
  
  

}
