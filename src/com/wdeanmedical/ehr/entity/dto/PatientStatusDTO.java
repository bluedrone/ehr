/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

package com.wdeanmedical.ehr.entity.dto;

public class PatientStatusDTO {

  public static final int ACTIVE = 1;
  public static final int INACTIVE = 2;
  public static final int DECEASED = 3;
  public static final int PURGED = 4;

  private String name;

  public PatientStatusDTO() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "PatientStatusDTO [name=" + name + "]";
  }

}
