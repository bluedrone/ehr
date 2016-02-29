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
@Table(name = "dell_activity")
public class IOTActivity extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 5761220011990424983L;
  
  private String activity;
  private String units;
  private Date date;
  
  @Column(name = "activity")
  public String getActivity() { return activity; }
  public void setActivity(String activity) { this.activity = activity; }
  
  @Column(name = "units")
  public String getUnits() { return units; }
  public void setUnits(String units) { this.units = units; }
  
  @Column(name = "date")
  public Date getDate() { return date; }
  public void setDate(Date date) { this.date = date; }
  
}
