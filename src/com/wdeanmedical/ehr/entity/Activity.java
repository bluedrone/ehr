/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */
 
package com.wdeanmedical.ehr.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "activity")
public class Activity  extends BaseEntity implements Serializable{
  
  private static final long serialVersionUID = -1794733487783889486L;
  
  public static final Integer LOGIN  = 1;
  public static final Integer VIEW_PATIENT = 2;
  public static final Integer VIEW_PATIENT_ENCOUNTER = 3;
  public static final Integer EDIT_PATIENT_FIELD = 4;
  public static final Integer EDIT_PATIENT_ENCOUNTER_FIELD = 5;
  public static final Integer CREATE_ENCOUNTER = 6;
  public static final Integer LOGOUT = 7;
  public static final Integer DELETE_PATIENT = 8;
  
  private String activityType;

  @Column(name = "activity_type")
  public String getActivityType() {
    return activityType;
  }

  public void setActivityType(String activityType) {
    this.activityType = activityType;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result
        + ((activityType == null) ? 0 : activityType.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) { return true; }
    if (!super.equals(obj)) { return false; }
    if (getClass() != obj.getClass()) { return false; }
    Activity other = (Activity) obj;
    if (activityType == null) {
      if (other.activityType != null) { return false; }
    } 
    else if (!activityType.equals(other.activityType)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Activity [activityType=" + activityType + "]";
  }  

}
