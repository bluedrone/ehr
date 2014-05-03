package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "health_issue")
public class HealthIssue extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -4465643606350830534L;
  private String name;

  public HealthIssue() {
  }

  @Column(name = "name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
