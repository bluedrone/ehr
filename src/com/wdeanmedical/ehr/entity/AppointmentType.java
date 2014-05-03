package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "appointment_type")
public class AppointmentType extends BaseEntity implements Serializable {
   private static final long serialVersionUID = -3127553778387743952L;
   
   private String name;

  public AppointmentType() {
  }

  @Column(name = "name")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

}
