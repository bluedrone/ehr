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
@Table(name = "dell_bp")
public class BP extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 8387590616646502675L;
  
  private String sys;
  private String dia;
  private String units;
  private Date date;
  
  @Column(name = "sys")
  public String getSys() { return sys; }
  public void setSys(String sys) { this.sys = sys; }
  
  @Column(name = "dia")
  public String getDia() { return dia; }
  public void setDia(String dia) { this.dia = dia; }  
  
  @Column(name = "units")
  public String getUnits() { return units; }
  public void setUnits(String units) { this.units = units; }
  
  @Column(name = "date")
  public Date getDate() { return date; }
  public void setDate(Date date) { this.date = date; }
  
}
