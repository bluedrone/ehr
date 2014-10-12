/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

package com.wdeanmedical.ehr.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.wdeanmedical.ehr.entity.Activity;

public class ActivityLogDTO extends AdminDTO{

  private String dateFrom;
  private String dateTo;
  private String userName;
  private String patientName;
  private Date timePerformed;
  private String clinicianName;
  private Integer encounterId;
  private String fieldName;
  private String activity;
  private Integer activityId;
  private String module;
  public Map<String, Set<String>> activityLogClinicianSearchTypeAheads = new HashMap<String, Set<String>>();
  public Map<String, Set<String>> activityLogPatientSearchTypeAheads = new HashMap<String, Set<String>>();
  public Map<String, List<Activity>> clinicianActivityList = new HashMap<String, List<Activity>>();

  public ActivityLogDTO() {
  }  

  public String getDateFrom() {
    return dateFrom;
  }

  public void setDateFrom(String dateFrom) {
    this.dateFrom = dateFrom;
  }

  public String getDateTo() {
    return dateTo;
  }

  public void setDateTo(String dateTo) {
    this.dateTo = dateTo;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPatientName() {
    return patientName;
  }

  public void setPatientName(String patientName) {
    this.patientName = patientName;
  }

  public Date getTimePerformed() {
    return timePerformed;
  }

  public void setTimePerformed(Date timePerformed) {
    this.timePerformed = timePerformed;
  }

  public String getClinicianName() {
    return clinicianName;
  }

  public void setClinicianName(String clinicianName) {
    this.clinicianName = clinicianName;
  }

  public Integer getEncounterId() {
    return encounterId;
  }

  public void setEncounterId(Integer encounterId) {
    this.encounterId = encounterId;
  }

  public String getFieldName() {
    return fieldName;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getActivity() {
    return activity;
  }

  public void setActivity(String activity) {
    this.activity = activity;
  }  

  public Integer getActivityId() {
    return activityId;
  }

  public void setActivityId(Integer activityId) {
    this.activityId = activityId;
  }

  public String getModule() {
    return module;
  }

  public void setModule(String module) {
    this.module = module;
  }
}
