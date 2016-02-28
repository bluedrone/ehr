/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */
 
package com.wdeanmedical.ehr.dto;

import com.wdeanmedical.ehr.entity.Activity;
import com.wdeanmedical.ehr.model.dell.BP;
import com.wdeanmedical.ehr.model.dell.Glucose;
import com.wdeanmedical.ehr.model.dell.Pulse;
import com.wdeanmedical.ehr.model.dell.Weightscale;

public class DeviceDTO extends BooleanResultDTO {

  public BP bp;
  public Pulse pulse;
  public Glucose glucose;
  public Weightscale weightscale;
  public Activity activity;
  public String phynotes;
}
