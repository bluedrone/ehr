package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "race")
public class Race extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -5348893393712684101L;
  private String name;
    

   public Race() {
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
    return "com.wdeanmedical.ehr.entity.Race[id=" + getId() + "]";
  }

    
}
