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

@Entity
@Table(name = "module")
public class Module extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1654238601772562605L;

  public static final Integer EHR = 1;
  public static final Integer PM = 2;
  public static final Integer PORTAL = 3;

  private String moduleType;

  @Column(name = "module_type")
  public String getModuleType() {
    return moduleType;
  }

  public void setModuleType(String moduleType) {
    this.moduleType = moduleType;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((moduleType == null) ? 0 : moduleType.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;
    Module other = (Module) obj;
    if (moduleType == null) {
      if (other.moduleType != null)
        return false;
    } else if (!moduleType.equals(other.moduleType))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "Module [moduleType=" + moduleType + "]";
  }

}
