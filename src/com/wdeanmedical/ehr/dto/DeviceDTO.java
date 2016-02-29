/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */
 
package com.wdeanmedical.ehr.dto;

import com.wdeanmedical.ehr.entity.Activity;
import com.wdeanmedical.ehr.entity.dell.BP;
import com.wdeanmedical.ehr.entity.dell.Glucose;
import com.wdeanmedical.ehr.entity.dell.Pulse;
import com.wdeanmedical.ehr.entity.dell.Weightscale;

public class DeviceDTO extends BooleanResultDTO {

  private BP bp;
  private Pulse pulse;
  private Glucose glucose;
  private Weightscale weightscale;
  private Activity activity;
  private String phynotes;
  
  
  public BP getBp() { return bp; }
  public void setBp(BP bp) { this.bp = bp; }
  
  public Pulse getPulse() { return pulse; }
  public void setPulse(Pulse pulse) { this.pulse = pulse; }
  
  public Glucose getGlucose() { return glucose; }
  public void setGlucose(Glucose glucose) { this.glucose = glucose; }
  
  public Weightscale getWeightscale() { return weightscale; }
  public void setWeightscale(Weightscale weightscale) { this.weightscale = weightscale; }
  
  public Activity getActivity() { return activity; }
  public void setActivity(Activity activity) { this.activity = activity; }
  
  public String getPhynotes() { return phynotes; }
  public void setPhynotes(String phynotes) { this.phynotes = phynotes; }
}
