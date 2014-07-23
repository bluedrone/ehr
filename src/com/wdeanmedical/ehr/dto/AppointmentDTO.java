package com.wdeanmedical.ehr.dto;

import java.util.Date;
import com.wdeanmedical.ehr.entity.Appointment;


public class AppointmentDTO extends AuthorizedDTO {
	private int id;
	private Date newApptStartTime;
	private Date newApptEndTime;
	private String startTime;
	private String endTime;
	private Integer dayOfWeek;
	private boolean override;
	private int patient;
	private int clinician;
	private int department;
	private int appointmentType;
	private String title;
	private String desc;
	private Integer apptLengthInMinutes;
	private Appointment appointment;

	public AppointmentDTO() {
	}

	
	public int getId() { return id; }
	public void setId(int id) {	this.id = id; }

	public String getStartTime() { return startTime; }
	public void setStartTime(String startTime) { this.startTime = startTime;}

	public String getEndTime() { return endTime; }
	public void setEndTime(String endTime) { this.endTime = endTime; }

	public Integer getDayOfWeek() {	return dayOfWeek; }
	public void setDayOfWeek(Integer dayOfWeek) { this.dayOfWeek = dayOfWeek; }

	public boolean isOverride() { return override; }
	public void setOverride(boolean override) { this.override = override; }

	public int getPatient() { return patient; }
	public void setPatient(int patient) { this.patient = patient; }

	public int getClinician() {	return clinician; }
	public void setClinician(int clinician) { this.clinician = clinician; }

	public int getDepartment() { return department; }
	public void setDepartment(int department) { this.department = department; }

	public int getAppointmentType() { return appointmentType;	}
	public void setAppointmentType(int appointmentType) { this.appointmentType = appointmentType; }

	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }

	public String getDesc() { return desc; } 
	public void setDesc(String desc) { this.desc = desc; }

	public Integer getApptLengthInMinutes() { return apptLengthInMinutes; }
	public void setApptLengthInMinutes(Integer apptLengthInMinutes) { this.apptLengthInMinutes = apptLengthInMinutes; }

	public Date getNewApptStartTime() { return newApptStartTime; }
	public void setNewApptStartTime(Date newApptStartTime) { this.newApptStartTime = newApptStartTime; }

	public Date getNewApptEndTime() { return newApptEndTime; }
	public void setNewApptEndTime(Date newApptEndTime) { this.newApptEndTime = newApptEndTime; }

	public Appointment getAppointment() { return appointment; }
	public void setAppointment(Appointment appointment) { this.appointment = appointment; }
}
