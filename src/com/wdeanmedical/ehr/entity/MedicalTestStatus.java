package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "medical_test_status")
public class MedicalTestStatus extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 5267445570839330301L;
  private String name;

  public MedicalTestStatus() {
  }

  @Column(name = "name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
