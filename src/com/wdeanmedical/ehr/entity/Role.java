package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class Role extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -8100416106575008609L;
  
  private String name;
    

   public Role() {
   }
    
  @Column(name = "name")
  @Basic(optional = false)  
   public String getName() {
     return name;
   }
   public void setName(String name) {
     this.name = name;
   }
    

	@Override
  public String toString() {
    return "org.cbmi.scheduler.entity.Role[id=" + getId() + "]";
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Role)) {
      return false;
    }
    Role other = (Role) object;
    if ((getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
      return false;
    }
    return true;
  }
    
}
