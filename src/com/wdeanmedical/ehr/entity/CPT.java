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
@Table(name = "cpt")
public class CPT extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 4564544087108567122L;
  
  private String code;
  private String description;

  public CPT() {
  }

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

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      {return false;}
    if (getClass() != obj.getClass())
      {return false;}
    CPT other = (CPT) obj;
    if (code == null) {
      if (other.code != null)
        {return false;}
    } else if (!code.equals(other.code))
      {return false;}
    if (description == null) {
      if (other.description != null)
        {return false;}
    } else if (!description.equals(other.description))
      {return false;}
    return true;
  }

  @Override
  public String toString() {
    return "CPT [description=" + description + ", code=" + code + "]";
  }

}
