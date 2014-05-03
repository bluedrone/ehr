package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.IndexColumn;

@Entity
@Table(name = "patient_intake_group")
public class PatientIntakeGroup extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 5791055194437842209L;
  private int sortOrder;
  private String name;
  private List<Encounter> encounterList;
    

  public PatientIntakeGroup() {
  }
  
  @Column(name = "sort_order")
  @Basic(optional = false)  
  public int getSortOrder() { return sortOrder; }
  public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
    
  @Column(name = "name")
  @Basic(optional = false)  
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
   
  @Transient
  public List<Encounter> getEncounterList() { return encounterList; }
  public void setEncounterList(List<Encounter> encounterList) { this.encounterList = encounterList; }

  @Override
  public String toString() {
    return "com.wdeanmedical.ehr.entity.PatientIntakeGroup[id=" + getId() + "]";
  }

    
}
