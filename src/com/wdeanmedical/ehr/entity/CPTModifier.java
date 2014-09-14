/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */
 
package com.wdeanmedical.ehr.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "cpt_modifier")
public class CPTModifier extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 3465057457460175412L;

  private String code;
  private String shortDescription;
  private String description;

  public CPTModifier() {
  }
  
  
  @Column(name = "short_description")
  public String getShortDescription() { return shortDescription; }
  public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }

  @Column(name = "description")
  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }

  @Column(name = "code")
  public String getCode() { return code; }
  public void setCode(String code) { this.code = code; }
  
  
  

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((code == null) ? 0 : code.hashCode());
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    return result;
  }


}
