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
  public String toString() {
    return "com.wdeanmedical.ehr.entity.Country[id=" + getId() + "]";
  }

    
}
