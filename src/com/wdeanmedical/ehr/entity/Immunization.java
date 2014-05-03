package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "immunization")
public class Immunization extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 8040984258720719750L;
  private String name;

  public Immunization() {
  }

  @Column(name = "name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
