package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "patient_letter_reason")
public class PatientLetterReason extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 132835305124405096L;
  private String name;

  public PatientLetterReason() {
  }

  @Column(name = "name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
