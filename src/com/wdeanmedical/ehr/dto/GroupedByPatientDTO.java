package com.wdeanmedical.ehr.dto;

import java.util.List;

import com.wdeanmedical.ehr.entity.ActivityLog;
import com.wdeanmedical.ehr.entity.Patient;

public class GroupedByPatientDTO {
  
  private Patient patient;
  
  private List<ActivityLogDTO> activityLog;

  public Patient getPatient() {
    return patient;
  }

  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  public List<ActivityLogDTO> getActivityLog() {
    return activityLog;
  }

  public void setActivityLog(List<ActivityLogDTO> activityLog) {
    this.activityLog = activityLog;
  }
}
