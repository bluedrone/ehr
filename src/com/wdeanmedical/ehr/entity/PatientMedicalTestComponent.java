package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "patient_medical_test_component")
public class PatientMedicalTestComponent extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -3974028308118232534L;
  
  private Integer patientMedicalTestId;
  private String name;
  private String testValue;
  private String testRange;
  private String units;
  private String flag;

  public PatientMedicalTestComponent() {
  }

  @Column(name = "patient_medical_test_id")
  public Integer getPatientMedicalTestId() { return patientMedicalTestId; }
  public void setPatientMedicalTestId(Integer patientMedicalTestId) { this.patientMedicalTestId = patientMedicalTestId; }
  
  @Column(name = "name")
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  @Column(name = "test_value")
  public String getTestValue() { return testValue; }
  public void setTestValue(String testValue) { this.testValue = testValue; }

  @Column(name = "test_range")
  public String getTestRange() { return testRange; }
  public void setTestRange(String testRange) { this.testRange = testRange; }

  @Column(name = "units")
  public String getUnits() { return units; }
  public void setUnits(String units) { this.units = units; }

  @Column(name = "flag")
  public String getFlag() { return flag; }
  public void setFlag(String flag) { this.flag = flag; }
 
}
