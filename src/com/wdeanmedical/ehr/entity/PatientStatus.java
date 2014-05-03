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
  
  public static final int ACTIVE   =  1;
  public static final int INACTIVE =  2;
  public static final int DECEASED = 3;
  public static final int PURGED = 4;
  
  private String name;

  public PatientStatus() {
  }

  @Column(name = "name")
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  
}
