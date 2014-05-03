package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "medical_test")
public class MedicalTest extends BaseEntity implements Serializable {
  private static final long serialVersionUID = -8385738425289498337L;
  private String name;

  public MedicalTest() {
  }

  @Column(name = "name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
