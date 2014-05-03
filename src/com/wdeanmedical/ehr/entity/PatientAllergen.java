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
  
  private Integer patientId;
  private Allergen allergen;
  private String comment;

  public PatientAllergen() {
  }



  @Column(name = "patient_id")
  public Integer getPatientId() { return patientId; }
  public void setPatientId(Integer patientId) { this.patientId = patientId; }

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

 

}
