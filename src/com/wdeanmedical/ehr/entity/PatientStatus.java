/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */
 
package com.wdeanmedical.ehr.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.wdeanmedical.ehr.entity.BaseEntity;

@Entity
@Table(name = "patient_status")
public class PatientStatus extends BaseEntity implements Serializable {
  private static final long serialVersionUID = -6574632022145191012L;

  public static final int ACTIVE = 1;
  public static final int INACTIVE = 2;
  public static final int DECEASED = 3;
  public static final int PURGED = 4;

  private String name;

  public PatientStatus() {
  }

  @Column(name = "name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

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
    PatientStatus other = (PatientStatus) obj;
    if (name == null) {
      if (other.name != null)
        {return false;}
    } else if (!name.equals(other.name))
      {return false;}
    return true;
  }

  @Override
  public String toString() {
    return "PatientStatus [name=" + name + "]";
  }

}
