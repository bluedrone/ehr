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
@Table(name = "dell_weightscale")
public class Weightscale extends BaseEntity implements Serializable {
  
  private static final long serialVersionUID = 804520053479817760L;
  
  private String weight;
  private String units;
  private Date date;
  
  @Column(name = "weight")
  public String getWeight() { return weight; }
  public void setWeight(String weight) { this.weight = weight; }
  
  @Column(name = "units")
  public String getUnits() { return units; }
  public void setUnits(String units) { this.units = units; }
  
  @Column(name = "date")
  public Date getDate() { return date; }
  public void setDate(Date date) { this.date = date; }
  
}