package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "allergen")
public class Allergen extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 8117552004340724207L;
  
  private String name;

  public Allergen() {
  }

  @Column(name = "name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
