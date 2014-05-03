package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "medical_procedure")
public class MedicalProcedure extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 9085601505369483654L;
  private String name;

  public MedicalProcedure() {
  }

  @Column(name = "name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
