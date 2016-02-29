/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */
 
package com.wdeanmedical.ehr.entity.dell;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.wdeanmedical.ehr.entity.BaseEntity;

@Entity
@Table(name = "dell_device_data")
public class DeviceData extends BaseEntity implements Serializable {


  private Date date;
  private Integer patientId;
  private String bp;
  private String pulse;
  private String glucose;
  private String weightscale;
  private String activity;
  private String phynotes;
  

  public DeviceData() {
  }


  public Date getDate() { return date; }
  public void setDate(Date date) { this.date = date; }

  public Integer getPatientId() { return patientId; }
  public void setPatientId(Integer patientId) { this.patientId = patientId; }

  public String getBp() { return bp; }
  public void setBp(String bp) { this.bp = bp; }

  public String getPulse() { return pulse; }
  public void setPulse(String pulse) { this.pulse = pulse; }

  public String getGlucose() { return glucose; }
  public void setGlucose(String glucose) { this.glucose = glucose; }

  public String getWeightscale() { return weightscale; }
  public void setWeightscale(String weightscale) { this.weightscale = weightscale; }
  
  public String getActivity() { return activity; }
  public void setActivity(String activity) { this.activity = activity; }

  public String getPhynotes() { return phynotes; }
  public void setPhynotes(String phynotes) { this.phynotes = phynotes; }

}
