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
@Table(name = "dell_phynotes")
public class Phynotes extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 7457771701663194132L;
  
  private String phynotes;
  private Date date;
  
  @Column(name = "phynotes")
  public String getPhynotes() { return phynotes; }
  public void setPhynotes(String phynotes) { this.phynotes = phynotes; }
  
  @Column(name = "date")
  public Date getDate() { return date; }
  public void setDate(Date date) { this.date = date; }
  
}
