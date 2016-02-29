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
import javax.persistence.Table;

import com.wdeanmedical.ehr.entity.BaseEntity;

@Entity
@Table(name = "dell_pulse")
public class Pulse extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 3474095315045276347L;
  
  private String rate;
  private String units;
  private Date date;
  
  @Column(name = "rate")
  public String getRate() { return rate; }
  public void setRate(String rate) { this.rate = rate; }
  
  @Column(name = "units")
  public String getUnits() { return units; }
  public void setUnits(String units) { this.units = units; }
  
  @Column(name = "date")
  public Date getDate() { return date; }
  public void setDate(Date date) { this.date = date; }
  
}