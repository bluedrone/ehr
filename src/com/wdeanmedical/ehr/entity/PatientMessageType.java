package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "patient_message_type")
public class PatientMessageType extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 2671345577570043946L;
  private String name;
  public static final int GENERAL = 1;
  public static final int MEDICAL_ADVICE = 2;
  public static final int RX_RENEWAL = 3;
  public static final int APPT_REQUEST = 4;
  public static final int INITIAL_APPT_REQUEST = 5;

  public PatientMessageType() {
  }

  @Column(name = "name")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

}
