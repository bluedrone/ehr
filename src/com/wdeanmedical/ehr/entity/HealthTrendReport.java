package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "health_trend_report")
public class HealthTrendReport extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -6472438611028481446L;
  private String name;

  public HealthTrendReport() {
  }

  @Column(name = "name")
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  
}
