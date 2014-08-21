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
@Table(name = "us_state")
public class USState extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 4749957909713623155L;
  private String name;
  private String code;

  public USState() {
  }

  @Column(name = "name")
  @Basic(optional = false)
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  
  @Column(name = "code")
  public String getCode() { return code; }
  public void setCode(String code) { this.code = code; }

@Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((name == null) ? 0 : name.hashCode());
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
    USState other = (USState) obj;
    if (name == null) {
      if (other.name != null)
        {return false;}
    } else if (!name.equals(other.name))
      {return false;}
    return true;
  }

  @Override
  public String toString() {
    return "USState [name=" + name + "]";
  }

}
