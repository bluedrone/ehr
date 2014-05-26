/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

package com.wdeanmedical.ehr.entity.dto;

public class PatientMessageTypeDTO {
  private String name;
  public static final int GENERAL = 1;
  public static final int MEDICAL_ADVICE = 2;
  public static final int RX_RENEWAL = 3;
  public static final int APPT_REQUEST = 4;
  public static final int INITIAL_APPT_REQUEST = 5;

  public PatientMessageTypeDTO() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "PatientMessageTypeDTO [name=" + name + "]";
  }

}
