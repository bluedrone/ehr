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
    

   public USState() {
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
    return "com.wdeanmedical.ehr.entity.USState[id=" + getId() + "]";
  }

    
}
