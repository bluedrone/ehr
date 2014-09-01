package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "report")
public class Report extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 4625281727164584469L;

  private String name;
  private String title;
  private String description;
  private int sortOrder;
    

  public Report() {
  }
   
   
  @Column(name = "name")
  @Basic(optional = false)  
   public String getName() { return name; }
   public void setName(String name) { this.name = name; }
    
  @Column(name = "title")
  @Basic(optional = false)  
  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }
   
  @Column(name = "description")
  @Basic(optional = false)  
  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }
   
  @Column(name = "sort_order")
  @Basic(optional = false)  
  public int getSortOrder() { return sortOrder; }
  public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
    
}
