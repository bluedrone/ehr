/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */
 
package com.wdeanmedical.ehr.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "patient_medical_test_component")
public class PatientMedicalTestComponent extends BaseEntity implements
    Serializable {

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
  public Integer getPatientMedicalTestId() {
    return patientMedicalTestId;
  }

  public void setPatientMedicalTestId(Integer patientMedicalTestId) {
    this.patientMedicalTestId = patientMedicalTestId;
  }

  @Column(name = "name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "test_value")
  public String getTestValue() {
    return testValue;
  }

  public void setTestValue(String testValue) {
    this.testValue = testValue;
  }

  @Column(name = "test_range")
  public String getTestRange() {
    return testRange;
  }

  public void setTestRange(String testRange) {
    this.testRange = testRange;
  }

  @Column(name = "units")
  public String getUnits() {
    return units;
  }

  public void setUnits(String units) {
    this.units = units;
  }

  @Column(name = "flag")
  public String getFlag() {
    return flag;
  }

  public void setFlag(String flag) {
    this.flag = flag;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((flag == null) ? 0 : flag.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime
        * result
        + ((patientMedicalTestId == null) ? 0 : patientMedicalTestId
            .hashCode());
    result = prime * result
        + ((testRange == null) ? 0 : testRange.hashCode());
    result = prime * result
        + ((testValue == null) ? 0 : testValue.hashCode());
    result = prime * result + ((units == null) ? 0 : units.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      {return false;}
    if (getClass() != obj.getClass())
      {return false;}
    PatientMedicalTestComponent other = (PatientMedicalTestComponent) obj;
    if (flag == null) {
      if (other.flag != null)
        {return false;}
    } else if (!flag.equals(other.flag))
      {return false;}
    if (name == null) {
      if (other.name != null)
        {return false;}
    } else if (!name.equals(other.name))
      {return false;}
    if (patientMedicalTestId == null) {
      if (other.patientMedicalTestId != null)
        {return false;}
    } else if (!patientMedicalTestId.equals(other.patientMedicalTestId))
      {return false;}
    if (testRange == null) {
      if (other.testRange != null)
        {return false;}
    } else if (!testRange.equals(other.testRange))
      {return false;}
    if (testValue == null) {
      if (other.testValue != null)
        {return false;}
    } else if (!testValue.equals(other.testValue))
      {return false;}
    if (units == null) {
      if (other.units != null)
        {return false;}
    } else if (!units.equals(other.units))
      {return false;}
    return true;
  }

  @Override
  public String toString() {
    return "PatientMedicalTestComponent [patientMedicalTestId="
        + patientMedicalTestId + ", name=" + name + ", testValue="
        + testValue + ", testRange=" + testRange + ", units=" + units
        + ", flag=" + flag + "]";
  }

}
