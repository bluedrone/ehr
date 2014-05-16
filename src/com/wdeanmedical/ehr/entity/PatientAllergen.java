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
@Table(name = "patient_allergen")
public class PatientAllergen extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 3930862315213189346L;

  private Patient patient;
  private Allergen allergen;
  private String comment;

  public PatientAllergen() {
  }

  @JoinColumn(name = "patient", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public Patient getPatient() {
    return patient;
  }

  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  @JoinColumn(name = "allergen", referencedColumnName = "id")
  @ManyToOne(optional = false)
  public Allergen getAllergen() {
    return allergen;
  }

  public void setAllergen(Allergen allergen) {
    this.allergen = allergen;
  }

  @Column(name = "comment")
  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result
        + ((allergen == null) ? 0 : allergen.hashCode());
    result = prime * result + ((comment == null) ? 0 : comment.hashCode());
    result = prime * result + ((patient == null) ? 0 : patient.hashCode());
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
    PatientAllergen other = (PatientAllergen) obj;
    if (allergen == null) {
      if (other.allergen != null)
        {return false;}
    } else if (!allergen.equals(other.allergen))
      {return false;}
    if (comment == null) {
      if (other.comment != null)
        {return false;}
    } else if (!comment.equals(other.comment))
      {return false;}
    if (patient == null) {
      if (other.patient != null)
        {return false;}
    } else if (!patient.equals(other.patient))
      {return false;}
    return true;
  }

  @Override
  public String toString() {
    return "PatientAllergen [patient=" + patient + ", allergen=" + allergen
        + ", comment=" + comment + "]";
  }

}
