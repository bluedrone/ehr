package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "ethnicity")
public class Ethnicity extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -4178802006803752787L;
  private String name;
    

   public Ethnicity() {
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
    return "com.wdeanmedical.ehr.Ethnicity[id=" + getId() + "]";
  }

    
}
