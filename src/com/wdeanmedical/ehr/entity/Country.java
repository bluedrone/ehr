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
@Table(name = "country")
public class Country extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -2776887697346418192L;
  private String name;
  private String code;

  public Country() {
  }

  @Column(name = "name")
  @Basic(optional = false)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "code")
  @Basic(optional = true)
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((code == null) ? 0 : code.hashCode());
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
    Country other = (Country) obj;
    if (code == null) {
      if (other.code != null)
        {return false;}
    } else if (!code.equals(other.code))
      {return false;}
    if (name == null) {
      if (other.name != null)
        {return false;}
    } else if (!name.equals(other.name))
      {return false;}
    return true;
  }

  @Override
  public String toString() {
    return "Country [name=" + name + ", code=" + code + "]";
  }

}
