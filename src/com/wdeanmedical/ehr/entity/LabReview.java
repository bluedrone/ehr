package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "lab_review")
public class LabReview extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 8448261762534690734L;

  private Patient patient;
  private Clinician clinician;
  private Date date;
  private String name;
  private String value;

  public LabReview() {
  }

  @JoinColumn(name = "patient", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public Patient getPatient() {
    return patient;
  }

  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  @Column(name = "date")
  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  @JoinColumn(name = "clinician", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public Clinician getClinician() {
    return clinician;
  }

  public void setClinician(Clinician clinician) {
    this.clinician = clinician;
  }

  @Column(name = "name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "value")
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result
        + ((clinician == null) ? 0 : clinician.hashCode());
    result = prime * result + ((date == null) ? 0 : date.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((patient == null) ? 0 : patient.hashCode());
    result = prime * result + ((value == null) ? 0 : value.hashCode());
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
    LabReview other = (LabReview) obj;
    if (clinician == null) {
      if (other.clinician != null)
        {return false;}
    } else if (!clinician.equals(other.clinician))
      {return false;}
    if (date == null) {
      if (other.date != null)
        {return false;}
    } else if (!date.equals(other.date))
      {return false;}
    if (name == null) {
      if (other.name != null)
        {return false;}
    } else if (!name.equals(other.name))
      {return false;}
    if (patient == null) {
      if (other.patient != null)
        {return false;}
    } else if (!patient.equals(other.patient))
      {return false;}
    if (value == null) {
      if (other.value != null)
        {return false;}
    } else if (!value.equals(other.value))
      {return false;}
    return true;
  }

  @Override
  public String toString() {
    return "LabReview [patient=" + patient + ", clinician=" + clinician
        + ", date=" + date + ", name=" + name + ", value=" + value
        + "]";
  }

}
