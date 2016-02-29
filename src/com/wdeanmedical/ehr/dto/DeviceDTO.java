/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */
 
package com.wdeanmedical.ehr.dto;

import com.wdeanmedical.ehr.entity.dell.BP;
import com.wdeanmedical.ehr.entity.dell.Glucose;
import com.wdeanmedical.ehr.entity.dell.IOTActivity;
import com.wdeanmedical.ehr.entity.dell.Phynotes;
import com.wdeanmedical.ehr.entity.dell.Pulse;
import com.wdeanmedical.ehr.entity.dell.Weightscale;

public class DeviceDTO extends BooleanResultDTO {

  private BP bp;
  private Pulse pulse;
  private Glucose glucose;
  private Weightscale weightscale;
  private IOTActivity activity;
  private String phynotes;
  private Phynotes phynotesObject;
  
  
  public BP getBp() { return bp; }
  public void setBp(BP bp) { this.bp = bp; }
  
  public Pulse getPulse() { return pulse; }
  public void setPulse(Pulse pulse) { this.pulse = pulse; }
  
  public Glucose getGlucose() { return glucose; }
  public void setGlucose(Glucose glucose) { this.glucose = glucose; }
  
  public Weightscale getWeightscale() { return weightscale; }
  public void setWeightscale(Weightscale weightscale) { this.weightscale = weightscale; }
  
  public IOTActivity getActivity() { return activity; }
  public void setActivity(IOTActivity activity) { this.activity = activity; }
  
  public String getPhynotes() { return phynotes; }
  public void setPhynotes(String phynotes) { this.phynotes = phynotes; }
  
  public Phynotes getPhynotesObject() { return phynotesObject; }
  public void setPhynotesObject(Phynotes phynotesObject) { this.phynotesObject = phynotesObject; }
  
}
