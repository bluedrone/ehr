/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

package com.wdeanmedical.ehr.dto;

import java.util.Date;

public class ActivityLogDTO {

  private String userName;
  private String patientName;
  private Date timePerformed;
  private String clinicianName;
  private Integer encounterId;
  private String fieldName;
  private String activity;
  private String module;

  public ActivityLogDTO() {
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

  public String getModule() {
    return module;
  }

  public void setModule(String module) {
    this.module = module;
  }

}
