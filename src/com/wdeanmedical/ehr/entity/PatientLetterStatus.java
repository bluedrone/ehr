package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "patient_letter_status")
public class PatientLetterStatus extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 2784612919957439764L;
  private String name;

  public PatientLetterStatus() {
  }

  @Column(name = "name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
